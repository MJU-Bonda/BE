package bonda.bonda.domain.badge.application;

import bonda.bonda.domain.articlecase.repository.ArticlecaseRepository;
import bonda.bonda.domain.badge.domain.Badge;
import bonda.bonda.domain.badge.domain.ProgressType;
import bonda.bonda.domain.badge.domain.repository.BadgeRepository;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BadgeService {

    private final String BADGES_FOLDERNAME = "badges";

    private final BadgeRepository badgeRepository;
    private final MemberRepository memberRepository;
    private final MemberBadgeRepository memberBadgeRepository;
    private final RecentViewBookRepository recentViewBookRepository;
    private final RecentViewArticleRepository recentViewArticleRepository;
    private final BookcaseRepository bookcaseRepository;
    private final ArticlecaseRepository articlecaseRepository;

    private final S3Service s3Service;

    private record BadgeInit(String name, String description, String image, ProgressType progressType, Integer goal) {};

    @PostConstruct
    public void init() {
        List<BadgeInit> badges = List.of(
                new BadgeInit("느긋한 발걸음", "도서 상세페이지를 처음 조회하면 획득할 수 있어요.", null, ProgressType.BOOK_VIEW, 1),
                new BadgeInit("길찾기 초보자", "도서 상세페이지 5개를 조회하면 획득할 수 있어요.", null, ProgressType.BOOK_VIEW, 5),
                new BadgeInit("발견의 마법사", "도서 상세페이지 10개를 조회하면 획득할 수 있어요.", null, ProgressType.BOOK_VIEW, 10),

                new BadgeInit("첫 장의 설렘", "아티클을 처음 조회하면 획득할 수 있어요.", null, ProgressType.ARTICLE_VIEW, 1),
                new BadgeInit("페이지 여행자", "아티클 5개를 조회하면 획득할 수 있어요.", null, ProgressType.ARTICLE_VIEW, 5),
                new BadgeInit("서가 산책", "아티클 10개를 조회하면 획득할 수 있어요.", null, ProgressType.ARTICLE_VIEW, 10),

                new BadgeInit("나의 첫 번째", "도서를 처음 저장하면 획득할 수 있어요.", null, ProgressType.BOOK_SAVE, 1),
                new BadgeInit("책갈피 수집가", "도서 3개를 저장하면 획득할 수 있어요.", null, ProgressType.BOOK_SAVE, 3),
                new BadgeInit("서재 채집가", "도서 5개를 저장하면 획득할 수 있어요.", null, ProgressType.BOOK_SAVE, 5),

                new BadgeInit("첫 번째 서랍", "아티클을 처음 저장하면 획득할 수 있어요.", null, ProgressType.ARTICLE_SAVE, 1),
                new BadgeInit("기억의 조각", "아티클 3개를 저장하면 획득할 수 있어요.", null, ProgressType.ARTICLE_SAVE, 3),
                new BadgeInit("취향 기록가", "아티클 5개를 저장하면 획득할 수 있어요.", null, ProgressType.ARTICLE_SAVE, 5)
        );

        for (BadgeInit badge : badges) {
            if(!badgeRepository.existsByName(badge.name)) {
                Badge newBadge = Badge.builder()
                        .name(badge.name)
                        .description(badge.description)
                        .image(badge.image)
                        .progressType(badge.progressType)
                        .goal(badge.goal)
                        .build();
                badgeRepository.save(newBadge);
            }
        }
    }

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
                    .image(badge.getImage())
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
                .badgeCount(unlockedBadgeIds.size())
                .viewBadgeList(viewBadgeList)
                .saveBadgeList(saveBadgeList)
                .build();

        return SuccessResponse.of(myBadgeListRes);
    }

    private Boolean isViewType(ProgressType progressType) {
        return progressType == ProgressType.BOOK_VIEW || progressType == ProgressType.ARTICLE_VIEW;
    }
}