package bonda.bonda.domain.badge.application;

import bonda.bonda.domain.articlecase.repository.ArticlecaseRepository;
import bonda.bonda.domain.badge.domain.Badge;
import bonda.bonda.domain.badge.domain.ProgressType;
import bonda.bonda.domain.badge.domain.repository.BadgeRepository;
import bonda.bonda.domain.badge.dto.response.BadgeDetailsRes;
import bonda.bonda.domain.badge.dto.response.BadgeRes;
import bonda.bonda.domain.badge.dto.response.MyBadgeListRes;
import bonda.bonda.domain.bookcase.repository.BookcaseRepository;
import bonda.bonda.domain.member.domain.Member;
import bonda.bonda.domain.member.domain.repository.MemberRepository;
import bonda.bonda.domain.memberbadge.MemberBadge;
import bonda.bonda.domain.memberbadge.repository.MemberBadgeRepository;
import bonda.bonda.domain.recentviewarticle.repository.RecentViewArticleRepository;
import bonda.bonda.domain.recentviewbook.repository.RecentViewBookRepository;
import bonda.bonda.global.common.SuccessResponse;
import bonda.bonda.global.common.aws.S3Service;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BadgeService {

    private final BadgeRepository badgeRepository;
    private final MemberRepository memberRepository;
    private final MemberBadgeRepository memberBadgeRepository;
    private final RecentViewBookRepository recentViewBookRepository;
    private final RecentViewArticleRepository recentViewArticleRepository;
    private final BookcaseRepository bookcaseRepository;
    private final ArticlecaseRepository articlecaseRepository;

    /**현재 멤버의 상태에 따라 뱃지 생성 및 생성 여부 반환**/
    @Transactional
    public boolean checkAndAwardBadges(Member member, ProgressType progressType) {
        boolean isAwarded = false; //생성 여부
        int progress = switch (progressType) {   //  진척도 유형에 따른 현재 진척도 가져오기
            case BOOK_VIEW -> recentViewBookRepository.countByMember(member); //책 조회
            case ARTICLE_VIEW -> recentViewArticleRepository.countByMember(member); //아티클 조회
            case BOOK_SAVE -> bookcaseRepository.countByMember(member); //책 저장
            case ARTICLE_SAVE -> articlecaseRepository.countByMember(member); //아티클 저장
        };
        //현재 조건에 맞는 뱃지 목록 가져오기
        List<Badge> badges = badgeRepository.findByProgressType(progressType);
        for (Badge badge : badges) { //뱃지 돌면서, 현재 진척도와 비교
            if (progress >= badge.getGoal() && !memberBadgeRepository.existsByMemberAndBadge(member, badge)) { //현재 없으면서, 진척도 달생 시
                memberBadgeRepository.save(new MemberBadge(member, badge)); // 저장
                member.plusBadgeCount(); //뱃지 카운트 더하기
                isAwarded = true; //생성 여부
            }
        }
        return isAwarded;
    }

    public SuccessResponse<MyBadgeListRes> getMyBadgeList(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BadCredentialsException("해당 아이디를 가진 멤버가 없습니다."));

        List<Badge> allBadge = badgeRepository.findAll();
        Set<Long> unlockedBadgeIds = memberBadgeRepository.findByMember(member).stream()
                .map(mb -> mb.getBadge().getId())
                .collect(Collectors.toSet());

        List<BadgeRes> viewBadgeList = new ArrayList<>();
        List<BadgeRes> saveBadgeList = new ArrayList<>();

        for (Badge badge : allBadge) {
            BadgeRes badgeRes = BadgeRes.builder()
                    .Id(badge.getId())
                    .name(badge.getName())
                    .isUnlocked(unlockedBadgeIds.contains(badge.getId()))
                    .build();

            if (isViewType(badge.getProgressType())) {
                viewBadgeList.add(badgeRes);
            }
            else {
                saveBadgeList.add(badgeRes);
            }
        }

        MyBadgeListRes myBadgeListRes = MyBadgeListRes.builder()
                .badgeCount(member.getBadgeCount())
                .viewBadgeList(viewBadgeList)
                .saveBadgeList(saveBadgeList)
                .build();

        return SuccessResponse.of(myBadgeListRes);
    }

    private Boolean isViewType(ProgressType progressType) {
        return progressType == ProgressType.BOOK_VIEW || progressType == ProgressType.ARTICLE_VIEW;
    }

    public SuccessResponse<BadgeDetailsRes> getBadgeDetails(Long memberId, Long badgeId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BadCredentialsException("해당 아이디의 멤버를 찾을 수 없습니다."));
        Badge badge = badgeRepository.findById(badgeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 번호의 뱃지를 찾을 수 없습니다."));

        boolean isUnlock = memberBadgeRepository.existsByMemberAndBadge(member, badge);
        LocalDate acquiredDate= null;
        if(isUnlock) {
            acquiredDate = memberBadgeRepository.findByMemberAndBadge(member, badge)
                    .map(mb -> mb.getCreatedAt().toLocalDate())
                    .orElse(null);
        }

        int progress = switch (badge.getProgressType()) {
            case BOOK_VIEW ->recentViewBookRepository.countByMember(member);
            case BOOK_SAVE -> bookcaseRepository.countByMember(member);
            case ARTICLE_VIEW -> recentViewArticleRepository.countByMember(member);
            case ARTICLE_SAVE -> articlecaseRepository.countByMember(member);
        };

        String description = isUnlock
                ? (badge.getGoal() == 1
                    ? String.format("첫 %s %s하여 '%s' 뱃지를 획득했어요",
                                    getContentType(badge.getProgressType()),
                                    getCategory(badge.getProgressType()),
                                    badge.getName())
                    : String.format("%d개의 %s %s하여 '%s' 뱃지를 획득했어요",
                                    badge.getGoal(),
                                    getContentType(badge.getProgressType()),
                                    getCategory(badge.getProgressType()),
                                    badge.getName())
                )
                : badge.getDescription();

        BadgeDetailsRes badgeDetailsRes = BadgeDetailsRes.builder()
                .name(badge.getName())
                .description(description)
                .progressType(badge.getProgressType())
                .currentProgress(progress)
                .goal(badge.getGoal())
                .isUnlocked(isUnlock)
                .acquiredDate(acquiredDate)
                .build();

        return SuccessResponse.of(badgeDetailsRes);
    }

    private String getContentType(ProgressType progressType) {
        return switch (progressType) {
            case BOOK_VIEW, BOOK_SAVE -> "도서를";
            case ARTICLE_VIEW, ARTICLE_SAVE -> "아티클을";
        };
    }

    private String getCategory(ProgressType progressType) {
        return switch (progressType) {
            case BOOK_VIEW, ARTICLE_VIEW -> "조회";
            case BOOK_SAVE, ARTICLE_SAVE -> "저장";
        };
    }
}