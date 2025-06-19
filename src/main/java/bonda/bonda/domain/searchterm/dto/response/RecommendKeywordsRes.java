package bonda.bonda.domain.searchterm.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RecommendKeywordsRes {

    @Schema(type = "array",
            example = "[\"여행\", \"미니멀리즘\", \"감정\"]",
            description = "서비스에 제공하는 추천 키워드입니다. 3개가 출력되며 하루마다 갱신됩니다.")
    private List<String> recommendKeywords;
}
