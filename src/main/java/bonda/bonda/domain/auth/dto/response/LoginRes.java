package bonda.bonda.domain.auth.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRes {

    private String accessToken;

    private String refreshToken;

    private Boolean isNewUser;
}
