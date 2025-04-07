package com.example.newsFeed.jwt;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;


@Component
public class TokenUtils {
    private static final String jwtSecretKey = "thisIsASecretKeyUsedForJwtTokenGenerationAndItIsLongEnoughToMeetTheRequirementOf256Bits";
    // jwtSecretKey를 바이트 배열로 변환하고, 이를 사용하여 HMAC-SHA256 알고리즘에 사용할 키를 생성한다.
    private static final Key key = Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    private static final String JWT_TYPE = "JWT";
    private static final String ALGORITHM = "HS256";
    private static final String LOGIN_ID = "loginId";
    private static final String USERNAME = "username";

    public static String generateJwtToken(User user) {

        JwtBuilder builder = Jwts.builder()
                .setHeader(createHeader())                                  // Header 구성
                .setClaims(createClaims(user))                           // Payload - Claims구성
                .setSubject(String.valueOf(user.getId()))              // Payload - Subjects구성
                .setIssuer("profile")                                       // Issuer 구성
                .signWith(key, SignatureAlgorithm.HS256)                    // Signature 구성 : 이 키를 사용하여 JWT 토큰에 서명을 추가한다. 이 서명은 토큰의 무결성을 보장하는 데 사용된다.
                .setExpiration(createExpiredDate());                        // Expired Date 구성

        return builder.compact();
    }
}
