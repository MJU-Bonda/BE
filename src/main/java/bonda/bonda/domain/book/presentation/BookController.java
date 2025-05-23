package bonda.bonda.domain.book.presentation;

import bonda.bonda.domain.book.application.BookReadService;
import bonda.bonda.domain.book.dto.response.BookListByCategoryRes;
import bonda.bonda.global.common.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/books")
public class BookController {
    private final BookReadService bookReadService;

    @GetMapping()
    public ResponseEntity<SuccessResponse<BookListByCategoryRes>> getBookListByCategory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "24") int size,
            @RequestParam(defaultValue = "popularity") String orderBy,
            @RequestParam(defaultValue = "ALL") String category) {
        return ResponseEntity.ok(bookReadService.bookListByCategory(page, size, orderBy, category));
    }
}
