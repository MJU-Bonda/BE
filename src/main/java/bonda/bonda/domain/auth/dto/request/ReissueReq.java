package bonda.bonda.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReissueReq {

    @Schema(type = "String", example = "jwtRefreshToken", description = "서버로부터 받은 RefreshToken을 입력하세요.")
    @NotNull
    private String refreshToken;
}
