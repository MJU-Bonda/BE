package bonda.bonda.domain.auth.dto.request;

import lombok.Data;

@Data
public class ReissueReq {

    private String refreshToken;
}
