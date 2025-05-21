package bonda.bonda.domain.book.application;

import bonda.bonda.domain.book.dto.response.BookListByCategoryRes;
import bonda.bonda.global.common.SuccessResponse;

public interface BookReadService {
    SuccessResponse<BookListByCategoryRes> bookListByCategory(Integer page,Integer size, String orderBy,String category);
}

