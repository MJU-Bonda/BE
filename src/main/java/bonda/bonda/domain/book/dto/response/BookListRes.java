package bonda.bonda.domain.book.dto.response;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookListRes {
    Long id;
    String title;
    String author;
    String imageUrl;
    String subject;
}
