package bonda.bonda.domain.book.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Schema(description = "최근 조회한 도서 목록 응답 DTO")
public class RecentViewBookListRes {
    @Schema(description = "요청 페이지", example = "0")
    Integer page;
    @Schema(description = "다음 페이지 존재 여부", example = "false")
    Boolean hasNextPage;
    @Schema(description = "도서 리스트 목록 dto", example = "0")
    List<BookListRes> bookList;
}
