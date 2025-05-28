package bonda.bonda.domain.auth.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginReq {

    @NotNull
    private String idToken;
}
