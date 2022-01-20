package com.example.demo.src.user;


import com.example.demo.config.BaseException;

import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

//Provider : Read의 비즈니스 로직 처리
@Service
public class UserProvider {

    private final UserDao userDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public UserProvider(UserDao userDao, JwtService jwtService) {
        this.userDao = userDao;
        this.jwtService = jwtService;
    }



/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 전화번호 중복 검사 - checkphoneNumber()  */
    public int checkphoneNumber(String phoneNumber) throws BaseException{
        try{
            return userDao.checkphoneNumber(phoneNumber);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR_CHECK_PHONENUMBER);   //"전화번호 중복 검사에 실패하였습니다."
        }
    }


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 닉네임 중복 검사 - checkNickName()  */
    public int checkNickName(String nickName) throws BaseException{
        try{
            return userDao.checkNickName(nickName);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR_CHECK_NICKNAME);   //"전화번호 중복 검사에 실패하였습니다."
        }
    }


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /* 로그인 진행 (idx와 authCode 받음) */
    public PostLoginRes logIn(PostLoginReq postLoginReq) throws BaseException{

        //useridx와 인증코드 일치 여부 확인 (회원가입이 되어 있는지 확인)
        int result = userDao.checkAuthCode(postLoginReq);
        if (result == 0){
            throw new BaseException(NOT_EXIST_USER);
        }

        //userIdx를 통해 jwt토큰을 발급해 jwt 변수에 저장
        String jwt = jwtService.createJwt(postLoginReq.getUserIdx());

        //userIdx와 jwt를 리턴
        return new PostLoginRes(postLoginReq.getUserIdx(),jwt);

    }

//////////////////////////////////////////////////////////////////////////////////////
    /* 폰번호에 해당하는 유저가 존재하는지 확인 - JoinCheck() */
    public int JoinCheck(String phoneNumber) throws BaseException{
        try{
            return userDao.JoinCheck(phoneNumber);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR_NOT_EXISTS_USER);     //사용자 정보를 DB에서 조회하지 못했습니다.
        }
    }



/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 유저 idx 값 조회 - getUserIdx() */
    public User getUserIdx(String phoneNumber) throws BaseException {
        try {
            User user = userDao.getUserIdx(phoneNumber);
            return user;
        } catch (Exception exception) {    //에러가 있다면 (의미적 validation 처리)
            throw new BaseException(DATABASE_ERROR_NOT_EXISTS_USER);   //"사용자 정보를 DB에서 조회하지 못했습니다. "
        }
    }


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 로그아웃된 유저 (만료된 토큰 접근)인지 확인 */
    public void checkByUser(String jwt) throws BaseException{

        int checkNum = userDao.checkByUser(jwt);
        if(checkNum == 1){
            throw new BaseException(LOGOUT_USER_JWT);
        }
    }


////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 프로필 조회 - getUserProfile() */
    public GetUserRes getUserProfile(String nickName) throws BaseException {   //UserComtroller.java에서 userIdx값을 받아옴.
        try {
            GetUserRes getUserRes = userDao.getUserProfile(nickName);  //userDao.getUser()에게 userIdx값을 그대로 넘겨줌
            return getUserRes;
        } catch (Exception exception) {    //에러가 있다면 (의미적 validation 처리)
            throw new BaseException(DATABASE_ERROR_USER_INFO);
        }
    }


/////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 내 프로필 조회 - getMyProfile() */
    public GetUserRes getMyProfile(int userIdx) throws BaseException {   //UserComtroller.java에서 userIdx값을 받아옴.
        try {
            GetUserRes getUserRes = userDao.getMyProfile(userIdx);  //userDao.getUser()에게 userIdx값을 그대로 넘겨줌
            return getUserRes;
        } catch (Exception exception) {    //에러가 있다면 (의미적 validation 처리)
            throw new BaseException(DATABASE_ERROR_USER_INFO);
        }
    }



//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 사용자 차단 여부 확인 - checkBlcokUser()  */
    public int checkBlcokUser(PostUserBlockReq postUserBlockReq) throws BaseException{
        try{
            return userDao.checkBlcokUser(postUserBlockReq);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR_BLOCK_CHECK_USER);   //"사용자 차단 여부 확인에 실패하였습니다."
        }
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////
//    /* 사용자 차단 해제 여부 확인 - checkBlcokCancellUser()  */
//    public int checkBlcokCancellUser(PatchUserBlockCancellReq patchUserBlockCancellReq) throws BaseException{
//        try{
//            return userDao.checkBlcokCancellUser(patchUserBlockCancellReq);
//        } catch (Exception exception){
//            throw new BaseException(DATABASE_ERROR_BLOCK_CANCELL_CHECK_USER);   //"사용자 차단 해제 여부 확인에 실패하였습니다."
//        }
//    }


/////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 차단한 사용자 정보 조회 - getBlockUser() */
    public List<GetUserBlockRes> getBlockUser(int userIdx) throws BaseException {
        try {
            List<GetUserBlockRes> getUserBlockRes = userDao.getBlockUser(userIdx);
            return getUserBlockRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR_BLOCK_USER_INFO);   //"차단한 사용자 프로필 조회에 실패하였습니다."
        }
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 회원 탈퇴 여부 확인 - checkdeleteUser()  */
    public int checkdeleteUser(int useIdx) throws BaseException{
        try{
            return userDao.checkdeleteUser(useIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR_DELETE_CHECK_USER);   //"회원탈퇴 여부 확인에 실패하였습니다."
        }


    }


///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 미노출 사용자 추가 여부 확인 - checkBlcokUser()  */
    public int checkHideUser(PostHideUserReq postHideUserReq) throws BaseException{
        try{
            return userDao.checkHideUser(postHideUserReq);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR_HIDDEN_CHECK_USER);   //"미노출 사용자 추가 확인에 실패하였습니다."
        }
    }



//////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 미노출 사용자 정보 조회 - getHiddenUser() */
    public List<GetHiddenUserRes> getHiddenUser(int userIdx) throws BaseException {
        try {
            List<GetHiddenUserRes> getHiddenUserRes = userDao.getHiddenUser(userIdx);
            return getHiddenUserRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR_HIDDEN_USER_INFO);   //"미노출 사용자 조회에 실패하였습니다."
        }
    }


////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 사용자 신고 여부 확인 - checkReportUser()  */
    public int checkReportUser(PostUserReportReq postUserReportReq) throws BaseException{
        try{
            return userDao.checkReportUser(postUserReportReq);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR_REPORT_CHECK_USER);   //"사용자 신고 여부 확인에 실패하였습니다."
        }
    }


//////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 게시글 신고 여부 확인 - checkReportProduct()  */
    public int checkReportProduct(PostProductReportReq postProductReportReq) throws BaseException{
        try{
            return userDao.checkReportProduct(postProductReportReq);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR_REPORT_CHECK_PRODUCT);   //"상품 게시글 신고 여부 확인에 실패하였습니다."
        }
    }


















}
