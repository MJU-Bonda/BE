package bonda.bonda.domain.auth.presentation;

import bonda.bonda.domain.auth.application.AuthService;
import bonda.bonda.domain.auth.dto.request.LoginReq;
import bonda.bonda.domain.auth.dto.response.LoginRes;
import bonda.bonda.global.common.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<SuccessResponse<LoginRes>> signUpOrLogin(@RequestBody LoginReq loginReq) {
        return ResponseEntity.ok(authService.signUpOrLogin(loginReq));
    }
}
