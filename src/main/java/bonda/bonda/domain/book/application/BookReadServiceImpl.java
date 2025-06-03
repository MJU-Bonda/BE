package bonda.bonda.domain.book.application;

import bonda.bonda.domain.book.domain.Book;
import bonda.bonda.domain.book.domain.BookCategory;
import bonda.bonda.domain.book.domain.Subject;
import bonda.bonda.domain.book.domain.repository.BookRepository;
import bonda.bonda.domain.book.dto.response.BookListByCategoryRes;
import bonda.bonda.domain.book.dto.response.BookListRes;
import bonda.bonda.domain.book.dto.response.LovedBookListRes;
import bonda.bonda.domain.book.dto.response.MySavedBookListRes;
import bonda.bonda.domain.member.domain.Member;
import bonda.bonda.domain.member.domain.repository.MemberRepository;
import bonda.bonda.global.common.SuccessResponse;
import bonda.bonda.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static bonda.bonda.domain.book.dto.BookMapper.convertToBookListRes;
import static bonda.bonda.global.exception.ErrorCode.*;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BookReadServiceImpl implements BookReadService {
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    @Override
    public SuccessResponse<BookListByCategoryRes> bookListByCategory(Integer page, Integer size, String orderBy, String category) {
        if (BookCategory.isValid(category) == false) {
            throw new BusinessException("Invalid book category: " + category, INVALID_BOOK_CATEGORY);
        }

        Pageable pageable = PageRequest.of(page, size);

        Page<Book> bookList = bookRepository.findBookListByCategory(pageable, orderBy, category).orElseThrow(() -> new BusinessException(NOT_FOUND_ERROR));
        //Book 리스트를 bookListRes로 변환
        List<BookListRes> bookListRes = convertToBookListRes(bookList.getContent());

        return SuccessResponse.of(BookListByCategoryRes.builder()
                .page(page)
                .total(bookList.getTotalElements())
                .category(category)
                .orderBy(orderBy)
                .hasNextPage(bookList.hasNext())
                .bookList(bookListRes)
                .build());
    }

    @Override
    public SuccessResponse<LovedBookListRes> getLovedBookList(String subject) {
        // 입력받은 주제 검증
        if (Subject.isValid(subject) == false) {
            throw new BusinessException("Invalid book subject: " + subject, INVALID_BOOK_SUBJECT);
        }
        ;
        List<Book> lovedBookList = bookRepository.findLovedBookList(subject);
        List<BookListRes> bookListRes = convertToBookListRes(lovedBookList);
        return SuccessResponse.of(LovedBookListRes.builder()
                .bookList(bookListRes)
                .subject(subject)
                .build());
    }


    @Override
    public SuccessResponse<MySavedBookListRes> getMySavedBookList(int page, int size, String orderBy, Member member) {


        // 페이지 정보
        Pageable pageable = PageRequest.of(page, size);
        Page<Book> bookList = bookRepository.findMySavedBookList(pageable, orderBy, member);
        List<BookListRes> bookListRes = convertToBookListRes(bookList.getContent());
        return SuccessResponse.of(MySavedBookListRes.builder()
                .page(page)
                .total(bookList.getTotalElements())
                .orderBy(orderBy)
                .hasNextPage(bookList.hasNext())
                .bookList(bookListRes)
                .build());
    }
}

