package bonda.bonda.global.security.jwt;

import bonda.bonda.domain.member.domain.Member;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Optional;

public interface JwtTokenProvider {

    String createAccessToken(Member member);

    String createRefreshToken();

    void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken);

    void sendAccessToken(HttpServletResponse response, String accessToken);

    Optional<String> extractAccessToken(HttpServletRequest request);

    Optional<String> extractRefreshToken(HttpServletRequest request);

    Optional<Long> extractMemberId(String accessToken);

    void setAccessTokenHeader(HttpServletResponse response, String accessToken);

    void setRefreshTokenHeader(HttpServletResponse response, String refreshToken);

    boolean isTokenValid(String token);

    boolean isTokenInBlackList(String accessToken);
}