package bonda.bonda.domain.article.presentation;

import bonda.bonda.domain.article.dto.response.*;
import bonda.bonda.domain.book.dto.response.BookDetailRes;
import bonda.bonda.domain.book.dto.response.RecentViewBookListRes;
import bonda.bonda.domain.book.dto.response.SearchBookListRes;
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
import org.springframework.web.bind.annotation.*;

@Tag(name = "Article API", description = "아티클 관련 API입니다.")
public interface ArticleApi {

    @Operation(summary = "아티클 홈 화면 (카테고리별 조회)", description = "카테고리별로 아티클을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "카테고리별 아티클 조회 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ArticleListByCategoryRes.class))}
            ),
            @ApiResponse(
                    responseCode = "400", description = "카테고리별 아티클 조회 실패",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class))}
            )
    })
    @GetMapping("{articleCategory}")
    ResponseEntity<SuccessResponse<ArticleListByCategoryRes>> getArticleListByCategory(
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지당 항목 수", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "카테고리명 (예: ALL, AUTHOR_OR_PUBLISHER, BOOKSTORE, THEME)", example = "ALL")
            @PathVariable(value = "articleCategory") String category,
            @Parameter(hidden = true) // Swagger에 표시하지 않음 (내부에서 주입되는 로그인 사용자 정보)
            @LoginMember Member member);


    @Operation(summary = "아티클 저장", description = "아티클을 저장합니다..")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "아티클 저장 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = SaveArticleRes.class))}
            ),
            @ApiResponse(
                    responseCode = "400", description = "아티클 저장 실패",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class))}
            )
    })
    @PostMapping("save/{articleId}")
    ResponseEntity<SuccessResponse<SaveArticleRes>> saveArticle(
            @Parameter(description = "아티클 아이디", example = "123")
            @PathVariable("articleId") Long articleId,
            @Parameter(hidden = true) // Swagger에 표시하지 않음 (내부에서 주입되는 로그인 사용자 정보)
            @LoginMember Member member);





    @Operation(summary = "아티클 검색", description = "키워드로 아티클을 검색합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "아티클 검색 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = SearchArticleListRes.class))}
            ),
            @ApiResponse(
                    responseCode = "401", description = "아티클 검색 실패",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class))}
            )
    })
    @GetMapping("search")
    ResponseEntity<SuccessResponse<SearchArticleListRes>> searchArticleList(
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "페이지당 항목 수", example = "24")
            @RequestParam(defaultValue = "24") int size,

            @Parameter(description = "정렬 기준 (예: newest, popularity)", example = "newest")
            @RequestParam(defaultValue = "newest") String orderBy,

            @Parameter(description = "검색 키워드", example = "자바")
            @RequestParam String word,

            @Parameter(hidden = true) // Swagger에 표시하지 않음 (내부에서 주입되는 로그인 사용자 정보)
            @LoginMember Member member);



    @Operation(summary = "저장한 아티클 검색", description = "멤버가 저장한 아티클 리스트를 검색합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "아티클 조회 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = MySavedArticleListRes.class))}
            ),
            @ApiResponse(
                    responseCode = "401", description = "아티클 검색 실패",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class))}
            )
    })
    @GetMapping("/my-save")
    ResponseEntity<SuccessResponse<MySavedArticleListRes>> getMySavedArticleListRes(
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지당 항목 수", example = "24")
            @RequestParam(defaultValue = "24") int size,
            @Parameter(description = "정렬 기준 (예: recentlySaved, title)", example = "recentlySaved")
            @RequestParam(defaultValue = "recentlySaved") String orderBy,
            @Parameter(hidden = true) // Swagger에 표시하지 않음 (내부에서 주입되는 로그인 사용자 정보)
            @LoginMember Member member);

    @Operation(summary = "아티클 상세 조회", description = "아티클을 상세 조회합니다.. (최근 조회한 아티클 생성 + 뱃지 생성 처리)")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "아티클 조회 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ArticleDetailRes.class))}
            ),
            @ApiResponse(
                    responseCode = "401", description = "인증 실패",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class))}
            )
    })
    @GetMapping("{articleId}")
    ResponseEntity<SuccessResponse<ArticleDetailRes>> getArticleDetail(
            @Parameter(description = "아티클 ID", example = "21")
            @PathVariable("articleId") Long articleId,
            @Parameter(hidden = true) // Swagger에 표시하지 않음 (내부에서 주입되는 로그인 사용자 정보)
            @LoginMember Member member);


    @Operation(summary = "최근 조회한 아티클 목록 조회", description = "최근 조회한 아티클 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "아티클 조회 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = RecentViewArticleListRes.class))}
            ),
            @ApiResponse(
                    responseCode = "401", description = "인증 실패",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class))}
            )
    })
    @GetMapping("/my-recent-views")
    ResponseEntity<SuccessResponse<RecentViewArticleListRes>> getRecentViewArticleList(
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지당 항목 수", example = "24")
            @RequestParam(defaultValue = "24") int size,
            @Parameter(hidden = true) // Swagger에 표시하지 않음 (내부에서 주입되는 로그인 사용자 정보)
            @LoginMember Member member);

}
