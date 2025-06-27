package bonda.bonda.domain.auth.presentation;

import bonda.bonda.domain.auth.application.AuthService;
import bonda.bonda.domain.auth.dto.request.LoginReq;
import bonda.bonda.domain.auth.dto.request.LogoutReq;
import bonda.bonda.domain.auth.dto.request.ReissueReq;
import bonda.bonda.domain.auth.dto.response.LoginRes;
import bonda.bonda.domain.auth.dto.response.ReissueRes;
import bonda.bonda.domain.member.domain.Member;
import bonda.bonda.global.annotation.LoginMember;
import bonda.bonda.global.common.Message;
import bonda.bonda.global.common.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @Override
    @PostMapping("/reissue")
    public ResponseEntity<SuccessResponse<ReissueRes>> reissueToken(@Valid @RequestBody ReissueReq reissueReq) {
        String refreshToken = reissueReq.getRefreshToken();
        return ResponseEntity.ok(authService.reissue(refreshToken));
    }

    @Override
    @DeleteMapping("/logout")
    public ResponseEntity<SuccessResponse<Message>> logout(@LoginMember Member member, @Valid @RequestBody LogoutReq logoutReq) {
        Long memberId = member.getId();
        return ResponseEntity.ok(authService.logout(memberId, logoutReq));
    }
}
