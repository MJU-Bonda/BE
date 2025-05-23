package bonda.bonda.domain.book.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;


@Data
@Builder
public class BookListByCategoryRes {
    Integer page;
    Long total;
    String category;
    String orderBy;
    Boolean hasNextPage;
    List<BookListRes> bookList;

}
