package com.example.newsFeed.global.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.dao.DataAccessException;

@AllArgsConstructor
@Getter
public enum Errors {

    // Common
    INVALID_INPUT_VALUE(400, "Bad Request", "C001", "Invalid Input Value"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed", "C002", "Method Not Allowed"),
    ENTITY_NOT_FOUND(400, "Bad Request", "C003", "Entity Not Found"),
    INTERNAL_SERVER_ERROR(500, "Server Error", "C004", "Internal Server Error"),
    INVALID_TYPE_VALUE(400, "Bad Request", "C005", "Invalid Type Value"),
    DB_ERROR(500, "Internal Server Error", "D001", "DB 접근 중 문제가 발생했습니다."),

    // User
    EMAIL_DUPLICATION(400, "Bad Request", "U001", "Email is Duplicated"),
    USER_NOT_FOUND(404, "Not Found", "U002", "User Not Found"),
    INVALID_PASSWORD(400, "Bad Request", "U003", "Invalid Password"),
    UNAUTHORIZED_ACCESS(400, "Bad Request", "U004", "Unauthorized Access"),

    // Board
    BOARD_NOT_FOUND(404, "Not Found", "B001", "게시글이 존재하지 않습니다."),

    // COMMENT
    COMMENT_NOT_FOUND(404, "Not Found", "C001", "Comment Not Found"),

    // Follower
    FOLLOWER_NOT_FOUND(404, "Not Found", "F001", "Follower Not Found"),
    RELATION_NOT_FOUND(404, "Not Found", "F002", "Relations Not Found"),
    BAD_REQUEST_TYPE(400, "Bad Request", "F003", "Bad Request Type"),
    REQUEST_ALREADY_ACCEPTED(400, "Bad Request", "F004", "Request already accepted"),
    REQUEST_ALREADY_EXIST(400, "Bad Request", "F005",  "Request already exist"),
    REQUEST_TO_SELF(400,"Bad Request",  "F006", "Can't request to self"),

    //Likes
    Likes_NOT_FOUND(400, "Bad Request", "F001", "Likes_Not_Found"),

    // TokenRedis
    TOKEN_REDIS_NOT_FOUND(404, "Not Found", "T001", "Token Redis Not Found"),

    // Jwt
    JWT_UNKNOWN(403, "Forbidden", "J000", "알 수 없는 JWT 예외입니다."),
    JWT_SIGNATURE_INVALID(403, "Forbidden", "J001", "JWT 서명이 잘못되었습니다."),
    JWT_MALFORMED(403, "Forbidden", "J002", "형식이 잘못된 JWT입니다."),
    JWT_UNSUPPORTED(403, "Forbidden", "J003", "지원하지 않는 형식의 JWT입니다."),
    JWT_ILLEGAL(403, "Forbidden", "J004", "JWT 형식이 올바르지 않습니다."),
    JWT_INVALID(403, "Forbidden", "J005", "유효하지 않은 JWT입니다."),
    REFRESH_TOKEN_EXPIRED(403, "Forbidden", "J006", "JWT가 만료되었습니다. 재로그인 해주세요.");

    private final int status;
    private final String error;
    private final String code;
    private final String message;

    /**
     * 예외 타입에 따라 대응하는 Errors enum 반환
     */
    public static Errors fromException(Exception e) {
        if (e instanceof SecurityException) return JWT_SIGNATURE_INVALID;
        if (e instanceof MalformedJwtException) return JWT_MALFORMED;
        if (e instanceof UnsupportedJwtException) return JWT_UNSUPPORTED;
        if (e instanceof IllegalArgumentException) return JWT_ILLEGAL;
        if (e instanceof ExpiredJwtException) return REFRESH_TOKEN_EXPIRED;
        if (e instanceof JwtException) return JWT_INVALID;
        if (e instanceof DataAccessException) return DB_ERROR;
        return JWT_UNKNOWN; // 기본값
    }
}
