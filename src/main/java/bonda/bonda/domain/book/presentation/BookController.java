package bonda.bonda.domain.book.presentation;

import bonda.bonda.domain.book.application.BookCommandService;
import bonda.bonda.domain.book.application.BookReadService;
import bonda.bonda.domain.book.application.BookSearchService;
import bonda.bonda.domain.book.dto.request.SaveBookFromAladinReq;
import bonda.bonda.domain.book.dto.response.*;
import bonda.bonda.domain.member.domain.Member;
import bonda.bonda.global.annotation.LoginMember;
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

    @Override
    @GetMapping()
    public ResponseEntity<SuccessResponse<BookListByCategoryRes>> getBookListByCategory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "24") int size,
            @RequestParam(defaultValue = "popularity") String orderBy,
            @RequestParam(defaultValue = "ALL") String category) {
        return ResponseEntity.ok(bookReadService.bookListByCategory(page, size, orderBy, category));
    }

    @Override
    @PostMapping("/new")
    public ResponseEntity<SuccessResponse<Message>> saveBookFromAladin(@Valid @RequestBody SaveBookFromAladinReq postReq) {
        return ResponseEntity.ok(bookCommandService.saveBookFromAladin(postReq));
    }

    @Override
    @GetMapping("/search")
    public ResponseEntity<SuccessResponse<SearchBookListRes>> searchBookList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "24") int size,
            @RequestParam(defaultValue = "newest") String orderBy,
            @RequestParam String word,
            @LoginMember Member member) {
        return ResponseEntity.ok(bookSearchService.searchBookList(page, size, orderBy, word, member));
    }

    @Override
    @PostMapping("/save/{bookId}")
    public ResponseEntity<SuccessResponse<SaveBookRes>> saveBook(@PathVariable(value = "bookId") Long bookId, @LoginMember Member member) {
        return ResponseEntity.ok(bookCommandService.saveBook(member, bookId));

    }

    @Override
    @DeleteMapping("/save/{bookId}")
    public ResponseEntity<SuccessResponse<DeleteSaveBookRes>> deleteBook(@PathVariable(value = "bookId") Long bookId, @LoginMember Member member) {
        return ResponseEntity.ok(bookCommandService.deleteSaveBook(member, bookId));
    }

    @Override
    @GetMapping("/liked")
    public ResponseEntity<SuccessResponse<LovedBookListRes>> getLovedBookList(@RequestParam(defaultValue = "ALL") String subject) {
        return ResponseEntity.ok(bookReadService.getLovedBookList(subject));
    }

    @Override
    @GetMapping("/my-save")
    public ResponseEntity<SuccessResponse<MySavedBookListRes>> getMySavedBookListRes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "24") int size,
            @RequestParam(defaultValue = "recentlySaved") String orderBy,
            @LoginMember Member member) {
        return ResponseEntity.ok(bookReadService.getMySavedBookList(page, size, orderBy, member));
    }
}
