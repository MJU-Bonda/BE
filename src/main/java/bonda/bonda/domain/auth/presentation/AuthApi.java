package bonda.bonda.domain.auth.presentation;

import bonda.bonda.domain.auth.dto.request.LoginReq;
import bonda.bonda.domain.auth.dto.request.LogoutReq;
import bonda.bonda.domain.auth.dto.request.ReissueReq;
import bonda.bonda.domain.auth.dto.response.LoginRes;
import bonda.bonda.domain.auth.dto.response.ReissueRes;
import bonda.bonda.domain.member.domain.Member;
import bonda.bonda.global.annotation.LoginMember;
import bonda.bonda.global.common.Message;
import bonda.bonda.global.common.SuccessResponse;
import bonda.bonda.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Auth API", description = "인증 관련 API입니다.")
public interface AuthApi {

    @Operation(summary = "회원가입 또는 로그인", description = "회원가입 또는 로그인을 합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "회원가입 혹은 로그인 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = LoginRes.class))}
            ),
            @ApiResponse(
                    responseCode = "401", description = "회원가입 혹은 로그인 실패",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class))}
            )
    })
    @PostMapping("/login")
    ResponseEntity<SuccessResponse<LoginRes>> signUpOrLogin(
            @Parameter(description = "회원가입 및 로그인 시 필요한 Request 입니다.", required = true) @Valid @RequestBody LoginReq loginReq
    );

    @Operation(summary = "AccessToken 재발급", description = "RefreshToken을 이용해 AccessToken을 재발급합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "토큰 재발급 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ReissueRes.class))}
            ),
            @ApiResponse(
                    responseCode = "401", description = "토큰 재발급 실패",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class))}
            )
    })
    @PostMapping("/reissue")
    ResponseEntity<SuccessResponse<ReissueRes>> reissueToken(
            @Parameter(description = "RefreshToken을 활용한 AccessToken 재발급에 필요한 Request 입니다.", required = true) @Valid @RequestBody ReissueReq reissueReq);

    @DeleteMapping("/logout")
    ResponseEntity<SuccessResponse<Message>> logout(
            @Parameter(description = "로그아웃을 하려는 회원의 AccessToken을 입력하세요.", required = true) @LoginMember Member member,
            @Parameter(description = "로그아웃 시 필요한 Request 입니다.", required = true) @Valid @RequestBody LogoutReq logoutReq);
}
