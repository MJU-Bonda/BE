package bonda.bonda.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    // api 연동 WHITE_LIST
    private final String[] WHITE_LIST= { "/auth/**","/books/**" };

}
