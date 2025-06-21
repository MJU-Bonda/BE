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
    @Schema(description = "아티클 ID", example = "1")
    Long articleId;
    @Schema(description = "카테고리명", example = "POEM")
    String category;
    @Schema(description = "아티클 제목", example = "너의 이름은")
    String title;
    @Schema(description = "아티클 소개 문구", example = "사랑과 이별에 대한 짧은 이야기")
    String introduction;
    @Schema(description = "아티클 이미지 URL", example = "https://cdn.example.com/images/article1.jpg")
    String imageUrl;
    @Schema(description = "사용자가 북마크했는지 여부", example = "true")
    Boolean isBookmarked;
}
