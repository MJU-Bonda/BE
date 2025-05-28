package bonda.bonda.domain.book.application;

import bonda.bonda.domain.book.domain.Book;
import bonda.bonda.domain.book.domain.BookCategory;
import bonda.bonda.domain.book.domain.repository.BookRepository;
import bonda.bonda.domain.book.dto.aladin.BookDto;
import bonda.bonda.domain.book.dto.aladin.BookListDto;
import bonda.bonda.domain.book.dto.request.BookSaveReq;
import bonda.bonda.global.common.Message;
import bonda.bonda.global.common.SuccessResponse;
import bonda.bonda.global.config.AladinConfig;
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

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookCommandServiceImpl implements BookCommandService {
    private final BookRepository bookRepository;
    private final ObjectMapper objectMapper;
    private final AladinConfig aladinConfig;
    private final RestTemplate restTemplate;


    @Transactional
    public SuccessResponse<Message> saveBook(BookSaveReq request) {
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
}
