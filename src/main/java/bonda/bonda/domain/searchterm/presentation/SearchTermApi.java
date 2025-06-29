package bonda.bonda.domain.searchterm.presentation;

import bonda.bonda.domain.member.domain.Member;
import bonda.bonda.domain.searchterm.dto.response.ModifyAutoSave;
import bonda.bonda.domain.searchterm.dto.response.RecentSearchRes;
import bonda.bonda.domain.searchterm.dto.response.RecommendKeywordsRes;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @Operation(summary = "최근 검색어 개별 삭제", description = "회원의 최근 검색어를 개별로 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "최근 검색어 개별 삭제 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}
            ),
            @ApiResponse(
                    responseCode = "401", description = "최근 검색어 개별 삭제 실패",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class))}
            )
    })
    @DeleteMapping("/recent")
    ResponseEntity<SuccessResponse<Message>> deleteSearchTerm(
            @Parameter(description = "최근 검색어를 삭제하고 싶은 회원의 AccessToken을 입력하세요.", required = true) @LoginMember Member member,
            @Parameter(description = "삭제할 최근 검색어를 parameter 값으로 입력하세요.") @RequestParam String keyword);

    @Operation(summary = "최근 검색어 전체 삭제", description = "회원의 최근 검색어 전체를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "최근 검색어 전체 삭제 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}
            ),
            @ApiResponse(
                    responseCode = "401", description = "최근 검색어 전체 삭제 실패",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class))}
            )
    })
    @DeleteMapping("/recent/all")
    ResponseEntity<SuccessResponse<Message>> deleteAllSearchTerm(
            @Parameter(description = "최근 검색어를 삭제하고 싶은 회원의 AccessToken을 입력하세요.", required = true) @LoginMember Member member);

    @Operation(summary = "추천 키워드 목록 조회", description = "서비스에서 제공하는 추천 키워드 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "추천 키워드 목록 조회 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = RecommendKeywordsRes.class))}
            ),
            @ApiResponse(
                    responseCode = "404", description = "추천 키워드 목록 조회 실패",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class))}
            )
    })
    @GetMapping("/recommend")
    ResponseEntity<SuccessResponse<RecommendKeywordsRes>> getRecommendKeywords();

    @Operation(summary = "검색어 자동 저장 여부 변경", description = "회원의 최근 검색어 자동 저장 여부를 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "검색어 자동 저장 여부 변경 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}
            ),
            @ApiResponse(
                    responseCode = "401", description = "검색어 자동 저장 여부 변경 실패",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class))}
            )
    })
    @PatchMapping("/auto-save")
    ResponseEntity<SuccessResponse<ModifyAutoSave>> modifyAutoSave(
            @Parameter(description = "검색어 자동 저장 여부를 변경하고 싶은 회원의 AccessToken을 입력하세요.", required = true) @LoginMember Member member);
}
