package bonda.bonda.global.exception;

import lombok.Getter;

/**
 * [공통 코드] API 통신에 대한 '에러 코드'를 Enum 형태로 관리를 한다.
 * Global Error CodeList : 전역으로 발생하는 에러코드를 관리한다.
 * Custom Error CodeList : 업무 페이지에서 발생하는 에러코드를 관리한다
 * Error Code Constructor : 에러코드를 직접적으로 사용하기 위한 생성자를 구성한다.
 */
@Getter
public enum ErrorCode {

    /**
     * ******************************* Global Error CodeList ***************************************
     * HTTP Status Code
     * 400 : Bad Request
     * 401 : Unauthorized
     * 403 : Forbidden
     * 404 : Not Found
     * 500 : Internal Server Error
     * *********************************************************************************************
     */
    // 잘못된 서버 요청
    BAD_REQUEST_ERROR(400, "G001", "Bad Request Exception"),

    // @RequestBody 데이터 미 존재
    REQUEST_BODY_MISSING_ERROR(400, "G002", "Required request body is missing"),

    // 유효하지 않은 타입
    INVALID_TYPE_VALUE(400, "G003", " Invalid Type Value"),

    // Request Parameter 로 데이터가 전달되지 않을 경우
    MISSING_REQUEST_PARAMETER_ERROR(400, "G004", "Missing Servlet RequestParameter Exception"),

    // 입력/출력 값이 유효하지 않음
    IO_ERROR(400, "G005", "I/O Exception"),

    // com.google.gson JSON 파싱 실패
    JSON_PARSE_ERROR(400, "G006", "JsonParseException"),

    // com.fasterxml.jackson.core Processing Error
    JACKSON_PROCESS_ERROR(400, "G007", "com.fasterxml.jackson.core Exception"),

    // 권한이 없음
    FORBIDDEN_ERROR(403, "G008", "Forbidden Exception"),

    // 서버로 요청한 리소스가 존재하지 않음
    NOT_FOUND_ERROR(404, "G009", "Not Found Exception"),

    // NULL Point Exception 발생
    NULL_POINT_ERROR(404, "G010", "Null Point Exception"),

    // @RequestBody 및 @RequestParam, @PathVariable 값이 유효하지 않음
    NOT_VALID_ERROR(404, "G011", "handle Validation Exception"),

    // @RequestBody 및 @RequestParam, @PathVariable 값이 유효하지 않음
    NOT_VALID_HEADER_ERROR(404, "G012", "Header에 데이터가 존재하지 않는 경우 "),

    // RuntimeException으로 뭔가 잘못된 데이터가 바인딩 됨
    CONFLICT(409, "G013", "Data Integrity Violation Exception"),

    // 이미 처리된 작업 또는 데이터
    ALREADY_EXISTS(409, "G014", "An attempt was made to create an entity but the entity already exists."),

    // 인증되지 않음
    UNAUTHORIZED_ERROR(401, "G015", "Unauthorized Exception"),

    // 서버가 처리 할 방법을 모르는 경우 발생
    INTERNAL_SERVER_ERROR(500, "G999", "Internal Server Error Exception"),


    /**
     * ******************************* Custom Error CodeList ***************************************
     */
    // Transaction Insert Error
    INSERT_ERROR(200, "9999", "Insert Transaction Error Exception"),

    // Transaction Update Error
    UPDATE_ERROR(200, "9999", "Update Transaction Error Exception"),

    // Transaction Delete Error
    DELETE_ERROR(200, "9999", "Delete Transaction Error Exception"),

    // Business Error
    BUSINESS_EXCEPTION_ERROR(400, "B999", "Business Exception Error"),

    NETWORK_AUTHENTICATION_REQUIRED(511, "B998", "Network Authentication Required"),

    ILLEGAL_ARGUMENT_EXCEPTION_ERROR(400, "B997", "Illegal Argument Exception Error"),

    ARRAY_INDEX_OUT_OF_BOUNDS_ERROR(400, "B996", "Array Index Out of Bounds Error"),

    FILE_UPLOAD_FAILED(500, "B995", "S3 버킷에 파일(이미지) 업로드를 실패했습니다."),

    FILE_DELETE_FAILED(500, "B994", "S3 버킷에 파일(이미지) 삭제를 실패했습니다."),

    INVALID_BOOK_CATEGORY(400, "BC001", "유효하지 않은 책 카테고리입니다."),
    INVALID_BOOK_SUBJECT(400, "BS001", "유효하지 않은 책 주제입니다."),

    INVALID_BOOK_Id(400, "BC002", "유효하지 않은 책 아이디."),

    ALREADY_SAVED_BOOK(400, "BC003", "이미 저장한 도서입니다."),
    SAVED_BOOK_NOT_EXIST(400, "BC004", "저장된 도서가 없습니다"),

    INVALID_MEMBER(400, "MB001", "유효하지 않은 회원입니다."),

    INVALID_ARTICLE_ID(400, "A001", "유효하지 않은 아티클 아이디."),
    ALREADY_SAVED_ARTICLE(400, "AS001", "이미 저장한 아티클입니다."),
    SAVED_ARTICLE_NOT_EXIST(400, "AS002", "저장된 아티클이 없습니다"),


    INVALID_ARTICLE_CATEGORY(400, "AC001", "유효하지 않은 아티클 카테고리입니다.");


    // End

    /**
     * ******************************* Error Code Constructor ***************************************
     */
    // 에러 코드의 '코드 상태'을 반환한다.
    private final int status;

    // 에러 코드의 '코드간 구분 값'을 반환한다.
    private final String divisionCode;

    // 에러 코드의 '코드 메시지'을 반환한다.
    private final String message;

    // 생성자 구성
    ErrorCode(final int status, final String divisionCode, final String message) {
        this.status = status;
        this.divisionCode = divisionCode;
        this.message = message;
    }
}
