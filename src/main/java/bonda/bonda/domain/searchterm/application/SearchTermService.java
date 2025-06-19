package bonda.bonda.domain.searchterm.application;

import bonda.bonda.domain.member.domain.Member;
import bonda.bonda.domain.member.domain.repository.MemberRepository;
import bonda.bonda.domain.searchterm.dto.response.RecentSearchRes;
import bonda.bonda.global.common.Message;
import bonda.bonda.global.common.SuccessResponse;
import bonda.bonda.infrastructure.redis.RedisListUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
}
