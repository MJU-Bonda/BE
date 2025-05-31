package bonda.bonda.domain.book.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
@Data
public class BookSaveReq {
    @Schema(type = "String", example = "NOVEL", description = "DB에 적제될 본다 카테고리 입니다.")
    @NotNull
    String category;
    @Schema(type = "Long", example = "50930", description = "알라딘 api에 요청할 카테고리 ID 입니다.")
    @NotNull
    Long categoryId;
}
