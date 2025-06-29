package bonda.bonda.domain.searchterm.application;

import bonda.bonda.domain.member.domain.Member;
import bonda.bonda.domain.member.domain.repository.MemberRepository;
import bonda.bonda.domain.searchterm.dto.response.ModifyAutoSave;
import bonda.bonda.domain.searchterm.dto.response.RecentSearchRes;
import bonda.bonda.domain.searchterm.dto.response.RecommendKeywordsRes;
import bonda.bonda.global.common.Message;
import bonda.bonda.global.common.SuccessResponse;
import bonda.bonda.infrastructure.redis.RedisListUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
@Slf4j
public class SearchTermService {

    private static final Integer MAX_RECENT_SEARCH_TERMS = 5;
    private static final String RS_PREFIX = "RS_";  // RS = Recent Search : 최근 검색

    private final MemberRepository memberRepository;

    private final RedisListUtil redisUtil;

    @Transactional
    public void saveSearchTerm(Member member, String searchTerm) {
        if(!member.getAutoSave()){
            log.info("자동 저장 OFF: memberId = {}, 검색어 = {}", member.getId(), searchTerm);
            return;
        }

        redisUtil.addToRecentList(RS_PREFIX + member.getId(), searchTerm, MAX_RECENT_SEARCH_TERMS);

        log.info("검색어 저장 완료: memberId = {}, 검색어 = {}", member.getId(), searchTerm);
    }

    public SuccessResponse<RecentSearchRes> getRecentSearchTerms(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BadCredentialsException("해당 아이디를 가진 멤버가 없습니다."));
        List<String> searchTermList = redisUtil.getList(RS_PREFIX + memberId);

        RecentSearchRes recentSearchRes = RecentSearchRes.builder()
                .recentSearchTermList(searchTermList)
                .autoSave(member.getAutoSave())
                .build();

        return SuccessResponse.of(recentSearchRes);
    }

    @Transactional
    public SuccessResponse<Message> deleteSearchTerm(Long memberId, String keyword) {
        redisUtil.removeItem(RS_PREFIX + memberId, keyword);

        Message message = Message.builder()
                .message("최근 검색어 " + keyword + " 삭제 완료되었습니다")
                .build();

        return SuccessResponse.of(message);
    }

    @Transactional
    public SuccessResponse<Message> deleteAllSearchTerm(Long memberId) {
        redisUtil.deleteList(RS_PREFIX + memberId);

        Message message = Message.builder()
                .message("최근 검색어 전체 삭제 완료되었씁니다.")
                .build();

        return SuccessResponse.of(message);
    }

    public SuccessResponse<RecommendKeywordsRes> getRecommendKeyword() {
        List<String> keywords = Arrays.asList(
                "여행", "미니멀리즘", "감정", "번아웃", "비건", "슬로우라이프", "혼자", "시골", "로컬", "책방",
                "커피", "취향", "반려식물", "반려동물", "인테리어", "공간", "독립출판", "영화", "음악", "글쓰기",
                "서사", "아카이빙", "한달살이", "인터뷰", "편집", "잡지", "상실", "동네", "불완전", "기분"
        );

        // 매일 달라지는 시드값 생성
        ZoneId KST = ZoneId.of("Asia/Seoul");
        long seed = LocalDate.now(KST).toEpochDay();

        // 시드값에 따라 랜덤한 3개 keywords 추출
        List<String> shuffled = new ArrayList<>(keywords);
        Collections.shuffle(shuffled, new Random(seed));

        // 3개 선택
        List<String> selectedThreeKeywords = shuffled.subList(0, 3);

        RecommendKeywordsRes recommendKeywordsRes = RecommendKeywordsRes.builder()
                .recommendKeywords(selectedThreeKeywords)
                .build();

        return SuccessResponse.of(recommendKeywordsRes);
    }

    @Transactional
    public SuccessResponse<ModifyAutoSave> modifyAutoSave(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BadCredentialsException("해당 아이디를 가진 멤버가 없습니다."));

        Boolean autoSave = member.getAutoSave();
        member.updateAutoSave(!autoSave);   // true -> false / false -> true

        if(autoSave) {
            redisUtil.deleteList(RS_PREFIX + memberId);
        }

        ModifyAutoSave modifyAutoSave = ModifyAutoSave.builder()
                .autoSave(!autoSave)
                .build();

        return SuccessResponse.of(modifyAutoSave);
    }
}
