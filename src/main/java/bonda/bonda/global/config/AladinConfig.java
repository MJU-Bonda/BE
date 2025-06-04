package bonda.bonda.global.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Getter
@Setter
@Configuration
public class AladinConfig {

    @Value("${aladin.api.ttbKey}")
    private String ttbKey;
    @Value("${aladin.api.bookListBaseUrl}")
    private String listBaseUrl;
    @Value("${aladin.api.bookDetailBaseUrl}")
    private String detailBaseUrl;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
