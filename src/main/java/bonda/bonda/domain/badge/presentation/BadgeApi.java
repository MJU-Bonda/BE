package bonda.bonda.domain.badge.presentation;

import bonda.bonda.domain.auth.dto.response.LoginRes;
import bonda.bonda.domain.badge.dto.response.MyBadgeListRes;
import bonda.bonda.domain.member.domain.Member;
import bonda.bonda.global.annotation.LoginMember;
import bonda.bonda.global.common.SuccessResponse;
import bonda.bonda.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "Badge API", description = "뱃지 관련 API입니다.")
public interface BadgeApi {

    @Operation(summary = "회원 뱃지 목록 조회", description = "회원이 소유한 뱃지의 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "회원 뱃지 목록 조회 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = MyBadgeListRes.class))}
            ),
            @ApiResponse(
                    responseCode = "401", description = "회원 뱃지 목록 조회 실패",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class))}
            )
    })
    @GetMapping("/me")
    ResponseEntity<SuccessResponse<MyBadgeListRes>> getMyBadgeList(
            @Parameter(description = "소유한 뱃지 목록을 확인하고 싶은 멤버의 accessToken을 입력하시오.", required = true) @LoginMember Member member);
}
