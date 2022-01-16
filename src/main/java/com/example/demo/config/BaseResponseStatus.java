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
    POST_USERS_EMPTY_NICKNAME(false, 2015, "조회할 닉네임을 파라미터에 입력해주세요."),




    //    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, "jWT가 변조되었거나 만료시간이 지났습니다. 다시 확인해 주세요."),
    INVALID_USER_JWT(false,2003,"권한이 없는 유저의 접근입니다."),





    //keyword 리소스
    POST_KEYWORDS_EMPTY_KEYWORD(false, 2015, "키워드를 입력해주세요.(예:자전거)"),
    GET_KEYWORDS_EMPTY_KEYWORD(false, 2015, "설정한 지역과 알림키워드에 해당하는 상품이 없습니다."),



    /**
     * 3000 : Response 오류 (validation)
     */
    //user 리소스
    POST_USERS_EXISTS_PHONENUMBER(false,3001,"이미 가입된 전화번호 입니다."),
    POST_USERS_EXISTS_NICKNAME(false,3001,"사용중인 닉네임 입니다."),
    NOT_EXIST_USER(false,3101,"입력하신 인증코드에 해당하는 사용자가 없습니다."),

//    FAILED_TO_LOGIN(false,5100,"로그인에 실패하였습니다."),  //없어도 될듯???
    FAILED_TO_JOIN_CHECK(false,3002,"가입되지 않은 전화번호입니다. 가입을 먼저 진행해 주세요."),
    MODIFY_FAIL_AUTHCODE(false,3304,"인증코드에 해당하는 사용자가 없어서 인증코드를 변경하지 못했습니다."),
//    logout_FAIL_USER(false,3111,"이미 로그아웃 되었습니다."), 삭제예정~~~

    LOGOUT_USER_JWT(false, 3103, "로그아웃된 상태입니다."),
//    FAILED_TO_DELETE_USER(false,4110,"탈퇴할 사용자가 존재하지 않습니다."), 삭제예정

    AUTO_LOGOUT_FAIL_USER(false,3112,"아직 유저의 jwt 토큰이 만료되지 않아 자동 로그아웃에 실패했습니다."),



    PATCH_USERS_DELETE_USER(false,3008,"이미 탈퇴된 계정입니다."),

    POST_USERS_BLOCKS_NICKNAME(false,3008,"이미 차단한 사용자입니다."),
    POST_USERS_REPORT_NICKNAME(false,3008,"이미 신고한 사용자입니다."),


    POST_USERS_REPORT_PRODUCT(false,3008,"이미 신고한 상품 게시글 입니다."),


//    PATCH_USERS_BLOCKS_CANCELL_NICKNAME(false,3008,"이미 차단 해제한 사용자입니다."), 지울 예정



//    MODIFY_FAIL_NICKNAME(false,5102,"닉네임을 변경할 수 없습니다."),
//    MODIFY_FAIL_IMAGE(false,5102,"이미지를 변경 과정에 오류가 "),


//    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),
//
//    // [POST] /users
//    DUPLICATED_EMAIL(false, 3013, "중복된 이메일입니다."),
//    FAILED_TO_LOGIN(false,3014,"없는 아이디거나 비밀번호가 틀렸습니다."),



    //Keyword 리소스
    POST_KEYWORDS_EXISTS_KEYWORD(false,3001,"이미 추가된 키워드에요"),
//    PATCH_KEYWORDS_DELETE_KEYWORD(false,3008,"이미 삭제된 키워드입니다."),  삭제  예정

    PATCH_KEYWORDS_ACTIVE_REGION(false,3008,"이미 알림 동네가 활성화된 상태입니다."),
    PATCH_KEYWORDS_INACTIVE_REGION(false,3008,"이미 알림 동네가 비활성화된 상태입니다."),


    //Gagther 리소스
    POST_KEYWORDS_EXISTS_GATHER(false,3001,"이미 모아보기에 추가된 사용자에요"),


    //Hidden 리소스
    POST_USERS_HIDDEN_NICKNAME(false,3008,"이미 미노출된 등록된 사용자입니다."),



    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),

    //user 리소스
    DATABASE_ERROR_CREATE_USER(false, 4000, "신규 유저 정보를 DB에 등록하지 못하였습니다."),
    DATABASE_ERROR_CREATE_REGION(false, 4001, "유저의 동네 정보를 DB에 등록하지 못하였습니다."),
    DATABASE_ERROR_CHECK_PHONENUMBER(false, 4002, "전화번호 중복 검사에 실패하였습니다."),
    DATABASE_ERROR_CHECK_NICKNAME(false, 4002, "닉네임 중복 검사에 실패하였습니다."),

    SAVE_FAIL_jwt(false,5999,"JWT 토큰 저장에 실패하였습니다."),
    DATABASE_ERROR_NOT_EXISTS_USER(false, 4002, "사용자 정보를 DB에서 조회하지 못했습니다. "),
    DATABASE_ERROR_MODIFY_FAIL_AUTHCODE(false,3304,"인증 코드 변경에 실패했습니다."),

    DATABASE_ERROR_FAIL_LOGOUT(false, 4002, "로그아웃에 처리에 실패 하였습니다."),
    DATABASE_ERROR_MODIFY_FAIL_USER_NICKNAME(false, 4002, "사용자 닉네임 변경시 오류가 발생하였습니다."),
    DATABASE_ERROR_MODIFY_FAIL_USER_IMAGE(false, 4002, "사용자 이미지 변경시 오류가 발생하였습니다. 이미지 값을 재확인 해주세요."),


    DATABASE_ERROR_USER_INFO(false, 4242, "프로필 조회에 실패하였습니다."),


    DATABASE_ERROR_DELETE_CHECK_USER(false, 4242, "회원탈퇴 여부 확인에 실패하였습니다."),
    DATABASE_ERROR_DELETE_USER(false, 4242, "회원 탈퇴(유저 비활성화)에 실패하였습니다."),
    DATABASE_ERROR_DELETE_USER_REGION(false, 4242, "회원 탈퇴(동네 비활성화)에 실패하였습니다."),

    DATABASE_ERROR_BLOCK_USER(false, 4242, "사용자 차단에 실패했습니다. 닉네임을 확인해 주세요."),
    DATABASE_ERROR_BLOCK_CANCELL_USER(false, 4242, "사용자 차단 해제에 실패했습니다. 닉네임을 확인해 주세요."),

    DATABASE_ERROR_BLOCK_CHECK_USER(false, 4242, "사용자 차단 여부 확인에 실패하였습니다."),
