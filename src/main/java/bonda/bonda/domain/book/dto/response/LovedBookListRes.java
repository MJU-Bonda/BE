package bonda.bonda.domain.book.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class LovedBookListRes {
    String subject;
    List<BookListRes> bookList;
}
