package bonda.bonda.domain.auth.application;

import bonda.bonda.domain.member.dto.response.KakaoMemberRes;
import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.security.interfaces.RSAPublicKey;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoTokenValidator {

    public KakaoMemberRes validateAndExtract(String idToken) {
        try {
            JwkProvider jwkProvider = new UrlJwkProvider(new URL("https://kauth.kakao.com/.well-known/jwks.json"));
            DecodedJWT decodedJWT = JWT.decode(idToken);

            Jwk jwk = jwkProvider.get(decodedJWT.getKeyId());
            Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null);

            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(idToken);

            String sub = decodedJWT.getClaim("sub").asString();

            return new KakaoMemberRes(sub);
        } catch (Exception e) {
            throw new RuntimeException("유효하지 않은 kakao_idToken 입니다." + e.getMessage());
        }
    }
}
