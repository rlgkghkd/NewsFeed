package com.example.newsFeed.global.exception;

import lombok.Getter;

@Getter
//enum class Errors를 필드로 하고 있고 RuntimeException을 상속 받은 CustomException
public class CustomException extends RuntimeException {

    // Errors는 예외 유형을 정의한 enum 클래스 (예: UNAUTHORIZED_ACCESS, NOT_FOUND 등)
    private final Errors errors;

    /**
     * Errors enum만 전달받아 예외를 생성하는 생성자
     * super(errors.getMessage())를 호출해 예외 메시지를 RuntimeException에 전달
     * @param errors 정의된 에러 타입 (예: Errors.UNAUTHORIZED_ACCESS)
     */
    public CustomException(Errors errors) {
        // RuntimeException의 message 필드 설정, 매개변수에 대응하는 enum에 정의된  message를 오버라이딩
        super(errors.getMessage());
        this.errors = errors;
    }

    /**
     * Errors enum과 사용자 정의 메시지를 함께 전달받아 예외를 생성하는 생성자
     * @param errors 정의된 에러 타입
     * @param message 커스텀 메시지 (오버라이딩용)
     */
    public CustomException(Errors errors, String message) {
        super(message); // 개별로 정의된 메시지로 설정
        this.errors = errors;
    }
}