//    DATABASE_ERROR_BLOCK_CANCELL_CHECK_USER(false, 4242, "사용자 차단 해제 여부 확인에 실패하였습니다."), 지울 예정
    DATABASE_ERROR_BLOCK_USER_INFO(false, 4242, "차단한 사용자 프로필 조회에 실패하였습니다."),

    DATABASE_ERROR_REPORT_USER(false, 4242, "사용자 신고에 실패했습니다."),
    DATABASE_ERROR_REPORT_CHECK_USER(false, 4242, "사용자 신고 여부 확인에 실패하였습니다."),
    DATABASE_ERROR_REPORT_PRODUCT(false, 4242, "상품 게시글 신고에 실패했습니다."),
    DATABASE_ERROR_REPORT_CHECK_PRODUCT(false, 4242, "상품 게시글 신고 여부 확인에 실패하였습니다."),



//    //[PATCH] /users/{userIdx}
//    MODIFY_FAIL_USERNAME(false,4014,"유저네임 수정 실패"),
//
    PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다."),





    //keyword 리소스
    DATABASE_ERROR_CREATE_KEYWORD(false, 4000, "키워드를 DB에 등록하지 못하였습니다."),
    DATABASE_ERROR_CHECK_KEYWORD(false, 4002, "키워드 중복 검사에 실패하였습니다."),
    DATABASE_ERROR_DELETE_KEYWORD(false, 4242, "알림 키워드 삭제에 실패하였습니다."),
//    DATABASE_ERROR_CHECK_DELETE_KEYWORD(false, 4002, "키워드 삭제 여부 확인에 실패하였습니다."),   삭제  예정

    DATABASE_ERROR_ACTIVE_REGION_STATUS(false, 4242, "알림 동네 활성화에 실패하였습니다."),
    DATABASE_ERROR_INACTIVE_REGION_STATUS(false, 4242, "알림 동네 비활성화에 실패하였습니다."),
    DATABASE_ERROR_CHECK_ACTIVE_REGION_STATUS(false, 4242, "알림 동네 활성 상태 확인 실패하였습니다."),

    DATABASE_ERROR_GET_ALERT_KEYWORD(false, 4242, "설정한 알림 키워드 조회에 실패하였습니다."),
    DATABASE_ERROR_GET_ALERT_REGION(false, 4242, "알림 설정한 동네 조회에 실패하였습니다."),

    DATABASE_ERROR_GET_ALERT_PRODUCT(false, 4242, "키워드 알림 상품 조회에 실패하였습니다."),



    //gather 리소스
    DATABASE_ERROR_CREATE_GATHER(false, 4000, "모아보기할 사용자를 DB에 등록하지 못하였습니다."),
    DATABASE_ERROR_CHECK_GATHER(false, 4002, "모아보기한 사용자 중복 검사에 실패하였습니다."),
    DATABASE_ERROR_DELETE_GATHER(false, 4242, "모아보기 취소에 실패하였습니다."),

    DATABASE_ERROR_GET_GATHER_USER(false, 4242, "모아보기한 사용자 조회에 실패하였습니다."),
    DATABASE_ERROR_GET_GATHER_PRODUCT(false, 4242, "모아보기한 상품 조회에 실패하였습니다."),


    //hidden 리소스
    DATABASE_ERROR_HIDDEN_USER(false, 4242, "미노출 사용자 추가에 실패했습니다. 닉네임을 확인해 주세요."),
    DATABASE_ERROR_HIDDEN_CHECK_USER(false, 4242, "미노출 사용자 추가 확인에 실패하였습니다."),
    DATABASE_ERROR_HIDDEN_CANCELL_USER(false, 4242, "미노출 사용자 해제에 실패했습니다. 닉네임을 확인해 주세요."),
    DATABASE_ERROR_HIDDEN_USER_INFO(false, 4242, "미노출 사용자 조회에 실패하였습니다.");





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
