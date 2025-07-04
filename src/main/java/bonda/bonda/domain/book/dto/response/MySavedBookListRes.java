package bonda.bonda.domain.book.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MySavedBookListRes {
    Integer page;
    Long total;
    String orderBy;
    Boolean hasNextPage;
    List<BookListRes> bookList;
}
