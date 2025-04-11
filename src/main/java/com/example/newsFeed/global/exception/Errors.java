package com.example.newsFeed.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Errors {

    // Common
    INVALID_INPUT_VALUE(400, "Bad Request", "Invalid Input Value"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed", "Method Not Allowed"),
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

    // COMMENT
    COMMENT_NOT_FOUND(404, "Not Found", "Comment Not Found"),

    // Follower
    FOLLOWER_NOT_FOUND(404, "Not Found", "Follower Not Found"),
    RELATION_NOT_FOUND(404, "Not Found", "Relations Not Found"),
    BAD_REQUEST_TYPE(400, "Bad Request", "Bad Request Type"),
    REQUEST_ALREADY_ACCEPTED(400, "Bad Request", "Request already accepted"),
    REQUEST_ALREADY_EXIST(400, "Bad Request", "Request already exist"),
    REQUEST_TO_SELF(400, "Bad Request", "Can't request to self"),

    //Likes
    Likes_NOT_FOUND(400, "Bad Request", "Likes_Not_Found");

    private final int status;
    private final String error;
    private final String message;
}
