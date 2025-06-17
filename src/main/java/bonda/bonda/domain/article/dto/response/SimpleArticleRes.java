package bonda.bonda.domain.article.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SimpleArticleRes {
    Long articleId;
    String title;
    String articleCategory;
    String imageUrl;
}
