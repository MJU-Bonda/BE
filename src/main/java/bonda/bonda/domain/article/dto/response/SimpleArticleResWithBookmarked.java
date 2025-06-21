package bonda.bonda.domain.article.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@AllArgsConstructor
@Schema(description = "아티클 카드 응답 DTO")
public class SimpleArticleResWithBookmarked {
    Long articleId;
    String category;
    String title;
    String introduction;
    String imageUrl;
    Boolean isBookmarked;
}
