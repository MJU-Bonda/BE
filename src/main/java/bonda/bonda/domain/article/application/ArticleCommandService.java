package bonda.bonda.domain.article.application;


import bonda.bonda.domain.article.dto.response.ToggleBookmarkRes;
import bonda.bonda.domain.member.domain.Member;
import bonda.bonda.global.common.SuccessResponse;

public interface ArticleCommandService {
    SuccessResponse<ToggleBookmarkRes> toggleArticleBookmark(Member member, Long articleId);

}
