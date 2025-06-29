package bonda.bonda.domain.searchterm.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ModifyAutoSave {

    @Schema(type = "Boolean", example = "true", description = "회원의 검색어 자동 저장 허용 여부입니다. 자동 저장 허용 시 true를 출력합니다.")
    private Boolean autoSave;
}
