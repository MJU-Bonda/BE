package bonda.bonda.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LogoutReq {

    @Schema(type = "String", example = "jwtAccessToken", description = "서버로부터 받은 AccessToken을 입력하세요.")
    @NotNull
    private String accessToken;

    @Schema(type = "String", example = "jwtRefreshToken", description = "서버로부터 받은 RefreshToken을 입력하세요.")
    @NotNull
    private String refreshToken;
}
