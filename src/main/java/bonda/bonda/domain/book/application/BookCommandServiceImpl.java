package bonda.bonda.domain.book.application;

import bonda.bonda.domain.badge.application.BadgeService;
import bonda.bonda.domain.badge.domain.ProgressType;
import bonda.bonda.domain.book.domain.Book;
import bonda.bonda.domain.book.domain.BookCategory;
import bonda.bonda.domain.book.domain.repository.BookRepository;
import bonda.bonda.domain.book.dto.aladin.BookDto;
import bonda.bonda.domain.book.dto.aladin.BookListDto;
import bonda.bonda.domain.book.dto.request.SaveBookFromAladinReq;
import bonda.bonda.domain.book.dto.response.DeleteSaveBookRes;
import bonda.bonda.domain.book.dto.response.SaveBookRes;
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

    @Transactional
    public SuccessResponse<Message> saveBookFromAladin(SaveBookFromAladinReq request) {
        List<Book> bookList = new ArrayList<>();

        try {
            // 요청 url 설정
            String url = String.format(
                    "%s&ttbkey=%s&CategoryId=%s",
                    aladinConfig.getListBaseUrl(),
                    aladinConfig.getTtbKey(),
                    request.getCategoryId()
            );


            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(response);
            JsonNode items = root.path("item");
            log.info("Url :"+url);

            for (JsonNode item : items) {
                //json 응답 객체 변환
                BookListDto Listdto = new BookListDto();
                Listdto.setPublishDate(LocalDate.parse(item.path("pubDate").asText()));
                Listdto.setImage(item.path("cover").asText());
                Listdto.setPublisher(item.path("publisher").asText());
                Listdto.setTitle(item.path("title").asText());
                Listdto.setWriter(item.path("author").asText());

                //isbn으로 상세 정보 추가 요청
                String isbn = item.path("isbn").asText();

                log.info("title :"+ Listdto.getTitle());
                // 상세 정보 요청 url 설정
                String detailUrl = String.format(
                        "%s&ItemId=%s&ttbkey=%s",
                        aladinConfig.getDetailBaseUrl(),
                        isbn,
                        aladinConfig.getTtbKey()
                );
                log.info("detailUrl :"+detailUrl);
                try {
                    String detailResponse = restTemplate.getForObject(detailUrl, String.class);
                    JsonNode detailRoot = objectMapper.readTree(detailResponse);
                    JsonNode detailItem = detailRoot.path("item").get(0);

                    //json 응답 객체 변환
                    BookDto bookDto = new BookDto();
                    bookDto.setPage(detailItem.path("subInfo").path("itemPage").asInt());
                    bookDto.setWidth(detailItem.path("subInfo").path("packing").path("sizeWidth").asLong());
                    bookDto.setHeight(detailItem.path("subInfo").path("packing").path("sizeHeight").asLong());
                    bookDto.setPage(detailItem.path("subInfo").path("itemPage").asInt());
                    bookDto.setContent(detailItem.path("description").asText());
                    log.info(bookDto.toString());

                    //도서 객체 생성
                    Book book = Book.builder()
                            .image(Listdto.getImage())
                            .title(Listdto.getTitle())
                            .writer(Listdto.getWriter())
                            .publisher(Listdto.getPublisher())
                            .size(String.format("%d * %dmm", bookDto.getWidth(), bookDto.getHeight()))
                            .publishDate(Listdto.getPublishDate())
                            .page(bookDto.getPage())
                            .content(bookDto.getContent())
                            .bookCategory(BookCategory.valueOf(request.getCategory().toUpperCase()))
                            .build();
                    bookList.add(book);

                } catch (Exception e) {
                    log.warn("상세 정보 조회 실패: isbn=" + isbn, e);
                }

            }
            if (!bookList.isEmpty()) {
                bookRepository.saveAll(bookList); // DB에 저장
                return SuccessResponse.of(new Message("도서가 정상적으로 저장되었습니다."));
            } else {
                return SuccessResponse.of(new Message("저장된 도서가 없습니다."));
            }


        } catch (Exception e) {
            log.error("알라딘 API 호출 또는 파싱 실패", e);
            return SuccessResponse.of(new Message("알라딘 API 호출 또는 파싱에 실패했습니다."));

        }


    }

    @Transactional
    public SuccessResponse<SaveBookRes> saveBook(Member member, Long bookId) {
        // 멤버 조회 -> 영속 상태로 조회
        Member persistMember = memberRepository.findByKakaoId(member.getKakaoId()).orElseThrow(() -> new BusinessException(INVALID_MEMBER));
        // 도서 조회
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new BusinessException(INVALID_BOOK_Id));
        // 이미 저장한 도서인지 확인
        if (bookcaseRepository.existsByMemberAndBook(persistMember, book)) {
            throw new BusinessException(ALREADY_SAVED_BOOK);
        }
        // 저장된 도서 생성
        Bookcase bookcase = new Bookcase(persistMember, book);
        // 멤버 저장 도서 수 증가 -> 더티 체크
        persistMember.plusSaveCount();
        bookcaseRepository.save(bookcase);

        return SuccessResponse.of(SaveBookRes.builder()
                .bookId(bookId)
                .isNewBadge(badgeService.checkAndAwardBadges(persistMember, ProgressType.BOOK_SAVE)) //저장에 따른 뱃지 생성
                .message(new Message("도서 저장이 완료되었습니다!")).build());
    }

    @Transactional
    public SuccessResponse<DeleteSaveBookRes> deleteSaveBook(Member member, Long bookId) {
        // 멤버 조회 -> 영속 상태로 조회
        Member persistMember = memberRepository.findByKakaoId(member.getKakaoId()).orElseThrow(() -> new BusinessException(INVALID_MEMBER));
        // 도서 조회
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new BusinessException(INVALID_BOOK_Id));
        // 저장된 도서 조회
        Bookcase bookcase = bookcaseRepository.findByMemberAndBook(persistMember, book).orElseThrow(() -> new BusinessException(SAVED_BOOK_NOT_EXIST));
        bookcaseRepository.delete(bookcase);
        persistMember.minusSaveCount();

        return SuccessResponse.of(DeleteSaveBookRes.builder()
                .bookId(bookId)
                .message(new Message("도서 저장 삭제가 완료되었습니다.")).build());

    }
}
