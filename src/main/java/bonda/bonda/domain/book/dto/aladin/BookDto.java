package bonda.bonda.domain.book.dto.aladin;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {
   Integer page;
   Long height;
   Long width;
   String content;
}
