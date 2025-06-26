package bonda.bonda.domain.article.presentation;

import bonda.bonda.domain.article.application.ArticleCommandService;
import bonda.bonda.domain.article.application.ArticleReadService;
import bonda.bonda.domain.article.application.ArticleSearchService;
import bonda.bonda.domain.article.dto.response.*;
import bonda.bonda.domain.member.domain.Member;
import bonda.bonda.global.annotation.LoginMember;
import bonda.bonda.global.common.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/articles")
public class ArticleController implements ArticleApi {
    private final ArticleReadService articleReadService;
    private final ArticleCommandService articleCommandService;
    private final ArticleSearchService articleSearchService;

    @Override
    @GetMapping()
    public ResponseEntity<SuccessResponse<ArticleListByCategoryRes>> getArticleListByCategory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(value = "articleCategory") String category,
            @LoginMember Member member) {
        return ResponseEntity.ok(articleReadService.getArticleListByCategory(page, size, category, member));
    }

    @Override
    @PostMapping("save/{articleId}")
    public ResponseEntity<SuccessResponse<SaveArticleRes>> saveArticle(@PathVariable("articleId") Long articleId, @LoginMember Member member) {
        return ResponseEntity.ok(articleCommandService.saveArticle(member, articleId));
    }

    @Override
    @DeleteMapping("save/{articleId}")
    public ResponseEntity<SuccessResponse<DeleteSaveArticleRes>> deleteSaveArticle(@PathVariable("articleId") Long articleId, @LoginMember Member member) {
        return ResponseEntity.ok(articleCommandService.deleteSaveArticle(member, articleId));
    }

    @Override
    @GetMapping("search")
    public ResponseEntity<SuccessResponse<SearchArticleListRes>> searchArticleList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "24") int size,
            @RequestParam(defaultValue = "newest") String orderBy,
            @RequestParam String word,
            @LoginMember Member member) {
        return ResponseEntity.ok(articleSearchService.searchArticleList(page, size, orderBy, word, member));
    }

    @Override
    @GetMapping("/my-save")
    public ResponseEntity<SuccessResponse<MySavedArticleListRes>> getMySavedArticleListRes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "24") int size,
            @RequestParam(defaultValue = "recentlySaved") String orderBy,
            @LoginMember Member member) {
        return ResponseEntity.ok(articleReadService.getMySavedArticleList(page, size, orderBy, member));
    }

    @Override
    @GetMapping("{articleId}")
    public ResponseEntity<SuccessResponse<ArticleDetailRes>> getArticleDetail(@PathVariable("articleId") Long articleId, @LoginMember Member member) {
        return ResponseEntity.ok(articleReadService.getArticleDetail(articleId, member));
    }

}
