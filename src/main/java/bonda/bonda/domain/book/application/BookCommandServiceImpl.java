package bonda.bonda.domain.book.application;

import bonda.bonda.domain.article.domain.Article;
import bonda.bonda.domain.article.dto.response.ToggleBookmarkRes;
import bonda.bonda.domain.articlecase.Articlecase;
import bonda.bonda.domain.badge.application.BadgeService;
import bonda.bonda.domain.badge.domain.ProgressType;
import bonda.bonda.domain.book.domain.Book;
import bonda.bonda.domain.book.domain.BookCategory;
import bonda.bonda.domain.book.domain.repository.BookRepository;
import bonda.bonda.domain.book.dto.aladin.BookDto;
import bonda.bonda.domain.book.dto.aladin.BookListDto;
import bonda.bonda.domain.book.dto.request.SaveBookFromAladinReq;
import bonda.bonda.domain.book.dto.request.SaveBookLocalReq;
import bonda.bonda.domain.book.dto.response.SaveBookLocalRes;
import bonda.bonda.domain.book.dto.response.ToggleBookSaveRes;
import bonda.bonda.domain.bookcase.Bookcase;
import bonda.bonda.domain.bookcase.repository.BookcaseRepository;
import bonda.bonda.domain.member.domain.Member;
import bonda.bonda.domain.member.domain.repository.MemberRepository;
import bonda.bonda.global.common.Message;
import bonda.bonda.global.common.SuccessResponse;
import bonda.bonda.global.config.AladinConfig;
import bonda.bonda.global.exception.BusinessException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static bonda.bonda.global.exception.ErrorCode.*;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookCommandServiceImpl implements BookCommandService {
    private final BookRepository bookRepository;
    private final ObjectMapper objectMapper;
    private final AladinConfig aladinConfig;
    private final RestTemplate restTemplate;
    private final BookcaseRepository bookcaseRepository;
    private final MemberRepository memberRepository;
    private final BadgeService badgeService;

    //도서 카테고리별, 도서 저장 개수
    private static final Map<BookCategory, Integer> maxResultsPerCategory = Map.of(
            BookCategory.PHOTO_BOOK, 20,
            BookCategory.MAGAZINE, 20,
            BookCategory.ART_BOOK, 500,
            BookCategory.ESSAY, 10,
            BookCategory.ILLUSTRATION, 500,
            BookCategory.NOVEL, 10,
            BookCategory.CARTOON, 10,
            BookCategory.POEM, 100
    );


    @Transactional
    public SuccessResponse<Message> saveBookFromAladin(SaveBookFromAladinReq request) {
        List<Book> bookList = new ArrayList<>();

        // 카테고리 꺼내기
        BookCategory categoryEnum = BookCategory.valueOf(request.getCategory().toUpperCase());
        // 카테고리별 저장 개수
        int maxResults = maxResultsPerCategory.getOrDefault(categoryEnum, 10);
        int MAX_PAGE_SIZE = 50; //알라딘 api는 페이지당 최대 100개만 저장 가능
        // 누적 책의 수((page - 1) * MAX_PAGE_SIZE)가 목표 maxResults 보다 작을 때까지 실행
        for (int page = 1; (page - 1) * MAX_PAGE_SIZE < maxResults; page++) {
            // 만약 현재가 마지막 페이지인경우, maxResult 계산
            int fetchCount = Math.min(MAX_PAGE_SIZE, maxResults - (page - 1) * MAX_PAGE_SIZE);

            try {
                String url = String.format(
                        "%s&ttbkey=%s&CategoryId=%s&MaxResults=%d&start=%d",
                        aladinConfig.getListBaseUrl(),
                        aladinConfig.getTtbKey(),
                        request.getCategoryId(),
                        fetchCount,
                        page
                );

                log.info("Aladin API 호출 URL: {}", url);

                String response = restTemplate.getForObject(url, String.class);
                JsonNode root = objectMapper.readTree(response);
                JsonNode items = root.path("item");

                for (JsonNode item : items) {
                    // 기본 정보 파싱
                    BookListDto listDto = new BookListDto();
                    listDto.setPublishDate(LocalDate.parse(item.path("pubDate").asText()));
                    listDto.setImage(item.path("cover").asText());
                    listDto.setPublisher(item.path("publisher").asText());
                    listDto.setTitle(item.path("title").asText());
                    listDto.setWriter(item.path("author").asText());

                    String isbn = item.path("isbn").asText();
                    // 도서 db 중복 체크
                    log.info("도서 제목: {}", listDto.getTitle());
                    if (bookRepository.existsByTitle((item.path("title").asText()))) {
                        log.info("중복 도서 제목으로 건너뜀: {}", item.path("title").asText());
                        continue;
                    }

                    try {
                        String detailUrl = String.format(
                                "%s&ItemId=%s&ttbkey=%s",
                                aladinConfig.getDetailBaseUrl(),
                                isbn,
                                aladinConfig.getTtbKey()
                        );

                        String detailResponse = restTemplate.getForObject(detailUrl, String.class);
                        JsonNode detailItem = objectMapper.readTree(detailResponse)
                                .path("item").get(0);

                        BookDto bookDto = new BookDto();
                        bookDto.setPage(detailItem.path("subInfo").path("itemPage").asInt());
                        bookDto.setWidth(detailItem.path("subInfo").path("packing").path("sizeWidth").asLong());
                        bookDto.setHeight(detailItem.path("subInfo").path("packing").path("sizeHeight").asLong());
                        bookDto.setContent(detailItem.path("description").asText());

                        Book book = Book.builder()
                                .image(listDto.getImage())
                                .title(listDto.getTitle())
                                .writer(listDto.getWriter())
                                .publisher(listDto.getPublisher())
                                .size(String.format("%d * %dmm", bookDto.getWidth(), bookDto.getHeight()))
                                .publishDate(listDto.getPublishDate())
                                .page(bookDto.getPage())
                                .content(bookDto.getContent())
                                .bookCategory(categoryEnum)
                                .build();

                        bookList.add(book);

                    } catch (Exception e) {
                        log.warn("상세 정보 조회 실패 (isbn: {})", isbn, e);
                    }
                }

            } catch (Exception e) {
                log.error("알라딘 API 호출 또는 파싱 실패 (page: {})", page, e);
            }
        }

        if (!bookList.isEmpty()) {
            bookRepository.saveAll(bookList);
            log.info("카테고리 [{}]에 도서 {}권 저장 완료", request.getCategory(), bookList.size());
            return SuccessResponse.of(new Message("도서가 정상적으로 저장되었습니다."));
        } else {
            return SuccessResponse.of(new Message("저장된 도서가 없습니다."));
        }
    }

    @Transactional
    public SuccessResponse<ToggleBookSaveRes> toggleBookSaveRes(Member member, Long bookId) {
        // 멤버 조회 -> 영속 상태로 조회
        Member persistMember = memberRepository.findByKakaoId(member.getKakaoId()).orElseThrow(() -> new BusinessException(INVALID_MEMBER));
        // 도서 조회
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new BusinessException(INVALID_BOOK_Id));
        // 저장한 도서 조회 있으면 삭제 없으면 생성
        Optional<Bookcase> existing = bookcaseRepository.findByMemberAndBook(persistMember, book);
        //존재하는 경우 -> 삭제
        if (existing.isPresent()) {
            bookcaseRepository.delete(existing.get());
            persistMember.minusSaveCount();   // 멤버 저장 도서 수 감소 -> 더티 체크
            return SuccessResponse.of(ToggleBookSaveRes.builder()
                    .bookId(bookId)
                    .isNewBadge(false)
                    .message("도서 저장이 해제되었습니다.")
                    .build());
        }
        //존재하지 않는 경우
        // 저장한 도서 생성
        Bookcase bookcase = new Bookcase(persistMember, book);
        // 멤버 저장 도서 수 증가 -> 더티 체크
        persistMember.plusSaveCount();
        // 저장
        bookcaseRepository.save(bookcase);
        return SuccessResponse.of(ToggleBookSaveRes.builder()
                .bookId(bookId)
                .isNewBadge(badgeService.checkAndAwardBadges(persistMember, ProgressType.BOOK_SAVE))
                .message("도서 저장이 완료되었습니다.")
                .build());

    }

    @Override
    @Transactional
    public SuccessResponse<SaveBookLocalRes> saveBookLocal(SaveBookLocalReq req) {
        Book book = Book.builder()
                .image(req.getImage())
                .title(req.getTitle())
                .writer(req.getWriter())
                .publisher(req.getPublisher())
                .size(req.getSize())
                .publishDate(req.getPublishDate())
                .page(req.getPage())
                .introduction(req.getIntroduction())
                .content(req.getContent())
                .bookCategory(req.getBookCategory())
                .subject(req.getSubject())
                .build();

        bookRepository.save(book);

        return SuccessResponse.of(SaveBookLocalRes.builder()
                        .id(book.getId())
                        .title(book.getTitle())
                        .image(book.getImage())
                        .build());
    }
}
