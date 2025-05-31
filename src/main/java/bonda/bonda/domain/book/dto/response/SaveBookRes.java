package bonda.bonda.domain.book.dto.response;

import bonda.bonda.global.common.Message;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SaveBookRes {
    Long bookId;
    Message message;
}
