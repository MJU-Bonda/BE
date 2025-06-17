package bonda.bonda.domain.searchterm.presentation;

import bonda.bonda.domain.member.domain.Member;
import bonda.bonda.domain.searchterm.dto.response.RecentSearchRes;
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

@Tag(name = "SearchTerm API", description = "검색어 관련 API입니다.")
public interface SearchTermApi {

    @Operation(summary = "최근 검색어 목록 조회", description = "회원의 최근 검색어 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "최근 검색어 목록 조회 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = RecentSearchRes.class))}
            ),
            @ApiResponse(
                    responseCode = "401", description = "최근 검색어 목록 조회 실패",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class))}
            )
    })
    @GetMapping("/recent")
    ResponseEntity<SuccessResponse<RecentSearchRes>> getRecentSearchTerms(
            @Parameter(description = "최근 검색어 목록을 조회하고 싶은 회원의 AccessToken을 입력하세요.", required = true) @LoginMember Member member);
}
