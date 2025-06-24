package bonda.bonda.domain.badge.presentation;

import bonda.bonda.domain.badge.application.BadgeService;
import bonda.bonda.domain.badge.dto.response.MyBadgeListRes;
import bonda.bonda.domain.member.domain.Member;
import bonda.bonda.global.annotation.LoginMember;
import bonda.bonda.global.common.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/badges")
public class BadgeController {

    private final BadgeService badgeService;

    @GetMapping("/me")
    public ResponseEntity<SuccessResponse<MyBadgeListRes>> getMyBadgeList(@LoginMember Member member) {
        Long memberId = member.getId();
        return ResponseEntity.ok(badgeService.getMyBadgeList(memberId));
    }
}
