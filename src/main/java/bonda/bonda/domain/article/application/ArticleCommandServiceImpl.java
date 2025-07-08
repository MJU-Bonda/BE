package bonda.bonda.domain.article.application;

import bonda.bonda.domain.article.domain.Article;
import bonda.bonda.domain.article.domain.repository.ArticleRepository;
import bonda.bonda.domain.article.dto.response.ToggleBookmarkRes;
import bonda.bonda.domain.articlecase.Articlecase;
import bonda.bonda.domain.articlecase.repository.ArticlecaseRepository;
import bonda.bonda.domain.badge.application.BadgeService;
import bonda.bonda.domain.badge.domain.ProgressType;
import bonda.bonda.domain.member.domain.Member;
import bonda.bonda.domain.member.domain.repository.MemberRepository;
import bonda.bonda.global.common.Message;
import bonda.bonda.global.common.SuccessResponse;
import bonda.bonda.global.exception.BusinessException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static bonda.bonda.global.exception.ErrorCode.*;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class ArticleCommandServiceImpl implements ArticleCommandService {
    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;
    private final ArticlecaseRepository articlecaseRepository;
    private final BadgeService badgeService;

    @Override
    @Transactional
    public SuccessResponse<ToggleBookmarkRes> toggleArticleBookmark(Member member, Long articleId) {
        // 멤버 조회 -> 영속 상태로 조회
        Member persistMember = memberRepository.findByKakaoId(member.getKakaoId()).orElseThrow(() -> new BusinessException(INVALID_MEMBER));
        // 아티클 조회
        Article article = articleRepository.findById(articleId).orElseThrow(() -> new BusinessException(INVALID_ARTICLE_ID));
        // 저장한 아티클 조회 있으면 삭제 없으면 생성
        Optional<Articlecase> existing = articlecaseRepository.findByMemberAndArticle(persistMember, article);
        //존재하는 경우 -> 삭제
        if (existing.isPresent()) {
            articlecaseRepository.delete(existing.get());
            return SuccessResponse.of(ToggleBookmarkRes.builder()
                    .articleId(articleId)
                    .isNewBadge(false)
                    .message("아티클 저장이 해제되었습니다.")
                    .build());
        }
        //존재하지 않는 경우
        // 저장한 아티클 생성
        Articlecase articlecase = Articlecase.builder().article(article).member(persistMember).build();
        // 저장
        articlecaseRepository.save(articlecase);
        return SuccessResponse.of(ToggleBookmarkRes.builder()
                .articleId(articleId)
                .isNewBadge(badgeService.checkAndAwardBadges(persistMember, ProgressType.ARTICLE_SAVE))
                .message("아티클 저장이 완료되었습니다.")
                .build());

    }


}
