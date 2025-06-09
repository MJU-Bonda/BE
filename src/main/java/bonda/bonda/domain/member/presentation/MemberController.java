package bonda.bonda.domain.member.presentation;

import bonda.bonda.domain.member.application.MemberService;
import bonda.bonda.domain.member.domain.Member;
import bonda.bonda.domain.member.dto.response.MyPageInfoRes;
import bonda.bonda.global.annotation.LoginMember;
import bonda.bonda.global.common.Message;
import bonda.bonda.global.common.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
public class MemberController implements MemberApi {

    private final MemberService memberService;

    @GetMapping("/myPage")
    public ResponseEntity<SuccessResponse<MyPageInfoRes>> getMyPageInfo(@LoginMember Member member) {
        Long memberId = member.getId();
        return ResponseEntity.ok(memberService.getMyPageInfo(memberId));
    }

    @Override
    @PutMapping(value = "/update/nickname-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SuccessResponse<Message>> updateNicknameAndProfileImage(@LoginMember Member member,
                                                                                  @RequestPart(required = false) String nickname,
                                                                                  @RequestPart(required = false) MultipartFile profileImage) {
        Long memberId = member.getId();
        return ResponseEntity.ok(memberService.updateNicknameAndProfileImage(memberId, nickname, profileImage));
    }
}
