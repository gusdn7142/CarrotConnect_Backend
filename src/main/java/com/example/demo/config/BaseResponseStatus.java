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

//////////////////////////////////////////////////////////////////////////////////////////////
    /*
     * 2000 : Request 오류  (validation)
     */

    //user 리소스
    POST_USERS_EMPTY_PHONENUMBER(false, 2000, "전화번호를 입력해주세요."),
    POST_USERS_INVALID_PHONENUMBER(false, 2001, "전화번호 형식을 확인해주세요."),
    POST_lOGINS_EMPTY_AUTHCODE(false, 2002, "인증번호를 입력해주세요."),
    POST_lOGINS_INVALID_AUTHCODE(false, 2003, "인증번호는 네 자리 숫자만 입력 가능합니다."),
    POST_USERS_EMPTY_NICKNAME(false, 2004, "조회할 닉네임을 파라미터에 입력해주세요."),
    POST_USERS_FAIL_ALERT_SMS(false, 2005, "사용자에게 인증코드 알림 문자를 보내는데 실패하였습니다."),

    EMPTY_JWT(false, 2010, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2011, "jWT가 변조되었거나 만료시간이 지났습니다. 다시 확인해 주세요."),
    INVALID_USER_JWT(false,2012,"권한이 없는 유저의 접근입니다."),


    //keyword 리소스
    POST_KEYWORDS_EMPTY_KEYWORD(false, 2050, "키워드를 입력해주세요.(예:자전거)"),
    GET_KEYWORDS_EMPTY_KEYWORD(false, 2051, "설정한 지역과 알림키워드에 해당하는 상품이 없습니다."),



//////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 3000 : Response 오류 (validation)
     */
    //user 리소스
    POST_USERS_EXISTS_PHONENUMBER(false,3000,"이미 가입된 전화번호 입니다."),
    POST_USERS_EXISTS_NICKNAME(false,3001,"사용중인 닉네임 입니다."),
    NOT_EXIST_USER(false,3002,"입력하신 인증코드에 해당하는 사용자가 없습니다."),

    FAILED_TO_JOIN_CHECK(false,3003,"가입되지 않은 전화번호입니다. 가입을 먼저 진행해 주세요."),
    MODIFY_FAIL_AUTHCODE(false,3304,"인증코드에 해당하는 사용자가 없어서 인증코드를 변경하지 못했습니다."),

    LOGOUT_USER_JWT(false, 3005, "로그아웃된 상태입니다."),
    AUTO_LOGOUT_FAIL_USER(false,3006,"아직 유저의 jwt 토큰이 만료되지 않아 자동 로그아웃에 실패했습니다."),

    PATCH_USERS_DELETE_USER(false,3007,"이미 탈퇴된 계정입니다."),
    POST_USERS_BLOCKS_NICKNAME(false,3008,"이미 차단한 사용자입니다."),
    POST_USERS_REPORT_NICKNAME(false,3009,"이미 신고한 사용자입니다."),
    POST_USERS_REPORT_PRODUCT(false,3010,"이미 신고한 상품 게시글 입니다."),
    POST_USERS_HIDDEN_NICKNAME(false,3011,"이미 미노출된 등록된 사용자입니다."),

    //Keyword 리소스
    POST_KEYWORDS_EXISTS_KEYWORD(false,3050,"이미 추가된 키워드에요"),
    PATCH_KEYWORDS_ACTIVE_REGION(false,3051,"이미 알림 동네가 활성화된 상태입니다."),
    PATCH_KEYWORDS_INACTIVE_REGION(false,3052,"이미 알림 동네가 비활성화된 상태입니다."),

    //Gagther 리소스
    POST_KEYWORDS_EXISTS_GATHER(false,3090,"이미 모아보기에 추가된 사용자에요"),

    //townActivity 리소스
    POST_TOWNACTIVITY_EXISTS(false,3130,"이미 작성하셨던 게시글 입니다."),
    POST_TOWN_ACTIVITY_EMPTY_TOPICNAME(false, 3131, "게시글 주제를 입력해주세요."),
    POST_TOWN_ACTIVITY_EMPTY_CONTENT(false, 3132, "게시글 내용을 입력해주세요."),

    //Search 리소스
    POST_SEARCHS_CHECK(false,3170,"이미 등록한 검색어입니다."),


