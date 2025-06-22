package bonda.bonda.domain.article.presentation;

import bonda.bonda.domain.article.dto.response.ArticleListByCategoryRes;
import bonda.bonda.domain.article.dto.response.DeleteSaveArticleRes;
import bonda.bonda.domain.article.dto.response.SaveArticleRes;
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


    @Operation(summary = "아티클 저장 삭제", description = "저장한 아티클을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "아티클 삭제 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = DeleteSaveArticleRes.class))}
            ),
            @ApiResponse(
                    responseCode = "400", description = "아티클 저장 실패",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class))}
            )
    })
    @DeleteMapping("save/{articleId}")
    ResponseEntity<SuccessResponse<DeleteSaveArticleRes>> deleteSaveArticle(
            @Parameter(description = "아티클 아이디", example = "123")
            @PathVariable("articleId") Long articleId,
            @Parameter(hidden = true) // Swagger에 표시하지 않음 (내부에서 주입되는 로그인 사용자 정보)
            @LoginMember Member member);
}
