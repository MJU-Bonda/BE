package bonda.bonda.domain.book.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "도서 객체 응답 DTO")
public class BookListRes {
    @Schema(description = "도서 아이디", example = "1")
    Long id;
    @Schema(description = "도서 제목", example = "title")
    String title;
    @Schema(description = "도서 작가", example = "author")
    String author;
    @Schema(description = "도서 이미지 url")
    String imageUrl;
    @Schema(description = "도서 카테고리")
    String category;
}
