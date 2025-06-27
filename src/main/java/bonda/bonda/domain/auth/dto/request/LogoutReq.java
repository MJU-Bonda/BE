package bonda.bonda.domain.auth.dto.request;

import lombok.Data;

@Data
public class LogoutReq {

    private String accessToken;

    private String refreshToken;
}
