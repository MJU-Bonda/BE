package bonda.bonda.domain.article.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@Schema(description = "아티클 응답 DTO (북마크 X)")
public class SimpleArticleRes {
    @Schema(description = "아티클 ID", example = "1")
    Long articleId;

    @Schema(description = "아티클 제목", example = "효과적인 학습 전략")
    String title;

    @Schema(description = "아티클 카테고리", example = "교육")
    String articleCategory;

    @Schema(description = "아티클 이미지 URL", example = "https://example.com/article.jpg")
    String imageUrl;
}