//////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),

    //user 리소스
    DATABASE_ERROR_CREATE_USER(false, 4002, "신규 유저 정보를 DB에 등록하지 못하였습니다."),
    DATABASE_ERROR_CREATE_REGION(false, 4003, "유저의 동네 정보를 DB에 등록하지 못하였습니다."),
    DATABASE_ERROR_CHECK_PHONENUMBER(false, 4004, "전화번호 중복 검사에 실패하였습니다."),
    DATABASE_ERROR_CHECK_NICKNAME(false, 4005, "닉네임 중복 검사에 실패하였습니다."),

    SAVE_FAIL_jwt(false,4006,"JWT 토큰 저장에 실패하였습니다."),
    DATABASE_ERROR_NOT_EXISTS_USER(false, 4007, "사용자 정보를 DB에서 조회하지 못했습니다. "),
    DATABASE_ERROR_MODIFY_FAIL_AUTHCODE(false,4008,"인증 코드 변경에 실패했습니다."),

    DATABASE_ERROR_FAIL_LOGOUT(false, 4009, "로그아웃에 처리에 실패 하였습니다."),
    DATABASE_ERROR_MODIFY_FAIL_USER_NICKNAME(false, 4010, "사용자 닉네임 변경시 오류가 발생하였습니다."),
    DATABASE_ERROR_MODIFY_FAIL_USER_IMAGE(false, 4011, "사용자 이미지 변경시 오류가 발생하였습니다. 이미지 값을 재확인 해주세요."),
    DATABASE_ERROR_USER_INFO(false, 4012, "프로필 조회에 실패하였습니다."),


    DATABASE_ERROR_DELETE_CHECK_USER(false, 4013, "회원탈퇴 여부 확인에 실패하였습니다."),
    DATABASE_ERROR_DELETE_USER(false, 4014, "회원 탈퇴(유저 비활성화)에 실패하였습니다."),
    DATABASE_ERROR_DELETE_USER_REGION(false, 4015, "회원 탈퇴(동네 비활성화)에 실패하였습니다."),

    DATABASE_ERROR_BLOCK_USER(false, 4016, "사용자 차단에 실패했습니다. 닉네임을 확인해 주세요."),
    DATABASE_ERROR_BLOCK_CANCELL_USER(false, 4017, "사용자 차단 해제에 실패했습니다. 닉네임을 확인해 주세요."),

    DATABASE_ERROR_BLOCK_CHECK_USER(false, 4018, "사용자 차단 여부 확인에 실패하였습니다."),
    DATABASE_ERROR_BLOCK_USER_INFO(false, 4019, "차단한 사용자 프로필 조회에 실패하였습니다."),

    DATABASE_ERROR_REPORT_USER(false, 4020, "사용자 신고에 실패했습니다."),
    DATABASE_ERROR_REPORT_CHECK_USER(false, 4021, "사용자 신고 여부 확인에 실패하였습니다."),
    DATABASE_ERROR_REPORT_PRODUCT(false, 4022, "상품 게시글 신고에 실패했습니다."),
    DATABASE_ERROR_REPORT_CHECK_PRODUCT(false, 4023, "상품 게시글 신고 여부 확인에 실패하였습니다."),

    DATABASE_ERROR_HIDDEN_USER(false, 4024, "미노출 사용자 추가에 실패했습니다. 닉네임을 확인해 주세요."),
    DATABASE_ERROR_HIDDEN_CHECK_USER(false, 4025, "미노출 사용자 추가 확인에 실패하였습니다."),
    DATABASE_ERROR_HIDDEN_CANCELL_USER(false, 4026, "미노출 사용자 해제에 실패했습니다. 닉네임을 확인해 주세요."),
    DATABASE_ERROR_HIDDEN_USER_INFO(false, 4027, "미노출 사용자 조회에 실패하였습니다."),

    PASSWORD_ENCRYPTION_ERROR(false, 4028, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4029, "비밀번호 복호화에 실패하였습니다."),




    //keyword 리소스
    DATABASE_ERROR_CREATE_KEYWORD(false, 4080, "키워드를 DB에 등록하지 못하였습니다."),
    DATABASE_ERROR_CHECK_KEYWORD(false, 4081, "키워드 중복 검사에 실패하였습니다."),
    DATABASE_ERROR_DELETE_KEYWORD(false, 4082, "알림 키워드 삭제에 실패하였습니다."),

    DATABASE_ERROR_ACTIVE_REGION_STATUS(false, 4083, "알림 동네 활성화에 실패하였습니다."),
    DATABASE_ERROR_INACTIVE_REGION_STATUS(false, 4084, "알림 동네 비활성화에 실패하였습니다."),
    DATABASE_ERROR_CHECK_ACTIVE_REGION_STATUS(false, 4085, "알림 동네 활성 상태 확인 실패하였습니다."),

    DATABASE_ERROR_GET_ALERT_KEYWORD(false, 4086, "설정한 알림 키워드 조회에 실패하였습니다."),
    DATABASE_ERROR_GET_ALERT_REGION(false, 4087, "알림 설정한 동네 조회에 실패하였습니다."),

    DATABASE_ERROR_GET_ALERT_PRODUCT(false, 4088, "키워드 알림 상품 조회에 실패하였습니다."),


    //gather 리소스
    DATABASE_ERROR_CREATE_GATHER(false, 4120, "모아보기할 사용자를 DB에 등록하지 못하였습니다."),
    DATABASE_ERROR_CHECK_GATHER(false, 4121, "모아보기한 사용자 중복 검사에 실패하였습니다."),
    DATABASE_ERROR_DELETE_GATHER(false, 4122, "모아보기 취소에 실패하였습니다."),

    DATABASE_ERROR_GET_GATHER_USER(false, 4123, "모아보기한 사용자 조회에 실패하였습니다."),
    DATABASE_ERROR_GET_GATHER_PRODUCT(false, 4124, "모아보기한 상품 조회에 실패하였습니다."),



    //MytownActivity 리소스
    DATABASE_ERROR_CREATE_TOWN_ACTIVITY(false, 4180, "동네 생활 게시글을 DB에 등록하지 못하였습니다."),
    DATABASE_ERROR_CREATE_TOWN_ACTIVITY_IMAGE(false, 4181, "동네 생활 게시글 등록 과정에서 이미지를 DB에 등록하지 못하였습니다."),
    DATABASE_ERROR_TOWN_ACTIVITY_INFO(false, 4182, "동네 생활 게시글을 불러오는데 실패하였습니다."),
    DATABASE_ERROR_MODIFY_TOWN_ACTIVITY_IMAGE(false, 4183, "이미지 변경시 오류가 발생하였습니다."),
    DATABASE_ERROR_MODIFY_TOWN_ACTIVITY_CONTENT(false, 4184, "게시글 내용 변경시 오류가 발생하였습니다."),
    DATABASE_ERROR_MODIFY_TOWN_ACTIVITY_TOPICNAME(false, 4185, "게시글 주제 변경시 오류가 발생하였습니다."),
    DATABASE_ERROR_DELETE_TOWN_ACTIVITY(false, 4186, "게시글 삭제시 오류가 발생하였습니다."),
    DATABASE_ERROR_CHECK_TOWN_ACTIVITY(false, 4187, "게시글 중복 검사에 실패하였습니다."),


    //search 리소스
    DATABASE_ERROR_CREATE_SEARCH(false, 4200, "검색어 등록에 실패하였습니다."),
    DATABASE_ERROR_CHECK_SEARCH(false, 4201, "검색어 중복 등록 검사에 실패하였습니다."),
    DATABASE_ERROR_GET_RECENT_SEARCH(false, 4202, "최근 검색어 조회에 실패하였습니다."),
    DATABASE_ERROR_DELETE_RESENT_SEARCH(false, 4203, "최근 검색어 삭제에 실패하였습니다."),
    DATABASE_ERROR_DELETE_ALL_RESENT_SEARCH(false, 4204, "최근 검색어 전체 삭제에 실패하였습니다."),
    DATABASE_ERROR_GET_SEARCH_PRODUCT(false, 4205, "검색에 맞는 중고거래 글을 불러오지 못하였습니다."),
    DATABASE_ERROR_GET_SEARCH_TOWN_ACTIVITY(false, 4206, "검색에 맞는 동네생활 글을 불러오지 못하였습니다."),
    DATABASE_ERROR_GET_SEARCH_USER(false, 4207, "검색에 맞는 사용자들을 불러오지 못하였습니다."),
    DATABASE_ERROR_GET_RELATION_SEARCH(false, 4208, "연관 검색어 조회에 실패하였습니다.");







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
