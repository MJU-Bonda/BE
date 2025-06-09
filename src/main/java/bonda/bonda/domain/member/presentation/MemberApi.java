package bonda.bonda.domain.member.presentation;

import bonda.bonda.domain.auth.dto.response.LoginRes;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Member API", description = "회원 관련 API입니다.")
public interface MemberApi {

    @Operation(summary = "회원의 닉네임 또는 프로필 이미지 변경", description = "회원의 닉네임이나 프로필 이미지를 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "회원 프로필 변경 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}
            ),
            @ApiResponse(
                    responseCode = "401", description = "회원 프로필 변경 실패",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class))}
            )
    })
    @PutMapping(value = "/update/nickname-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<SuccessResponse<Message>> updateNicknameAndProfileImage(
            @Parameter(description = "닉네임이나 회원 이미지를 변경하고 싶은 회원의 AccessToken을 입력하세요.", required = true) @LoginMember Member member,
            @Parameter(description = "변경할 닉네임을 입력하세요.") @RequestPart(required = false) String nickname,
            @Parameter(description = "변경할 프로필 이미지 파일을 입력하세요.") @RequestPart(required = false) MultipartFile profileImage);
}
