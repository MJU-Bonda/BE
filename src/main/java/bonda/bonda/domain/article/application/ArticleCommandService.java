package bonda.bonda.domain.article.application;


import bonda.bonda.domain.article.dto.response.DeleteSaveArticleRes;
import bonda.bonda.domain.article.dto.response.SaveArticleRes;
import bonda.bonda.domain.member.domain.Member;
import bonda.bonda.global.common.SuccessResponse;

public interface ArticleCommandService {
    SuccessResponse<SaveArticleRes> saveArticle(Member member, Long articleId);

    SuccessResponse<DeleteSaveArticleRes> deleteSaveArticle(Member member, Long articleId);
}
