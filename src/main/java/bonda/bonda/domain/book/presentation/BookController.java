package bonda.bonda.domain.book.presentation;

import bonda.bonda.domain.book.application.BookCommandService;
import bonda.bonda.domain.book.application.BookReadService;
import bonda.bonda.domain.book.dto.request.BookSaveReq;
import bonda.bonda.domain.book.dto.response.BookListByCategoryRes;
import bonda.bonda.global.common.Message;
import bonda.bonda.global.common.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/books")
public class BookController {
    private final BookReadService bookReadService;
    private final BookCommandService bookCommandService;
    @GetMapping()
    public ResponseEntity<SuccessResponse<BookListByCategoryRes>> getBookListByCategory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "24") int size,
            @RequestParam(defaultValue = "popularity") String orderBy,
            @RequestParam(defaultValue = "ALL") String category) {
        return ResponseEntity.ok(bookReadService.bookListByCategory(page, size, orderBy, category));
    }

    @PostMapping("/new")
    public ResponseEntity<SuccessResponse<Message>> createPost(@RequestBody BookSaveReq postReq) {
        return ResponseEntity.ok(bookCommandService.saveBook(postReq));
    }


}
