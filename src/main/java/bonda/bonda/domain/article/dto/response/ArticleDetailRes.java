package bonda.bonda.domain.article.dto.response;

import bonda.bonda.domain.article.domain.ArticleCategory;
import bonda.bonda.domain.book.dto.response.RelatedBookRes;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder(toBuilder = true) //추후 조합이 가능하도록 하기 위해
public class ArticleDetailRes {
    Long articleId;
    String title;
    String introduction;
    String content;
    ArticleCategory articleCategory;
    Boolean isBookmarked;
    String imageUrl;
    List<RelatedBookRes> relatedBookList;
    List<SimpleArticleRes> otherArticleList;

}
