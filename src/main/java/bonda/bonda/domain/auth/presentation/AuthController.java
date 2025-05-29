package bonda.bonda.domain.auth.presentation;

import bonda.bonda.domain.auth.application.AuthService;
import bonda.bonda.domain.auth.dto.request.LoginReq;
import bonda.bonda.domain.auth.dto.response.LoginRes;
import bonda.bonda.global.common.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController implements AuthApi {

    private final AuthService authService;

    @Override
    @PostMapping("/login")
    public ResponseEntity<SuccessResponse<LoginRes>> signUpOrLogin(@Valid @RequestBody LoginReq loginReq) {
        return ResponseEntity.ok(authService.signUpOrLogin(loginReq));
    }
}
