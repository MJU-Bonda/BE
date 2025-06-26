package bonda.bonda.domain.article.application;

import bonda.bonda.domain.article.dto.response.ArticleDetailRes;
import bonda.bonda.domain.article.dto.response.ArticleListByCategoryRes;
import bonda.bonda.domain.article.dto.response.MySavedArticleListRes;
import bonda.bonda.domain.member.domain.Member;
import bonda.bonda.global.common.SuccessResponse;

public interface ArticleReadService {
    SuccessResponse<ArticleListByCategoryRes> getArticleListByCategory(int page, int size, String category, Member member);

    SuccessResponse<MySavedArticleListRes> getMySavedArticleList(int page, int size, String orderBy, Member member);

    SuccessResponse<ArticleDetailRes> getArticleDetail(Long articleId, Member member);
}
