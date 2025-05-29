package bonda.bonda.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginReq {

    @Schema(type = "String", example = "kakao.id.Token", description = "프론트로부터 받은 kakao의 idToken 입니다.")
    @NotNull
    private String idToken;
}
