package com.example.newsFeed.global.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {


    //Custom Exception 처리 - Service 계층에서 발생한 비즈니스 예외 처리
    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponseDto> handleCustomException(CustomException ex) {
        log.error("CustomException: {}", ex.getMessage());
        Errors errors = ex.getErrors();
        ErrorResponseDto response = ErrorResponseDto.of(errors, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrors().getStatus()));
    }

    //Repository(JPA) 계층 예외 처리 - EntityNotFoundException 처리
    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<ErrorResponseDto> handleEntityNotFoundException(EntityNotFoundException ex) {
        log.error("EntityNotFoundException: {}", ex.getMessage());
        ErrorResponseDto response = ErrorResponseDto.of(Errors.ENTITY_NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    //Repository(JPA) 계층 예외 처리 - DataAccessException 처리
    //(SQL 예외, Lock 획득 실패 등 DB 관련 예외)
    @ExceptionHandler(DataAccessException.class)
    protected ResponseEntity<ErrorResponseDto> handleDataAccessException(DataAccessException ex) {
        log.error("DataAccessException: {}", ex.getMessage());
        ErrorResponseDto response = ErrorResponseDto.of(Errors.DB_ERROR, Errors.DB_ERROR.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 요청 데이터 타입과 requestDto의 필드 타입이 맞지 않는 경우
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDto> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.error("HttpMessageNotReadableException: {}", ex.getMessage());
        ErrorResponseDto response = ErrorResponseDto.of(Errors.INVALID_INPUT_VALUE, "요청 본문을 파싱할 수 없습니다.");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    //지원하지 않는 HTTP 메소드 호출 시 발생하는 예외 처리
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponseDto> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        log.error("HttpRequestMethodNotSupportedException: {}", ex.getMessage());
        ErrorResponseDto response = ErrorResponseDto.of(Errors.METHOD_NOT_ALLOWED);
        return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
    }

    // 요청 데이터 타입과 Api 메서드 파라미터 타입이 맞지 않는 경우
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        log.error("MethodArgumentTypeMismatchException: {}", ex.getMessage());
        ErrorResponseDto response = ErrorResponseDto.of(Errors.INVALID_TYPE_VALUE);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // @Valid의 유용성 검증에 해당되어 예외가 발생한 경우
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationException(MethodArgumentNotValidException ex) {
        log.error("MethodArgumentNotValidException: {}", ex.getMessage());
        List<ErrorResponseDto.FieldError> fieldErrors = processFieldErrors(ex.getBindingResult());
        ErrorResponseDto response = ErrorResponseDto.of(Errors.INVALID_INPUT_VALUE, fieldErrors);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // @Validated의 유용성 검증에 해당되어 예외가 발생한 경우
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleConstraintViolationException(ConstraintViolationException ex) {
        log.error("ConstraintViolationException: {}", ex.getMessage());
        ErrorResponseDto response = ErrorResponseDto.of(Errors.INVALID_INPUT_VALUE);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    //필수 요청 파라미터 누락 예외 처리
    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<ErrorResponseDto> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        log.error("MissingServletRequestParameterException: {}", ex.getMessage());
        ErrorResponseDto response = ErrorResponseDto.of(Errors.INVALID_INPUT_VALUE);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    //BindingResult 에서 발생한 필드 에러 목록을 ErrorResponse.FieldError 목록으로 반환
    private List<ErrorResponseDto.FieldError> processFieldErrors(BindingResult bindingResult) {
        List<ErrorResponseDto.FieldError> fieldErrors = new ArrayList<>();

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            ErrorResponseDto.FieldError error = ErrorResponseDto.FieldError.of(
                    fieldError.getField(),
                    fieldError.getRejectedValue() == null ? "" : fieldError.getRejectedValue().toString(),
                    fieldError.getDefaultMessage());
            fieldErrors.add(error);
        }

        return fieldErrors;
    }
}