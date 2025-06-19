package bonda.bonda.domain.book.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RecentViewBookListRes {
    Integer page;
    Boolean hasNextPage;
    List<BookListRes> bookList;
}
