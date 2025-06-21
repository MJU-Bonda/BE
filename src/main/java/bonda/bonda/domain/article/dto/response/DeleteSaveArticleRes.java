package bonda.bonda.domain.article.dto.response;

import bonda.bonda.global.common.Message;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeleteSaveArticleRes {
    Long articleId;
    Message message;
}
