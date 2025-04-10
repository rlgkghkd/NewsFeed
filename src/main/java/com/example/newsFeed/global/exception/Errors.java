package com.example.newsFeed.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Errors {

    // Common
    INVALID_INPUT_VALUE(400, "Bad Request", "C001", "Invalid Input Value"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed", "C002", "Method Not Allowed"),
    ENTITY_NOT_FOUND(400, "Bad Request", "C003", "Entity Not Found"),
    INTERNAL_SERVER_ERROR(500, "Server Error", "C004", "Internal Server Error"),
    INVALID_TYPE_VALUE(400, "Bad Request", "C005", "Invalid Type Value"),

    // User
    EMAIL_DUPLICATION(400, "Bad Request", "U001", "Email is Duplicated"),
    USER_NOT_FOUND(404, "Not Found", "U002", "User Not Found"),
    INVALID_PASSWORD(400, "Bad Request", "U003", "Invalid Password"),
    UNAUTHORIZED_ACCESS(400, "Bad Request", "U004", "Unauthorized Access"),
    ACCESS_TOKEN_EXPIRATION(400, "Bad Request", "U005", "AccessToken is expired"),

    // Board
    BOARD_NOT_FOUND(404, "Not Found", "B001", "게시글이 존재하지 않습니다."),

    // Follower
    FOLLOWER_NOT_FOUND(404, "Not Found", "F001", "Follower Not Found"),


    // TokenRedis
    TOKEN_REDIS_NOT_FOUND(404, "Not Found", "T001", "Token Redis Not Found");
    private final int status;
    private final String error;
    private final String code;
    private final String message;
}
