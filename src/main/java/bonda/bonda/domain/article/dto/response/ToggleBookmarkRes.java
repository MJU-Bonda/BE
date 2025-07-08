package bonda.bonda.domain.article.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "아티클 저장 응답 DTO")
public class ToggleBookmarkRes {
    @Schema(description = "아티클 ID", example = "123")
    Long articleId;

    @Schema(description = "응답 메시지", example = "아티클이 성공적으로 저장되었습니다.")
    String message;

    @Schema(description = "아티클 저장으로 인한 신규 배지 생성 여부", example = "true")
    Boolean isNewBadge;
}
