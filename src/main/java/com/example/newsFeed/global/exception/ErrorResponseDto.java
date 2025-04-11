package com.example.newsFeed.global.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)// null 필드는 JSON 응답에 포함되지 않음

/**
 * 클라이언트에게 에러 응답을 전달할 때 사용되는 DTO 클래스
 * 에러의 상세 정보, 상태 코드, 메시지 등을 포함한다.
 */
public class ErrorResponseDto {

    // 에러 발생 시간
    private final LocalDateTime timestamp = LocalDateTime.now();
    // HTTP 상태 코드 (예: 400, 404, 500 등)
    private final int status;
    // 상태에 해당하는 설명 (예: "BAD_REQUEST")
    private final String error;
    // 프로젝트 내부에서 정의한 에러 코드 (예: "U001")
    private final String code;
    // 에러 메시지
    private final String message;

    // 유효성 검사 실패 등의 경우, 필드 에러들을 포함 (없으면 제외됨)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final List<FieldError> fieldErrors;

    //일반적인 에러 응답을 생성
    public static ErrorResponseDto of(Errors errors) {
        return ErrorResponseDto.builder()
                .status(errors.getStatus())
                .error(errors.getError())
                .code(errors.getCode())
                .message(errors.getMessage())
                .fieldErrors(new ArrayList<>())// 기본적으로 빈 리스트
                .build();
    }

    //에러 메시지를 커스터마이징하여 에러 응답을 생성
    public static ErrorResponseDto of(Errors errors, String message) {
        return ErrorResponseDto.builder()
                .status(errors.getStatus())
                .error(errors.getError())
                .code(errors.getCode())
                .message(message)
                .fieldErrors(new ArrayList<>())
                .build();
    }

    //필드 유효성 에러를 포함한 에러 응답 생성
    public static ErrorResponseDto of(Errors errors, List<FieldError> fieldErrors) {
        return ErrorResponseDto.builder()
                .status(errors.getStatus())
                .error(errors.getError())
                .code(errors.getCode())
                .message(errors.getMessage())
                .fieldErrors(fieldErrors)
                .build();
    }

    @Getter
    @Builder
    //개별 필드 에러 정보를 담는 내부 static 클래스
    public static class FieldError {
        private String field; // 문제가 발생한 필드명
        private String value; // 입력된 잘못된 값
        private String reason; // 왜 잘못되었는지에 대한 설명

        //FieldError 생성 메서드
        public static FieldError of(String field, String value, String reason) {
            return FieldError.builder()
                    .field(field)
                    .value(value)
                    .reason(reason)
                    .build();
        }
    }
}
