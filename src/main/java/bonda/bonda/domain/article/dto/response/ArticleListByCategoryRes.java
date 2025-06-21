package bonda.bonda.domain.article.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ArticleListByCategoryRes {
    Integer page;
    String category;
    Boolean hasNextPage;
    List<SimpleArticleResWithBookmarked> articleList;
}
