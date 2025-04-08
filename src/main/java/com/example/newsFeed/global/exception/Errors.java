package com.example.newsFeed.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Errors {

    // Common
    INVALID_INPUT_VALUE(400, "Bad Request", "Invalid Input Value"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed","Method Not Allowed"),
    ENTITY_NOT_FOUND(400, "Bad Request", "Entity Not Found"),
    INTERNAL_SERVER_ERROR(500, "Server Error", "Internal Server Error"),
    INVALID_TYPE_VALUE(400, "Bad Request", "Invalid Type Value"),

    // User
    EMAIL_DUPLICATION(400, "Bad Request", "Email is Duplicated"),
    USER_NOT_FOUND(404, "Not Found", "User Not Found"),
    INVALID_PASSWORD(400, "Bad Request", "Invalid Password"),
    UNAUTHORIZED_ACCESS(400, "Bad Request", "Unauthorized Access"),

    // Board
    SCHEDULE_NOT_FOUND(404, "Not Found", "Board Not Found"),

    // Follower
    COMMENT_NOT_FOUND(404, "Not Found", "Follower Not Found");

    private final int status;
    private final String error;
    private final String message;
}
