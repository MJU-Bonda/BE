package bonda.bonda.domain.article.dto.response;

import bonda.bonda.global.common.Message;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "저장한 아티클 삭제 응답 DTO")
public class DeleteSaveArticleRes {
    @Schema(description = "아티클 ID", example = "123")
    Long articleId;
//    @Schema(description = "응답 메시지", example = "아티클이 성공적으로 삭제되었습니다.")
    Message message;
}
