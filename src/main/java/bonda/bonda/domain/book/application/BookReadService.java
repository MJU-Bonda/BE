package bonda.bonda.domain.book.application;

import bonda.bonda.domain.book.dto.response.BookListByCategoryRes;
import bonda.bonda.domain.book.dto.response.LovedBookListRes;
import bonda.bonda.domain.book.dto.response.MySavedBookListRes;
import bonda.bonda.domain.member.domain.Member;
import bonda.bonda.global.common.SuccessResponse;

public interface BookReadService {
    SuccessResponse<BookListByCategoryRes> bookListByCategory(Integer page,Integer size, String orderBy,String category);

    SuccessResponse<LovedBookListRes> getLovedBookList(String subject);

    SuccessResponse<MySavedBookListRes> getMySavedBookList(int page, int size, String orderBy, Member member);
}

