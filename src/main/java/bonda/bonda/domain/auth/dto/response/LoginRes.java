package bonda.bonda.domain.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRes {

    @Schema(type = "String", example = "jwtAccessToken", description = "AccessToken을 출력합니다.")
    private String accessToken;

    @Schema(type = "String", example = "jwtRefreshToken", description = "RefreshToken을 출력합니다.")
    private String refreshToken;

    @Schema(type = "Boolean", example = "true", description = "신규 멤버인지 확인합니다. true면 회원가입 관련으로 false면 로그인 관련으로 이어집니다.")
    private Boolean isNewUser;
}
