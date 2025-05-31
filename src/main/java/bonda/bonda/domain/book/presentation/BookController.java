package bonda.bonda.domain.book.presentation;

import bonda.bonda.domain.book.application.BookCommandService;
import bonda.bonda.domain.book.application.BookReadService;
import bonda.bonda.domain.book.application.BookSearchService;
import bonda.bonda.domain.book.dto.request.BookSaveReq;
import bonda.bonda.domain.book.dto.response.BookListByCategoryRes;
import bonda.bonda.domain.book.dto.response.SearchBookListRes;
import bonda.bonda.global.common.Message;
import bonda.bonda.global.common.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/books")
public class BookController implements BookApi {
    private final BookReadService bookReadService;
    private final BookCommandService bookCommandService;
    private final BookSearchService bookSearchService;

    @GetMapping()
    public ResponseEntity<SuccessResponse<BookListByCategoryRes>> getBookListByCategory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "24") int size,
            @RequestParam(defaultValue = "popularity") String orderBy,
            @RequestParam(defaultValue = "ALL") String category) {
        return ResponseEntity.ok(bookReadService.bookListByCategory(page, size, orderBy, category));
    }

    @PostMapping("/new")
    public ResponseEntity<SuccessResponse<Message>> createPost(@Valid @RequestBody BookSaveReq postReq) {
        return ResponseEntity.ok(bookCommandService.saveBook(postReq));
    }

    @GetMapping("/search")
    public ResponseEntity<SuccessResponse<SearchBookListRes>> searchBookList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "24") int size,
            @RequestParam(defaultValue = "newest") String orderBy,
            @RequestParam String word) {
        return ResponseEntity.ok(bookSearchService.searchBookList(page, size, orderBy, word));
    }

}
