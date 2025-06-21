package bonda.bonda.domain.article.application;

import bonda.bonda.domain.article.dto.response.ArticleListByCategoryRes;
import bonda.bonda.domain.member.domain.Member;
import bonda.bonda.global.common.SuccessResponse;

public interface ArticleReadService {
    SuccessResponse<ArticleListByCategoryRes> getArticleListByCategory(int page, int size, String category, Member member);
}
