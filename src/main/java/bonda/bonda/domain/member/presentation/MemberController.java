package bonda.bonda.domain.member.presentation;

import bonda.bonda.domain.member.application.MemberService;
import bonda.bonda.domain.member.domain.Member;
import bonda.bonda.global.annotation.LoginMember;
import bonda.bonda.global.common.Message;
import bonda.bonda.global.common.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @PutMapping("/update/nickname-image")
    public ResponseEntity<SuccessResponse<Message>> updateNicknameAndProfileImage(@LoginMember Member member,
                                                                                  @RequestPart(required = false) String nickname,
                                                                                  @RequestPart(required = false) MultipartFile profileImage) {
        Long memberId = member.getId();
        return ResponseEntity.ok(memberService.updateNicknameAndProfileImage(memberId, nickname, profileImage));
    }
}
