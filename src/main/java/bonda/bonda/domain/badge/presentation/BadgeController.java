package bonda.bonda.domain.badge.presentation;

import bonda.bonda.domain.badge.application.BadgeService;
import bonda.bonda.domain.badge.dto.response.BadgeDetailsRes;
import bonda.bonda.domain.badge.dto.response.MyBadgeListRes;
import bonda.bonda.domain.member.domain.Member;
import bonda.bonda.global.annotation.LoginMember;
import bonda.bonda.global.common.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/badges")
public class BadgeController implements BadgeApi {

    private final BadgeService badgeService;

    @Override
    @GetMapping("/me")
    public ResponseEntity<SuccessResponse<MyBadgeListRes>> getMyBadgeList(@LoginMember Member member) {
        Long memberId = member.getId();
        return ResponseEntity.ok(badgeService.getMyBadgeList(memberId));
    }

    @GetMapping("/{badgeId}")
    public ResponseEntity<SuccessResponse<BadgeDetailsRes>> getBadgeDetails(@LoginMember Member member, @PathVariable Long badgeId) {
        Long memberId = member.getId();
        return ResponseEntity.ok(badgeService.getBadgeDetails(memberId, badgeId));
    }
}
