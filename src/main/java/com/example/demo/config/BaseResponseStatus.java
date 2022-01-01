package com.example.demo.config;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),


    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,2003,"권한이 없는 유저의 접근입니다."),
    INVALID_PHONENUM(false,2004,"존재하지 않는 전화번호입니다."),

    // users
    USERS_EMPTY_USER_ID(false, 2010, "유저 아이디 값을 확인해주세요."),

    // [POST] /users
    POST_USERS_EMPTY_EMAIL(false, 2015, "이메일을 입력해주세요."),
    POST_USERS_INVALID_EMAIL(false, 2016, "이메일 형식을 확인해주세요."),
    POST_USERS_EXISTS_EMAIL(false,2017,"중복된 이메일입니다."),

    POST_USERS_EMPTY_NICKNAME(false,2018,"닉네임을 입력해주세요"),
    POST_USERS_INVALID_PHONENUM(false,2019,"전화번호 형식을 확인해주세요"),
    POST_USERS_EXISTS_NICKNAME(false,2020,"중복된 닉네임입니다."),
    POST_USERS_EMPTY_PHONENUM(false,2021,"전화번호를 입력해주세요"),
    POST_USERS_EMPTY_PROFILEIMG(false,2022,"프로필사진을 올려주세요"),
    POST_PRODUCT_EMPTY_PRODUCTNAME(false,2023,"상품이름을 입력해주세요"),
    POST_PRODUCT__ALREADY_REGISTERED(false,2024,"이미 등록된 상품입니다"),
    POST_PRODUCT_EMPTY_SENDCHAT(false,2025, "보낼 채팅메시지를 입력해주세요"),
    POST_PRODUCT_EMPTY_PRODUCTID(false,2026,"좋아요누를 상품 인덱스를 입력해주세요"),

    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    // [POST] /users
    DUPLICATED_EMAIL(false, 3013, "중복된 이메일입니다."),
    FAILED_TO_LOGIN(false,3014,"비밀번호가 틀렸습니다."),
    EMPTY_EMAIL(false,3015,"존재하지 않는 이메일입니다!"),
    EMPTY_ADDRESS(false,3016,"주소를 입력해주세요"),



    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),

    //[PATCH] /users/{userIdx}
    MODIFY_FAIL_PROFILEIMG(false, 4009,"프로필사진 수정, 추가 실패하였습니다."),
    MODIFY_FAIL_USEREMAIL(false, 4010,"유저이메일 수정, 추가 실패"),


    PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다."),
    AHTOURNUM_ENCRYPTION_ERROR(false,4013,"인증번호 암호화에 실패하였습니다."),
    MODIFY_FAIL_USERNAME(false,4014,"유저네임 수정 실패"),
    AUTH_DECRYPTION_ERROR(false,4015,"인증번호 복호화에 실패하였습니다"),
    CHANGE_FAIL_ADDRESS(false,4016,"주소 변경에 실패하였습니다.");

    // 5000 : 필요시 만들어서 쓰세요
    // 6000 : 필요시 만들어서 쓰세요


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) { //BaseResponseStatus 에서 각 해당하는 코드를 생성자로 맵핑
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
