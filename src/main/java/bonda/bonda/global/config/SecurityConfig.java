package bonda.bonda.global.config;

import bonda.bonda.domain.member.domain.repository.MemberRepository;
import bonda.bonda.global.security.AuthenticationEntryPointImpl;
import bonda.bonda.global.security.filter.JwtAuthenticationProcessingFilter;
import bonda.bonda.global.security.jwt.JwtTokenProvider;
import bonda.bonda.infrastructure.redis.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final RedisUtil redisUtil;

    // api 연동 WHITE_LIST
    private final String[] WHITE_LIST= {
            "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html",    // swagger
            "/auth/**", "/books/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception.authenticationEntryPoint(new AuthenticationEntryPointImpl()))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(WHITE_LIST).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationProcessingFilter(), LogoutFilter.class)
        ;

        return http.build();
    }

    @Bean
    public JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter() {
        return new JwtAuthenticationProcessingFilter(jwtTokenProvider, memberRepository, redisUtil);
    }
}
