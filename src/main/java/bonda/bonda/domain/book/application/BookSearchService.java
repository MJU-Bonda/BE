package bonda.bonda.domain.book.application;

import bonda.bonda.domain.book.dto.response.*;
import bonda.bonda.domain.member.domain.Member;
import bonda.bonda.global.common.SuccessResponse;

public interface BookSearchService {
    SuccessResponse<SearchBookListRes> searchBookList(Integer page, Integer size, String orderBy, String word, Member member);

}
