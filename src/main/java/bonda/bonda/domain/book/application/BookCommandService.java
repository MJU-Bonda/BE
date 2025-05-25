package bonda.bonda.domain.book.application;

import bonda.bonda.domain.book.dto.request.BookSaveReq;
import bonda.bonda.global.common.Message;
import bonda.bonda.global.common.SuccessResponse;

public interface BookCommandService {
    SuccessResponse<Message> saveBook(BookSaveReq request);

}
