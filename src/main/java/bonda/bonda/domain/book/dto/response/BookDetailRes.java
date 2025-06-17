package bonda.bonda.domain.book.dto.response;

import bonda.bonda.domain.article.dto.response.SimpleArticleRes;
import bonda.bonda.domain.book.domain.BookCategory;
import bonda.bonda.domain.book.domain.Subject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder(toBuilder = true) //추후 조합이 가능하도록 하기 위해
public class BookDetailRes {
    Boolean isBookmarked;
    BookCategory category;
    String title;
    String imageUrl;
    String author;
    String publisher;
    String plateType;
    Integer page;
    Subject subject;
    String introduction;
    String content;
    Boolean isNewBadge;
    List<SimpleArticleRes> related_article_list;
}
