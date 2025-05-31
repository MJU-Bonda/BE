package bonda.bonda.domain.book.application;

import bonda.bonda.domain.book.domain.Book;
import bonda.bonda.domain.book.domain.repository.BookRepository;
import bonda.bonda.domain.book.dto.response.*;
import bonda.bonda.global.common.SuccessResponse;
import bonda.bonda.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static bonda.bonda.domain.book.dto.BookMapper.convertToBookListRes;
import static bonda.bonda.global.exception.ErrorCode.NOT_FOUND_ERROR;

@Service
@Transactional
@RequiredArgsConstructor
public class BookSearchServiceImpl implements BookSearchService {
    private final BookRepository bookRepository;

    @Override
    public SuccessResponse<SearchBookListRes> searchBookList(Integer page, Integer size, String orderBy, String word) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Book> bookList = bookRepository.searchBookList(pageable, orderBy, word).orElseThrow(() -> new BusinessException(NOT_FOUND_ERROR));
        List<BookListRes> bookListRes = convertToBookListRes(bookList.getContent());

        return SuccessResponse.of(SearchBookListRes.builder()
                .page(page)
                .total(bookList.getTotalElements())
                .word(word)
                .orderBy(orderBy)
                .hasNextPage(bookList.hasNext())
                .bookList(bookListRes)
                .build());
    }
}
