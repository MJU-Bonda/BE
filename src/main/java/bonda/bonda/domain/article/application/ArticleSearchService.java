package bonda.bonda.domain.article.application;

import bonda.bonda.domain.article.dto.response.SearchArticleListRes;
import bonda.bonda.domain.member.domain.Member;
import bonda.bonda.global.common.SuccessResponse;

public interface ArticleSearchService {
    SuccessResponse<SearchArticleListRes> searchArticleList(int page, int size, String orderBy, String word, Member member);
}
