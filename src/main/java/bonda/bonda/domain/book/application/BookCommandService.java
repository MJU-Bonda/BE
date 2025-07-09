package bonda.bonda.domain.book.application;

import bonda.bonda.domain.book.dto.request.SaveBookFromAladinReq;
import bonda.bonda.domain.book.dto.response.ToggleBookSaveRes;
import bonda.bonda.domain.member.domain.Member;
import bonda.bonda.global.common.Message;
import bonda.bonda.global.common.SuccessResponse;

public interface BookCommandService {
    SuccessResponse<Message> saveBookFromAladin(SaveBookFromAladinReq request);

    SuccessResponse<ToggleBookSaveRes> toggleBookSaveRes(Member member, Long bookId);

}
