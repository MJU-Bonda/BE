package bonda.bonda.domain.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryCountDTO {

    @Schema(type = "String", example = "NOVEL", description = "도서의 카테고리입니다. 총 8종류가 있습니다.")
    private String category;

    @Schema(type = "Long", example = "0L", description = "해당 카테고리의 저장된 도서 수 입니다.")
    private Long count;
}
