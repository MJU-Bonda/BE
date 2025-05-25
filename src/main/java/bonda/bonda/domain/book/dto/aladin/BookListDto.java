package bonda.bonda.domain.book.dto.aladin;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BookListDto {
    String title;
    String image;
    String publisher;
    String writer;
    LocalDate publishDate;
}
