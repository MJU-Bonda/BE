package bonda.bonda.domain.searchterm.presentation;

import bonda.bonda.domain.member.domain.Member;
import bonda.bonda.domain.searchterm.application.SearchTermService;
import bonda.bonda.domain.searchterm.dto.response.ModifyAutoSave;
import bonda.bonda.domain.searchterm.dto.response.RecentSearchRes;
import bonda.bonda.domain.searchterm.dto.response.RecommendKeywordsRes;
import bonda.bonda.global.annotation.LoginMember;
import bonda.bonda.global.common.Message;
import bonda.bonda.global.common.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/search-term")
public class SearchTermController implements SearchTermApi {

    private final SearchTermService searchTermService;

    @Override
    @GetMapping("/recent")
    public ResponseEntity<SuccessResponse<RecentSearchRes>> getRecentSearchTerms(@LoginMember Member member) {
        Long memberId = member.getId();
        return ResponseEntity.ok(searchTermService.getRecentSearchTerms(memberId));
    }

    @Override
    @DeleteMapping("/recent")
    public ResponseEntity<SuccessResponse<Message>> deleteSearchTerm(@LoginMember Member member, @RequestParam String keyword) {
        Long memberId = member.getId();
        return ResponseEntity.ok(searchTermService.deleteSearchTerm(memberId, keyword));
    }

    @Override
    @DeleteMapping("/recent/all")
    public ResponseEntity<SuccessResponse<Message>> deleteAllSearchTerm(@LoginMember Member member) {
        Long memberId = member.getId();
        return ResponseEntity.ok(searchTermService.deleteAllSearchTerm(memberId));
    }

    @Override
    @GetMapping("/recommend")
    public ResponseEntity<SuccessResponse<RecommendKeywordsRes>> getRecommendKeywords() {
        return ResponseEntity.ok(searchTermService.getRecommendKeyword());
    }

    @PatchMapping("/auto-save")
    public ResponseEntity<SuccessResponse<ModifyAutoSave>> modifyAutoSave(@LoginMember Member member) {
        Long memberId = member.getId();
        return ResponseEntity.ok(searchTermService.modifyAutoSave(memberId));
    }
}
