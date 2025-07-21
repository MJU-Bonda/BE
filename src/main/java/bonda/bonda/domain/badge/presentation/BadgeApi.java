package bonda.bonda.domain.badge.presentation;

import bonda.bonda.domain.auth.dto.response.LoginRes;
import bonda.bonda.domain.badge.dto.response.BadgeDetailsRes;
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
import org.springframework.web.bind.annotation.PathVariable;

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
            @Parameter(hidden = true) @LoginMember Member member);

    @Operation(summary = "회원 뱃지 상세 조회", description = "회원이 소유한 뱃지의 상세 내용을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "회원 뱃지 상세 조회 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = BadgeDetailsRes.class))}
            ),
            @ApiResponse(
                    responseCode = "401", description = "회원 뱃지 상세 조회 실패",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class))}
            )
    })
    @GetMapping("/{badgeId}")
    ResponseEntity<SuccessResponse<BadgeDetailsRes>> getBadgeDetails(
            @Parameter(hidden = true) @LoginMember Member memberr,
            @Parameter(description = "상세 조회하고 싶은 뱃지 아이디 번호를 입력하시오.", required = true) @PathVariable Long badgeId);
}
