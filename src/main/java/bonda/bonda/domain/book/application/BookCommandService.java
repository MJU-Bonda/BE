package bonda.bonda.domain.book.application;

import bonda.bonda.domain.book.dto.request.SaveBookFromAladinReq;
import bonda.bonda.domain.book.dto.response.SaveBookRes;
import bonda.bonda.domain.member.domain.Member;
import bonda.bonda.global.common.Message;
import bonda.bonda.global.common.SuccessResponse;

public interface BookCommandService {
    SuccessResponse<Message> saveBookFromAladin(SaveBookFromAladinReq request);

    SuccessResponse<SaveBookRes> saveBook(Member member, Long bookId);
}
