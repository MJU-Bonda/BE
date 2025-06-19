package bonda.bonda.domain.book.application;

import bonda.bonda.domain.article.dto.ArticleMapper;
import bonda.bonda.domain.badge.application.BadgeService;
import bonda.bonda.domain.badge.domain.ProgressType;
import bonda.bonda.domain.book.domain.Book;
import bonda.bonda.domain.book.domain.BookCategory;
import bonda.bonda.domain.book.domain.Subject;
import bonda.bonda.domain.book.domain.repository.BookRepository;
import bonda.bonda.domain.book.dto.response.*;
import bonda.bonda.domain.bookarticle.BookArticle;
import bonda.bonda.domain.bookarticle.repository.BookArticleRepository;
import bonda.bonda.domain.member.domain.Member;
import bonda.bonda.domain.recentviewbook.RecentViewBook;
import bonda.bonda.domain.recentviewbook.repository.RecentViewBookRepository;
import bonda.bonda.global.common.SuccessResponse;
import bonda.bonda.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static bonda.bonda.domain.book.dto.BookMapper.convertToBookListRes;
import static bonda.bonda.global.exception.ErrorCode.*;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BookReadServiceImpl implements BookReadService {
    private final BookRepository bookRepository;
    private final BookArticleRepository bookArticleRepository;
    private final RecentViewBookRepository recentViewBookRepository;
    private final BadgeService badgeService;
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

    @Override
    @Transactional
    public SuccessResponse<BookDetailRes> getBookDetail(Long bookId, Member member) {
        //책 조회 -> 없으면 오류
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new BusinessException(INVALID_BOOK_Id));
        BookDetailRes bookDetailRes = getBookDetailResWithIsBookMarked(bookId, member); //응답에 기본 정보 체우기
        return SuccessResponse.of(viewAndBadgeCheck(member, book, bookDetailRes));// 조회 기록 및 뱃지 처리해서 응답 반환
    }

    @Override
    public SuccessResponse<RecentViewBookListRes> getRecentViewBookList(int page, int size,Member member) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Book> recentViewBookList = bookRepository.findRecentViewBookList(member, pageable);
        List<BookListRes> bookListRes = convertToBookListRes(recentViewBookList.getContent());
        return SuccessResponse.of(RecentViewBookListRes.builder()
                .page(page)
                .hasNextPage(recentViewBookList.hasNext())
                .bookList(bookListRes)
                .build());
    }

    /**책 기본 정보와 북마크 여부 조회 및 res 일부 체우기**/
    private BookDetailRes getBookDetailResWithIsBookMarked(Long bookId, Member member) {
        BookDetailRes bookDetailRes = bookRepository.findBookDetailResWithIsBookMarked(bookId, member); //도서 기본 정보 체우기
        //연관된 아티클 체우기 -> 없으면 빈 리스트
        bookDetailRes = bookDetailRes.toBuilder()
                .related_article_list(ArticleMapper.convertToSimpleArticleListRes( //bookArticle에서 연관 3개 가져오고, mapper로 컨버터하기
                        bookArticleRepository.findWithArticleByBookId(bookId, PageRequest.of(0, 3)) // fetch 조인으로 article도 한번에 조회
                                .stream() // 가져온 bookArticle에서 article만 추출해서 mapper에 집어넣기
                                .map(BookArticle::getArticle) //fetch 안하면 여기서 n+1 발생
                                .collect(Collectors.toList())
                ))
                .build();
        return bookDetailRes;
    }

    /**최근 조회 도서 및 뱃지 처리**/
    private BookDetailRes viewAndBadgeCheck(Member member, Book book, BookDetailRes bookDetailRes) {
        RecentViewBook bookView = recentViewBookRepository.findByMemberAndBook(member, book).orElse(null); //최근 조회 도서 -> 없으면 null
        if (bookView == null) { // 첫 조회인 경우 생성 및 뱃지 체크
            recentViewBookRepository.save(new RecentViewBook(member, book));
            bookDetailRes = bookDetailRes.toBuilder() // 뱃지 체크 여부 체우기
                    .isNewBadge(badgeService.checkAndAwardBadges(member, ProgressType.BOOK_VIEW)) // 생성 되면 true (디폴트:false)
                    .build();
        } else { // 기존 조회 기록 존재: 마지막 조회 시간 업데이트
            bookView.updateViewDate(LocalDateTime.now()); // 더티체킹
        }
        return bookDetailRes;
    }

}

