package com.dayaeyak.auth.common.constant;

public class AuthValidationMessage {

    // email
    public static final String EMAIL_REG_EXP = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    public static final String INVALID_EMAIL_MESSAGE = "올바른 이메일 형식을 입력해주세요.";


    // password
    public static final String PASSWORD_REG_EXP = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$";
    public static final int PASSWORD_MIN_LENGTH = 8;
    public static final int PASSWORD_MAX_LENGTH = 20;
    public static final String INVALID_PASSWORD_MESSAGE = "비밀번호는 대소문자, 숫자, 특수문자를 포함한 8자 이상 20자 이내여야 합니다.";


    // nickname
    public static final int NICKNAME_MIN_LENGTH = 2;
    public static final int NICKNAME_MAX_LENGTH = 10;
    public static final String INVALID_NICKNAME_MESSAGE = "닉네임은 2자 이상 10자 이내여야 합니다.";


    // age
    public static final String INVALID_AGE_MESSAGE = "나이를 입력해주세요.";


    // phone
    public static final String PHONE_REG_EXP = "^[0-9]{11}$";
    public static final String INVALID_PHONE_MESSAGE = "전화번호는 11자리 숫자로 입력해야 합니다.";


    // role
    public static final String INVALID_ROLE_MESSAGE = "권한을 입력해주세요.";

    // refresh token
    public static final String INVALID_REFRESH_TOKEN_MESSAGE = "유효한 토큰을 입력해주세요.";
}
