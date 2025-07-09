package bonda.bonda.domain.book.dto.response;

import bonda.bonda.domain.book.domain.BookCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@Schema(description = "연관된 도서 DTO")
public class RelatedBookRes {
    @Schema(description = "도서 아이디")
    Long bookId;
    @Schema(description = "도서 제목")
    String title;
    @Schema(description = "도서 작가")
    String author;
    @Schema(description = "도서 카테고리")
    BookCategory category;
    @Schema(description = "도서 소개글")
    String introduction;
    @Schema(description = "도서 본문")
    String content;
    @Schema(description = "도서 이미지 URL")
    String imageUrl;
}
