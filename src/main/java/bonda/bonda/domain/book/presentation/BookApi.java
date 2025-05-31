package bonda.bonda.domain.book.presentation;

import bonda.bonda.domain.book.dto.request.BookSaveReq;
import bonda.bonda.domain.book.dto.response.BookListByCategoryRes;
import bonda.bonda.domain.book.dto.response.SearchBookListRes;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Book API", description = "도서 관련 API입니다.")
public interface BookApi {

    @Operation(summary = "도서 홈 화면", description = "카테고리별로 도서를 조회합니다.")
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
    public ResponseEntity<SuccessResponse<BookListByCategoryRes>> getBookListByCategory(

            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지당 항목 수", example = "24")
            @RequestParam(defaultValue = "24") int size,
            @Parameter(description = "정렬 기준 (예: popularity, recent)", example = "popularity")
            @RequestParam(defaultValue = "popularity") String orderBy,
            @Parameter(description = "카테고리명 (예: ALL, POEM, NOVEL 등)", example = "ALL")
            @RequestParam(defaultValue = "ALL") String category);



    @Operation(
            summary = "도서 DB",
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
    public ResponseEntity<SuccessResponse<Message>> createPost(@Valid @RequestBody BookSaveReq postReq);


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
    public ResponseEntity<SuccessResponse<SearchBookListRes>> searchBookList(
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "페이지당 항목 수", example = "24")
            @RequestParam(defaultValue = "24") int size,

            @Parameter(description = "정렬 기준 (예: newest, relevance)", example = "newest")
            @RequestParam(defaultValue = "newest") String orderBy,

            @Parameter(description = "검색 키워드", example = "자바")
            @RequestParam String word);
}
