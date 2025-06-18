package bonda.bonda.domain.badge.application;

import bonda.bonda.domain.articlecase.repository.ArticlecaseRepository;
import bonda.bonda.domain.badge.domain.Badge;
import bonda.bonda.domain.badge.domain.ProgressType;
import bonda.bonda.domain.badge.domain.repository.BadgeRepository;
import bonda.bonda.domain.bookcase.repository.BookcaseRepository;
import bonda.bonda.domain.member.domain.Member;
import bonda.bonda.domain.memberbadge.MemberBadge;
import bonda.bonda.domain.memberbadge.repository.MemberBadgeRepository;
import bonda.bonda.domain.recentviewarticle.repository.RecentViewArticleRepository;
import bonda.bonda.domain.recentviewbook.repository.RecentViewBookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BadgeService {

    private final BadgeRepository badgeRepository;
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
}