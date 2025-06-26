package bonda.bonda.domain.article.application;

import bonda.bonda.domain.article.domain.Article;
import bonda.bonda.domain.article.domain.ArticleCategory;
import bonda.bonda.domain.article.domain.repository.ArticleRepository;
import bonda.bonda.domain.article.dto.response.*;
import bonda.bonda.domain.book.domain.Book;
import bonda.bonda.domain.book.dto.response.BookListRes;
import bonda.bonda.domain.book.dto.response.RecentViewBookListRes;
import bonda.bonda.domain.member.domain.Member;
import bonda.bonda.global.common.SuccessResponse;
import bonda.bonda.global.exception.BusinessException;
import bonda.bonda.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static bonda.bonda.domain.article.dto.ArticleMapper.convertToSimpleArticleListRes;
import static bonda.bonda.domain.book.dto.BookMapper.convertToBookListRes;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ArticleReadServiceImpl implements ArticleReadService {
    private final ArticleRepository articleRepository;

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

}
