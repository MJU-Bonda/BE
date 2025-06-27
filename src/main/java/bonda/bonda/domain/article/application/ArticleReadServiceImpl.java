package bonda.bonda.domain.article.application;

import bonda.bonda.domain.article.domain.Article;
import bonda.bonda.domain.article.domain.ArticleCategory;
import bonda.bonda.domain.article.domain.repository.ArticleRepository;
import bonda.bonda.domain.article.dto.response.ArticleDetailRes;
import bonda.bonda.domain.article.dto.response.ArticleListByCategoryRes;
import bonda.bonda.domain.article.dto.response.MySavedArticleListRes;
import bonda.bonda.domain.article.dto.response.SimpleArticleRes;
import bonda.bonda.domain.article.dto.response.SimpleArticleResWithBookmarked;
import bonda.bonda.domain.badge.application.BadgeService;
import bonda.bonda.domain.badge.domain.ProgressType;
import bonda.bonda.domain.article.dto.response.*;
import bonda.bonda.domain.member.domain.Member;
import bonda.bonda.domain.member.domain.repository.MemberRepository;
import bonda.bonda.domain.recentviewarticle.RecentViewArticle;
import bonda.bonda.domain.recentviewarticle.repository.RecentViewArticleRepository;
import bonda.bonda.global.common.SuccessResponse;
import bonda.bonda.global.exception.BusinessException;
import bonda.bonda.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static bonda.bonda.domain.article.dto.ArticleMapper.convertToSimpleArticleListRes;
import static bonda.bonda.global.exception.ErrorCode.INVALID_ARTICLE_ID;
import static bonda.bonda.global.exception.ErrorCode.INVALID_MEMBER;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ArticleReadServiceImpl implements ArticleReadService {
    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;
    private final RecentViewArticleRepository recentViewArticleRepository;
    private final BadgeService badgeService;

    public SuccessResponse<ArticleListByCategoryRes> getArticleListByCategory(int page, int size, String category, Member member) {
        if (!ArticleCategory.isValid(category)) {
            throw new BusinessException("존재하지 않는 카테고리입니다.", ErrorCode.INVALID_ARTICLE_CATEGORY);
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<SimpleArticleResWithBookmarked> articleList=  articleRepository.findArticleListByCategory(pageable, category, member);
        return SuccessResponse.of(ArticleListByCategoryRes.builder()
                .category(category)
                .page(page)
                .hasNextPage(articleList.hasNext())
                .articleList(articleList.getContent()).build());
    }

    @Override
    public SuccessResponse<MySavedArticleListRes> getMySavedArticleList(int page, int size, String orderBy, Member member) {
        // 페이지 정보
        Pageable pageable = PageRequest.of(page, size);
        Page<Article> articleList = articleRepository.findMySavedArticleList(pageable, orderBy, member);
        List<SimpleArticleRes> articleListRes = convertToSimpleArticleListRes(articleList.getContent());

        return SuccessResponse.of(MySavedArticleListRes.builder()
                .page(page)
                .total(articleList.getTotalElements())
                .orderBy(orderBy)
                .hasNextPage(articleList.hasNext())
                .articleList(articleListRes)
                .build());
    }

    @Override
    public SuccessResponse<RecentViewArticleListRes> getRecentViewArticleList(int page, int size, Member member) {
        // 페이지 정보 생성
        Pageable pageable = PageRequest.of(page, size);
        // 최근 조회 아티클 리스트 가져오기
        Page<Article> recentViewArticleList = articleRepository.findRecentViewArticleList(member, pageable);
        // 아티클 리스트 dto로 변환
        List<SimpleArticleRes> simpleArticleRes = convertToSimpleArticleListRes(recentViewArticleList.getContent());
        return SuccessResponse.of(RecentViewArticleListRes.builder()
                .page(page)
                .hasNextPage(recentViewArticleList.hasNext())
                .articleList(simpleArticleRes)
                .build());
    }


    @Override
    @Transactional
    public SuccessResponse<ArticleDetailRes> getArticleDetail(Long articleId, Member member) {
        // 멤버 조회 -> 영속 상태로 조회
        Member persistMember = memberRepository.findByKakaoId(member.getKakaoId()).orElseThrow(() -> new BusinessException(INVALID_MEMBER));
        //아티클 조회 -> 없으면 오류
        Article article = articleRepository.findById(articleId).orElseThrow(() -> new BusinessException(INVALID_ARTICLE_ID));
        // 아티클 정보 조회
        ArticleDetailRes articleDetailRes = articleRepository.getArticleDetail(article, persistMember); //응답에 기본 정보 체우기
        // 최근 조회 여부 조회 -> 없으면 새로 생성
        RecentViewArticle recentViewArticle = recentViewArticleRepository.findByMemberAndArticle(persistMember, article).orElse(null);
        if (recentViewArticle == null) { // 첫 조회인 경우
            recentViewArticleRepository.save(new RecentViewArticle(persistMember, article)); // 조회 아티클 생성
            articleDetailRes = articleDetailRes.toBuilder()
                    .isNewBadge(badgeService.checkAndAwardBadges(persistMember, ProgressType.ARTICLE_VIEW)) // 현재 조회로 뱃지 여부 확인
                    .build();
        } else {
            recentViewArticle.updateViewDate(LocalDateTime.now());
        }
        return SuccessResponse.of(articleDetailRes);
    }
}
