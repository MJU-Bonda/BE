package bonda.bonda.domain.book.dto.response;

import bonda.bonda.domain.book.domain.BookCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RelatedBookRes {
    Long bookId;
    String title;
    String author;
    BookCategory category;
    String introduction;
    String content;
}
