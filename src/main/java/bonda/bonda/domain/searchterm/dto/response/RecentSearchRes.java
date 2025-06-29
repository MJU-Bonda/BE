package bonda.bonda.domain.searchterm.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RecentSearchRes {

    @Schema(type = "array",
            example = "[\"최근 검색어 1\",\"최근 검색어 2\",\"최근 검색어 3\",\"최근 검색어 4\",\"최근 검색어 5\"]",
            description = "회원의 최근 검색어 목록 리스트입니다. 최대 5개까지 저장됩니다.")
    private List<String> recentSearchTermList;

    @Schema(type = "Boolean", example = "true", description = "회원의 검색어 자동 저장 허용 여부입니다. 자동 저장 허용 시 true를 출력합니다.")
    private Boolean autoSave;
}
