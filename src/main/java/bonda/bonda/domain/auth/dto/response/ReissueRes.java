package bonda.bonda.domain.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReissueRes {

    @Schema(type = "String", example = "jwtAccessToken", description = "새로 발급받은 AccessToken을 출력합니다.")
    private String accessToken;
}
