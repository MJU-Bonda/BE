package bonda.bonda.domain.book.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Schema(description = "최근 유행하는 도서 목록 읃답")
public class RecentBookListRes {
    @Schema(description = "도서 주제")
    String subject;
    List<BookListRes> bookList;
}
