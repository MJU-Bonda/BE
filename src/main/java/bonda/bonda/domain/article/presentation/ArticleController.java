package bonda.bonda.domain.article.presentation;

import bonda.bonda.domain.article.application.ArticleReadService;
import bonda.bonda.domain.article.dto.response.ArticleListByCategoryRes;
import bonda.bonda.domain.member.domain.Member;
import bonda.bonda.global.annotation.LoginMember;
import bonda.bonda.global.common.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/articles")
public class ArticleController {
    private final ArticleReadService articleReadService;


    @GetMapping("{articleCategory}")
    public ResponseEntity<SuccessResponse<ArticleListByCategoryRes>> getArticleListByCategory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "24") int size,
            @PathVariable(value = "articleCategory") String category,
            @LoginMember Member member) {
        return ResponseEntity.ok(articleReadService.getArticleListByCategory(page, size, category, member));
    }

}
