package bonda.bonda.domain.book.presentation;

import bonda.bonda.domain.book.dto.request.SaveBookFromAladinReq;
import bonda.bonda.domain.book.dto.response.*;
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
import org.springframework.web.bind.annotation.*;

@Tag(name = "Book API", description = "도서 관련 API입니다.")
public interface BookApi {

    @Operation(summary = "도서 홈 화면 (카테고리별 조회)", description = "카테고리별로 도서를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "카테고리별 도서 조회 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = BookListByCategoryRes.class))}
            ),
            @ApiResponse(
                    responseCode = "401", description = "카테고리별 도서 조회 실패",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class))}
            )
    })
    @GetMapping()
    ResponseEntity<SuccessResponse<BookListByCategoryRes>> getBookListByCategory(
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지당 항목 수", example = "24")
            @RequestParam(defaultValue = "24") int size,
            @Parameter(description = "정렬 기준 (예: popularity, recent)", example = "popularity")
            @RequestParam(defaultValue = "popularity") String orderBy,
            @Parameter(description = "카테고리명 (예: ALL, POEM, NOVEL 등)", example = "ALL")
            @RequestParam(defaultValue = "ALL") String category);



    @Operation(
            summary = "도서 DB 저장 (알라딘 api)",
            description = "알라딘 API를 호출하여, 새로운 도서를 등록합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201", description = "도서 등록 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}
            ),
            @ApiResponse(
                    responseCode = "400", description = "잘못된 요청",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class))}
            )
    })
    @PostMapping("/new")
    ResponseEntity<SuccessResponse<Message>> saveBookFromAladin(@Valid @RequestBody SaveBookFromAladinReq postReq);



    @Operation(summary = "도서 검색", description = "키워드로 도서를 검색합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "도서 검색 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = SearchBookListRes.class))}
            ),
            @ApiResponse(
                    responseCode = "401", description = "도서 검색 실패",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class))}
            )
    })
    @GetMapping("/search")
    ResponseEntity<SuccessResponse<SearchBookListRes>> searchBookList(
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "페이지당 항목 수", example = "24")
            @RequestParam(defaultValue = "24") int size,

            @Parameter(description = "정렬 기준 (예: newest, relevance)", example = "newest")
            @RequestParam(defaultValue = "newest") String orderBy,

            @Parameter(description = "검색 키워드", example = "자바")
            @RequestParam String word,

            @Parameter(hidden = true) // Swagger에 표시하지 않음 (내부에서 주입되는 로그인 사용자 정보)
            @LoginMember Member member);



    @Operation(summary = "도서 저장", description = "사용자가 특정 도서를 저장합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "도서 저장 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = SaveBookRes.class))}
            ),
            @ApiResponse(
                    responseCode = "400", description = "잘못된 요청 (이미 저장된 도서 등)",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class))}
            ),
            @ApiResponse(
                    responseCode = "401", description = "인증 실패",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class))}
            )
    })
    @PostMapping("/save/{bookId}")
    ResponseEntity<SuccessResponse<SaveBookRes>> saveBook(
            @Parameter(description = "도서 ID", example = "123")
            @PathVariable(value = "bookId") Long bookId,
            @Parameter(hidden = true) // Swagger에 표시하지 않음 (내부에서 주입되는 로그인 사용자 정보)
            @LoginMember Member member);



    @Operation(summary = "도서 저장 삭제", description = "사용자가 특정 도서를 저장 목록에서 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "도서 저장 삭제 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = DeleteSaveBookRes.class))}
            ),
            @ApiResponse(
                    responseCode = "400", description = "잘못된 요청 (이미 저장된 도서 등)",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class))}
            ),
            @ApiResponse(
                    responseCode = "401", description = "인증 실패",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class))}
            )
    })
    @DeleteMapping("/save/{bookId}")
    ResponseEntity<SuccessResponse<DeleteSaveBookRes>> deleteBook(
            @Parameter(description = "도서 ID", example = "123")
            @PathVariable(value = "bookId") Long bookId,
            @Parameter(hidden = true) // Swagger에 표시하지 않음 (내부에서 주입되는 로그인 사용자 정보)
            @LoginMember Member member);



    @Operation(summary = "사랑받는 도서 조회", description = "홈 화면의 요즘 가장 사랑 받는 도서의 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "도서 조회 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = LovedBookListRes.class))}
            ),
            @ApiResponse(
                    responseCode = "400", description = "잘못된 요청 (잘못된 주제)",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class))}
            ),
            @ApiResponse(
                    responseCode = "401", description = "인증 실패",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class))}
            )
    })
    @GetMapping("/liked")
    ResponseEntity<SuccessResponse<LovedBookListRes>> getLovedBookList(@RequestParam(defaultValue = "ALL") String subject);



    @Operation(summary = "저장한 도서 목록 조회", description = "저장한 도서 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "도서 조회 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = SaveBookRes.class))}
            ),
            @ApiResponse(
                    responseCode = "401", description = "인증 실패",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class))}
            )
    })
    @GetMapping("/my-save")
    ResponseEntity<SuccessResponse<MySavedBookListRes>> getMySavedBookListRes(
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지당 항목 수", example = "24")
            @RequestParam(defaultValue = "24") int size,
            @Parameter(description = "정렬 기준 (예: recentlysaved, title)", example = "recentlysaved")
            @RequestParam(defaultValue = "recentlysaved") String orderBy,
            @Parameter(hidden = true) // Swagger에 표시하지 않음 (내부에서 주입되는 로그인 사용자 정보)
            @LoginMember Member member);





    @Operation(summary = "도서 상세 조회", description = "저장한 도서 목록을 조회합니다. (최근 조회한 도서 생성 + 뱃지 생성 처리)")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "도서 조회 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = BookDetailRes.class))}
            ),
            @ApiResponse(
                    responseCode = "401", description = "인증 실패",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class))}
            )
    })
    @GetMapping("/{bookId}")
    ResponseEntity<SuccessResponse<BookDetailRes>> getBookDetail(
            @Parameter(description = "도서 ID", example = "123")
            @PathVariable(value = "bookId") Long bookId,
            @Parameter(hidden = true) // Swagger에 표시하지 않음 (내부에서 주입되는 로그인 사용자 정보)
            @LoginMember Member member);


    @Operation(summary = "최근 조회한 도서 목록 조회", description = "최근 조회한 도서 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "도서 조회 성공",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = RecentViewBookListRes.class))}
            ),
            @ApiResponse(
                    responseCode = "401", description = "인증 실패",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class))}
            )
    })
    @GetMapping("/my-recent-views")
    ResponseEntity<SuccessResponse<RecentViewBookListRes>> getRecentViewBookList(
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지당 항목 수", example = "24")
            @RequestParam(defaultValue = "24") int size,
            @Parameter(hidden = true) // Swagger에 표시하지 않음 (내부에서 주입되는 로그인 사용자 정보)
            @LoginMember Member member);
}

