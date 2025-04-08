package com.example.newsFeed.validation;

import java.util.regex.Pattern;

public class PasswordValidator {
    // 1. 정규 표현식 패턴 선언
    // 대소문자 포함 영문 + 숫자 + 특수문자를 최소 1글자씩 포함이라는 조건에서
    // 대소문자 포함을 "대문자 또는 소문자 중 하나 이상만 있으면 된다"로 해석할 때의 경우로 설정함
    private static final String PASSWORD_PATTERN =
            "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()\\-+=]).{8,}$";
    // 2. 패턴 컴파일
    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
    // 3. isValid 메서드에서 유효성 검사 수행
    public static boolean isValid(String password) {
        if (password == null) {
            return false;
        }
        return pattern.matcher(password).matches();
    }
}
