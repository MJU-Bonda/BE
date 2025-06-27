package bonda.bonda.domain.member.application;

import bonda.bonda.domain.book.domain.BookCategory;
import bonda.bonda.domain.book.domain.repository.BookRepository;
import bonda.bonda.domain.bookcase.repository.BookcaseRepository;
import bonda.bonda.domain.member.domain.Member;
import bonda.bonda.domain.member.domain.repository.MemberRepository;
import bonda.bonda.domain.member.dto.response.CategoryCountDTO;
import bonda.bonda.domain.member.dto.response.MyActivityRes;
import bonda.bonda.domain.member.dto.response.MyPageInfoRes;
import bonda.bonda.domain.recentviewbook.repository.RecentViewBookRepository;
import bonda.bonda.global.common.Message;
import bonda.bonda.global.common.SuccessResponse;
import bonda.bonda.global.common.aws.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService {

    private final String MEBMER_FOLDERNAME = "members";

    private final MemberRepository memberRepository;
    private final RecentViewBookRepository recentViewBookRepository;
    private final BookcaseRepository bookcaseRepository;

    private final S3Service s3Service;
    private final BookRepository bookRepository;

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
        if(profileImage != null && !profileImage.isEmpty()) {
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

    public SuccessResponse<MyActivityRes> myActivityBooks(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BadCredentialsException("해당하는 멤버를 찾을 수 없습니다."));

        int bookViewCount = recentViewBookRepository.countByMember(member);
        int bookcaseCount = bookcaseRepository.countByMember(member);

        Map<BookCategory, Long> categoryCountMap = bookRepository.countBookcaseByCategory(member);
        List<CategoryCountDTO> top3PlusEtc = formatTop3AndEtcEnum(categoryCountMap);

        MyActivityRes myActivityRes = MyActivityRes.builder()
                .bookViewCount(bookViewCount)
                .bookcaseCount(bookcaseCount)
                .categoryCountList(top3PlusEtc)
                .build();

        return SuccessResponse.of(myActivityRes);
    }

    private List<CategoryCountDTO> formatTop3AndEtcEnum(Map<BookCategory, Long> categoryCountMap) {
        List<Map.Entry<BookCategory, Long>> sorted = categoryCountMap.entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
                .toList();

        List<CategoryCountDTO> result = new ArrayList<>();
        Long etcCount = 0L;

        for (int i = 0; i < sorted.size(); i++) {
            Map.Entry<BookCategory, Long> entry = sorted.get(i);
            if(i < 3) {
                result.add(new CategoryCountDTO(entry.getKey().getValue(), entry.getValue()));
            } else {
                etcCount += entry.getValue();
            }
        }

        if (etcCount > 0) {
            result.add(new CategoryCountDTO("기타", etcCount));
        }

        return result;
    }
}
