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


    /*
     * 2000 : Request 오류  (validation)
     */

    //user 리소스
    POST_USERS_EMPTY_PHONENUMBER(false, 2015, "전화번호를 입력해주세요."),
    POST_USERS_INVALID_PHONENUMBER(false, 2016, "전화번호 형식을 확인해주세요."),
    POST_lOGINS_EMPTY_AUTHCODE(false, 2015, "인증번호를 입력해주세요."),
    POST_lOGINS_INVALID_AUTHCODE(false, 2016, "인증번호는 네 자리 숫자만 입력 가능합니다."),





    //    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,2003,"권한이 없는 유저의 접근입니다."),




    /**
     * 3000 : Response 오류 (validation)
     */
    //user 리소스
    POST_USERS_EXISTS_PHONENUMBER(false,3001,"이미 가입된 전화번호 입니다."),
    NOT_EXIST_USER(false,3101,"입력하신 인증코드에 해당하는 사용자가 없습니다."),

//    FAILED_TO_LOGIN(false,5100,"로그인에 실패하였습니다."),  //없어도 될듯???
    FAILED_TO_JOIN_CHECK(false,3002,"가입되지 않은 전화번호입니다. 가입을 먼저 진행해 주세요."),
    MODIFY_FAIL_AUTHCODE(false,3304,"인증코드에 해당하는 사용자가 없어서 인증코드를 변경하지 못했습니다."),



//    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),
//
//    // [POST] /users
//    DUPLICATED_EMAIL(false, 3013, "중복된 이메일입니다."),
//    FAILED_TO_LOGIN(false,3014,"없는 아이디거나 비밀번호가 틀렸습니다."),






    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),

    //user 리소스
    DATABASE_ERROR_CREATE_USER(false, 4000, "신규 유저 정보를 DB에 등록하지 못하였습니다."),
    DATABASE_ERROR_CREATE_REGION(false, 4001, "유저의 동네 정보를 DB에 등록하지 못하였습니다."),
    DATABASE_ERROR_CHECK_PHONENUMBER(false, 4002, "전화번호 중복 검사에 실패하였습니다."),

    SAVE_FAIL_jwt(false,5999,"JWT 토큰 저장에 실패하였습니다."),
    DATABASE_ERROR_NOT_EXISTS_USER(false, 4002, "사용자 정보를 DB에서 조회하지 못했습니다. "),
    DATABASE_ERROR_MODIFY_FAIL_AUTHCODE(false,3304,"인증 코드 변경에 실패했습니다."),








//    //[PATCH] /users/{userIdx}
//    MODIFY_FAIL_USERNAME(false,4014,"유저네임 수정 실패"),
//
    PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다.");









    // 5000 : 필요시 만들어서 쓰세요
    // 6000 : 필요시 만들어서 쓰세요


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
