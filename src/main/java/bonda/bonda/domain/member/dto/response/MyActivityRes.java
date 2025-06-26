package bonda.bonda.domain.member.dto.response;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MyActivityRes {

    @Schema(type = "Integer", example = "1", description = "도서 상세페이지 조회한 횟수입니다.")
    private Integer bookViewCount;

    @Schema(type = "Integer", example = "1", description = "저장한 도서 갯수입니다.")
    private Integer bookcaseCount;

    @ArraySchema(schema = @Schema(implementation =  CategoryCountDTO.class))
    @Schema(description = "저장된 도서가 많은 상위 3개 카테고리 및 기타로 나타낸 카테고리별 도서 수 리스트입니다.")
    private List<CategoryCountDTO> categoryCountList;
}
