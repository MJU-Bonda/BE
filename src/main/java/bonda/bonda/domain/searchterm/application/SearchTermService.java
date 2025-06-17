package bonda.bonda.domain.searchterm.application;

import bonda.bonda.domain.member.domain.Member;
import bonda.bonda.domain.member.domain.repository.MemberRepository;
import bonda.bonda.domain.searchterm.dto.response.RecentSearchRes;
import bonda.bonda.global.common.Message;
import bonda.bonda.global.common.SuccessResponse;
import bonda.bonda.infrastructure.redis.RedisListUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class SearchTermService {

    private static final Integer MAX_RECENT_SEARCH_TERMS = 5;
    private static final String RS_PREFIX = "RS_";  // RS = Recent Search : 최근 검색

    private final MemberRepository memberRepository;

    private final RedisListUtil redisUtil;

    @Transactional
    public SuccessResponse<Message> saveSearchTerm(Member member, String searchTerm) {
        Message message;

        if(!member.getAutoSave()){
            message = Message.builder()
                    .message("자동 저장이 꺼져 있어 검색어가 저장되지 않습니다.")
                    .build();
            return SuccessResponse.of(message);
        }

        redisUtil.addToRecentList(RS_PREFIX + member.getId(), searchTerm, MAX_RECENT_SEARCH_TERMS);

        message = Message.builder()
                .message("검색어가 저장이 되었습니다.")
                .build();

        return SuccessResponse.of(message);
    }

    @Transactional
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
}
