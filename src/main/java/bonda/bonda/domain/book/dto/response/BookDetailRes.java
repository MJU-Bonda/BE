package bonda.bonda.domain.book.dto.response;

import bonda.bonda.domain.article.dto.response.SimpleArticleRes;
import bonda.bonda.domain.book.domain.BookCategory;
import bonda.bonda.domain.book.domain.Subject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder(toBuilder = true) //추후 조합이 가능하도록 하기 위해
@Schema(description = "도서 상세 응답 DTO")
public class BookDetailRes {
    @Schema(description = "북마크 여부", example = "true")
    Boolean isBookmarked;

    @Schema(description = "도서 카테고리")
    BookCategory category;

    @Schema(description = "도서 제목", example = "자바의 정석")
    String title;

    @Schema(description = "도서 이미지 URL", example = "https://example.com/image.jpg")
    String imageUrl;

    @Schema(description = "저자", example = "남궁성")
    String author;

    @Schema(description = "출판사", example = "도우출판")
    String publisher;

    @Schema(description = "판형", example = "100 * 100")
    String plateType;

    @Schema(description = "페이지 수", example = "864")
    Integer page;

    @Schema(description = "도서 주제")
    Subject subject;

    @Schema(description = "도서 한줄 소개 글")
    String introduction;

    @Schema(description = "본문 내용")
    String content;

    @Schema(description = "도서 조회로 인한 배지 생성 여부", example = "false")
    Boolean isNewBadge;

    @Schema(description = "관련 아티클 목록")
    List<SimpleArticleRes> related_article_list;
}
