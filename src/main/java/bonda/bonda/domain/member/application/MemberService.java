package bonda.bonda.domain.member.application;

import bonda.bonda.domain.member.domain.Member;
import bonda.bonda.domain.member.domain.repository.MemberRepository;
import bonda.bonda.domain.member.dto.response.MyPageInfoRes;
import bonda.bonda.global.annotation.LoginMember;
import bonda.bonda.global.common.Message;
import bonda.bonda.global.common.SuccessResponse;
import bonda.bonda.global.common.aws.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService {

    private final String MEBMER_FOLDERNAME = "members";

    private final MemberRepository memberRepository;
    private final S3Service s3Service;

    public SuccessResponse<MyPageInfoRes> getMyPageInfo(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BadCredentialsException("해당하는 멤버를 찾을 수 없습니다."));

        MyPageInfoRes myPageInfoRes = MyPageInfoRes.builder()
                .nickname(member.getNickname())
                .profileImage(member.getProfileImage())
                .savedBookCount(member.getSaveCount())
                .badgeCount(member.getBadgeCount())
                .build();

        return SuccessResponse.of(myPageInfoRes);
    }

    @Transactional
    public SuccessResponse<Message> updateNicknameAndProfileImage(Long memberId, String nickname, MultipartFile profileImage) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BadCredentialsException("해당하는 멤버를 찾을 수 없습니다."));

        // 닉네임 수정
        if(nickname != null && !nickname.trim().isEmpty()) {
            member.updateNickname(nickname);
        }

        // 프로필 이미지 수정
        if(!profileImage.isEmpty()) {
            if(member.getProfileImage() != null && !member.getProfileImage().isEmpty()) {
                s3Service.deleteImage(member.getProfileImage());
            }

            member.updateProfileImage(s3Service.uploadImage(MEBMER_FOLDERNAME, profileImage));
        }

        Message message = Message.builder()
                .message("프로필 변경이 완료되었습니다.")
                .build();

        return SuccessResponse.of(message);
    }
}
