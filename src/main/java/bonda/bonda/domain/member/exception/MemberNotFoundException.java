package bonda.bonda.domain.member.exception;

import bonda.bonda.global.exception.ErrorCode;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberNotFoundException extends RuntimeException {

    private ErrorCode errorCode;

    @Builder
    public MemberNotFoundException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    @Builder
    public MemberNotFoundException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
