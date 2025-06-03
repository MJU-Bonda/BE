package bonda.bonda.domain.auth.application;

import bonda.bonda.domain.auth.dto.request.LoginReq;
import bonda.bonda.domain.auth.dto.response.ReissueRes;
import bonda.bonda.domain.member.dto.response.KakaoMemberRes;
import bonda.bonda.domain.auth.dto.response.LoginRes;
import bonda.bonda.domain.member.domain.Member;
import bonda.bonda.domain.member.domain.repository.MemberRepository;
import bonda.bonda.global.common.SuccessResponse;
import bonda.bonda.global.exception.BusinessException;
import bonda.bonda.global.exception.ErrorCode;
import bonda.bonda.global.security.jwt.JwtTokenProvider;
import bonda.bonda.infrastructure.redis.RedisUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AuthService {

    @Value("${jwt.refresh.expiration}")
    private Long refreshExpiration;     // RefreshToken 유효기간(2주)

    private static String RT_PREFIX = "RT_";        // RefreshToken 접두사
    private static String BL_AT_PREFIX = "BL_AT_";  // Blacklist에 등록된 AccessToken 접두사

    private final KakaoTokenValidator kakaoTokenValidator;
    private final MemberRepository memberRepository;

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

        redisUtil.setDataExpire(RT_PREFIX + refreshToken, member.getNickname(), refreshExpiration);

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
        DecodedJWT decodedJWT = JWT.decode(refreshToken);
        Instant expireAt = decodedJWT.getExpiresAt().toInstant();
        if (!jwtTokenProvider.isTokenValid(refreshToken))
            throw new TokenExpiredException("유효하지 않은 refreshToken 입니다.", expireAt);

        String nickname = redisUtil.getData(RT_PREFIX + refreshToken);
        Member member = memberRepository.findByNickname(nickname)
                .orElseThrow(() -> new BusinessException(ErrorCode.UNAUTHORIZED_ERROR));

        String accesstoken = jwtTokenProvider.createAccessToken(member);

        ReissueRes reissueRes = ReissueRes.builder()
                .accessToken(accesstoken)
                .build();

        return SuccessResponse.of(reissueRes);
    }
}
