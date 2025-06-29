package bonda.bonda.domain.auth.application;

import bonda.bonda.domain.articlecase.Articlecase;
import bonda.bonda.domain.articlecase.repository.ArticlecaseRepository;
import bonda.bonda.domain.auth.dto.request.LoginReq;
import bonda.bonda.domain.auth.dto.request.LogoutReq;
import bonda.bonda.domain.auth.dto.response.ReissueRes;
import bonda.bonda.domain.bookcase.Bookcase;
import bonda.bonda.domain.bookcase.repository.BookcaseRepository;
import bonda.bonda.domain.member.dto.response.KakaoMemberRes;
import bonda.bonda.domain.auth.dto.response.LoginRes;
import bonda.bonda.domain.member.domain.Member;
import bonda.bonda.domain.member.domain.repository.MemberRepository;
import bonda.bonda.domain.memberbadge.MemberBadge;
import bonda.bonda.domain.memberbadge.repository.MemberBadgeRepository;
import bonda.bonda.domain.recentviewarticle.RecentViewArticle;
import bonda.bonda.domain.recentviewarticle.repository.RecentViewArticleRepository;
import bonda.bonda.domain.recentviewbook.RecentViewBook;
import bonda.bonda.domain.recentviewbook.repository.RecentViewBookRepository;
import bonda.bonda.domain.searchterm.application.SearchTermService;
import bonda.bonda.global.common.Message;
import bonda.bonda.global.common.SuccessResponse;
import bonda.bonda.global.security.jwt.JwtTokenProvider;
import bonda.bonda.infrastructure.redis.RedisUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AuthService {

    @Value("${jwt.refresh.expiration}")
    private Long refreshExpiration;     // RefreshToken 유효기간(2주)

    private static String RT_PREFIX = "RT_";        // RefreshToken 접두사
    private static String BL_AT_PREFIX = "BL_AT_";  // Blacklist에 등록된 AccessToken 접두사

    private final KakaoTokenValidator kakaoTokenValidator;
    private final SearchTermService searchTermService;
    private final MemberRepository memberRepository;
    private final MemberBadgeRepository memberBadgeRepository;
    private final BookcaseRepository bookcaseRepository;
    private final ArticlecaseRepository articlecaseRepository;
    private final RecentViewBookRepository recentViewBookRepository;
    private final RecentViewArticleRepository recentViewArticleRepository;

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisUtil redisUtil;

    @Transactional
    public SuccessResponse<LoginRes> signUpOrLogin(LoginReq loginReq) {
        String idToken = loginReq.getIdToken();
        KakaoMemberRes kakaoMemberRes = kakaoTokenValidator.validateAndExtract(idToken);

        Member member = memberRepository.findByKakaoId(kakaoMemberRes.getKakaoId())
                .orElseGet(() -> registerNewMember(kakaoMemberRes));

        boolean isNewMember = member.getNickname().startsWith("|KAKAO");      // |KAKAO로 시작하는 닉네임을 신규 유저로 생각

        String accessToken = jwtTokenProvider.createAccessToken(member);      // AccessToken 생성
        String refreshToken = jwtTokenProvider.createRefreshToken();          // RefreshToken 생성

        redisUtil.setDataExpire(RT_PREFIX + refreshToken, member.getKakaoId(), refreshExpiration);

        LoginRes loginRes = LoginRes.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .isNewUser(isNewMember)
                .build();

        return SuccessResponse.of(loginRes);
    }

    private Member registerNewMember(KakaoMemberRes kakaoMemberRes) {
        Member member = Member.builder()
                .kakaoId(kakaoMemberRes.getKakaoId())
                .nickname("|KAKAO" + (int)(Math.random() * 9000 + 1000))    // |KAKAO+랜덤숫자로 임의의 nickname 설정
                .profileImage(null)     // profileImage를 null로 저장하는 것은 기획에 따라 리팩토링 예정
                .build();

        memberRepository.save(member);

        return member;
    }

    public SuccessResponse<ReissueRes> reissue(String refreshToken) {
        if (!jwtTokenProvider.isTokenValid(refreshToken))
            throw new BadCredentialsException("유효하지 않은 refreshToken 입니다.");

        String kakaoId = redisUtil.getData(RT_PREFIX + refreshToken);
        Member member = memberRepository.findByKakaoId(kakaoId)
                .orElseThrow(() -> new BadCredentialsException("카카오 아이다가 다릅니다. 다시 로그인해주세요."));

        String accessToken = jwtTokenProvider.createAccessToken(member);

        ReissueRes reissueRes = ReissueRes.builder()
                .accessToken(accessToken)
                .build();

        return SuccessResponse.of(reissueRes);
    }

    @Transactional
    public SuccessResponse<Message> logout(Long memberId, LogoutReq logoutReq) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BadCredentialsException("해당하는 멤버를 찾을 수 없습니다."));

        String findKakaoId = redisUtil.getData(RT_PREFIX + logoutReq.getRefreshToken());
        if(!member.getKakaoId().equals(findKakaoId))
            throw new IllegalArgumentException("본인의 RefreshToken만 삭제 가능");
        redisUtil.deleteData(RT_PREFIX + logoutReq.getRefreshToken());

        // accessToken의 남은 유효시간 계산
        DecodedJWT decodedJWT = JWT.decode(logoutReq.getAccessToken());
        Instant expiredAt = decodedJWT.getExpiresAt().toInstant();
        Instant now = Instant.now();
        long between = ChronoUnit.SECONDS.between(now, expiredAt);

        // 남는 시간 만료만큼 AccessToken을 Blacklist에 포함
        redisUtil.setDataExpire(BL_AT_PREFIX + logoutReq.getAccessToken(), "black list token", between);

        Message message = Message.builder()
                .message("로그아웃이 완료되었습니다.")
                .build();

        return SuccessResponse.of(message);
    }

    @Transactional
    public SuccessResponse<Message> exit(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BadCredentialsException("해당하는 멤버를 찾을 수 없습니다."));

        // DB 내 멤버 관련 모든 데이터 삭제
        searchTermService.deleteAllSearchTerm(member.getId());
        List<Bookcase> bookcaseList = bookcaseRepository.findAllByMember(member);
        List<Articlecase> articlecaseList = articlecaseRepository.findAllByMember(member);
        List<RecentViewBook> recentViewBookList = recentViewBookRepository.findAllByMember(member);
        List<RecentViewArticle> recentViewArticleList = recentViewArticleRepository.findAllByMember(member);
        List<MemberBadge> memberBadgeList = memberBadgeRepository.findAllByMember(member);

        bookcaseRepository.deleteAll(bookcaseList);
        articlecaseRepository.deleteAll(articlecaseList);
        recentViewBookRepository.deleteAll(recentViewBookList);
        recentViewArticleRepository.deleteAll(recentViewArticleList);
        memberBadgeRepository.deleteAll(memberBadgeList);

        memberRepository.delete(member);

        Message message = Message.builder()
                .message("회원 탈퇴가 완료되었습니다.")
                .build();

        return SuccessResponse.of(message);
    }
}
