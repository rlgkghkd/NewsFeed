package com.example.newsFeed.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;


@Component
public class TokenUtils {

    private static final String jwtSecretKey = "thisIsASecretKeyUsedForJwtTokenGenerationAndItIsLongEnoughToMeetTheRequirementOf256Bits";
    private static final Key key = Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));

    public String createJwt(int userId){
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam("type","jwt") //Header 설정부분
                .claim("userId", userId) //Claim 설정부분
                .setIssuedAt(now) //생성일 설정부분
                .setExpiration(new Date(System.currentTimeMillis()+1*(1000*60*3))) //토큰 만료 시간 설정(3분)
                .signWith(SignatureAlgorithm.HS256, key) //HS256과 JWT_SECRET_KEY로 sign
                .compact(); //토큰 생성
    }

    public String createRefreshToken() {
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam("type", "jwt")
                .setIssuedAt(now)
                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 1)) //1일
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("userId", Integer.class).longValue();
    }
}