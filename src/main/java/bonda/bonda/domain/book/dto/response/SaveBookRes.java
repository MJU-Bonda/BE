package bonda.bonda.domain.book.dto.response;

import bonda.bonda.global.common.Message;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "도서 저장 응답 DTO")
public class SaveBookRes {
    @Schema(description = "도서 ID", example = "123")
    Long bookId;

    @Schema(description = "응답 메시지", example = "도서가 성공적으로 저장되었습니다.")
    Message message;

    @Schema(description = "도서 저장으로 인한 신규 배지 생성 여부", example = "true")
    Boolean isNewBadge;
}
