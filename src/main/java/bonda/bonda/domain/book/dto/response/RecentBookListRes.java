package bonda.bonda.domain.book.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RecentBookListRes {
    String subject;
    List<BookListRes> bookList;
}
