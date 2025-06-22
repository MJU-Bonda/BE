package bonda.bonda.domain.article.application;

import bonda.bonda.domain.article.domain.ArticleCategory;
import bonda.bonda.domain.article.domain.repository.ArticleRepository;
import bonda.bonda.domain.article.dto.response.ArticleListByCategoryRes;
import bonda.bonda.domain.article.dto.response.SimpleArticleResWithBookmarked;
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
}
