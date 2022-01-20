package com.example.demo.src.user;

import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;

import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Random;

import static com.example.demo.config.BaseResponseStatus.*;
import org.springframework.transaction.annotation.Transactional;



//문자 알림에 적용
//import java.util.HashMap;
//import java.util.Random;
//import org.json.simple.JSONObject;
//import net.nurigo.java_sdk.api.Message;
//import net.nurigo.java_sdk.exceptions.CoolsmsException;


// Service Create, Update, Delete 의 로직 처리
@Service
public class UserService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserDao userDao;
    private final UserProvider userProvider;
    private final JwtService jwtService;


    @Autowired
    public UserService(UserDao userDao, UserProvider userProvider, JwtService jwtService) {
        this.userDao = userDao;
        this.userProvider = userProvider;
        this.jwtService = jwtService;

    }


//////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 유저 등록 -  createUser() */
    @Transactional
    public PostUserRes createUser(PostUserReq postUserReq) throws BaseException {

        //전화번호 중복 검사
       if(userProvider.checkphoneNumber(postUserReq.getPhoneNumber()) ==1){              //전화번호가 중복이 되면 결과값인 1과 매핑이 되어 중복 여부를 판단 가능
            throw new BaseException(POST_USERS_EXISTS_PHONENUMBER); //"이미 가입된 전화번호 입니다."
        }

        //인증 코드 생성 (4자리 랜덤 숫자)
        Random rand  = new Random();
        String randomSum= "";
        for(int i=0; i<4; i++) {
            String ran = Integer.toString(rand.nextInt(10));
            randomSum+=ran;
        }
        int authCode = Integer.parseInt(randomSum);


        //인증코드 SMS 문자 알림 구현 - 시작
//        try {
//            String api_key = Secret.SMS_ALERT_API_KEY;  //본인의 API KEY
//            String api_secret = Secret.SMS_ALERT_API_SECRET;  //본인의 API SECRET
//            Message coolsms = new Message(api_key, api_secret);
//
//
//            // 4 params(to, from, type, text) are mandatory. must be filled
//            HashMap<String, String> params = new HashMap<String, String>();
//            params.put("to", postUserReq.getPhoneNumber());    // 수신전화번호
//            params.put("from", Secret.SMS_PHONE_NUMBER);    // "발송할 번호 입력" ,  발신전화번호. 테스트시에는 발신,수신 둘다 본인 번호로 하면 됨
//            params.put("type", "SMS");
//            params.put("text", "당근마켓 휴대폰인증 메시지 : 인증번호는" + "[" + authCode + "]" + "입니다.");
//            params.put("app_version", "test app 1.2"); // application name and version
//
//            JSONObject obj = (JSONObject) coolsms.send(params);
//            System.out.println(obj.toString());  //오류가 있나 확인
//
//            if(obj.toString().contains("error_list")){
//                throw new BaseException(POST_USERS_FAIL_ALERT_SMS);  //사용자에게 인증코드 알림 문자를 보내는데 실패하였습니다.
//            }
//
//        } catch (CoolsmsException e) {  //문자 알림 실패시
//            throw new BaseException(POST_USERS_FAIL_ALERT_SMS);  //사용자에게 인증코드 알림 문자를 보내는데 실패하였습니다.
//        }
        //인증코드 SMS 문자 알림 구현 - 끝

        //유저 등록
        try{
            //유저 테이블에서 유저 등록
            PostUserRes postUserRes = userDao.createUser(postUserReq, authCode);
            //return new PostUserRes(userIdx);
            //유저 idx, 인증코드 반환
            return postUserRes;

        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR_CREATE_USER);  //유저 생성 실패 에러
        }


    }




//////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 동네 등록 -  createRegion() */
    @Transactional
    public void createRegion(PostUserReq postUserReq) throws BaseException {

        //동네 등록
        try{
            int regionIdx = userDao.createRegion(postUserReq);  //유저 IDX, 위치, 위도, 경도 전송

        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR_CREATE_REGION);  //동네 등록 실패 에러
        }

    }


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 로그인시 jwt토큰을 DB에 저장 - saveJwt() */
    @Transactional
    public void saveJwt(PostLoginRes postLoginRes) throws BaseException {    //UserController.java에서 객체 값( useridx, jwt)을 받아와서...

        //userDao.java에서 쿼리문 수행결과로 받아온 (0 or 1)값을 result 변수에 저장
        int result = userDao.saveJwt(postLoginRes);

        if(result == 0){
            throw new BaseException(SAVE_FAIL_jwt);   //result 값이 0 (DB에서 userName 값의 수정 실패시) 이면 에러코드 반환
        }
    }





//////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 회원가입 인증 - userJoinCheck() */
    @Transactional
    public PostUserRes JoinAuth(PatchJoinAuthReq patchJoinAuthReq) throws BaseException {


        //폰번호에 해당하는 유저가 존재하는지 확인 (회원가입 여부 확인)
        if (userProvider.JoinCheck(patchJoinAuthReq.getPhoneNumber()) == 0) {    //가입되지 않은 유저입니다.
            throw new BaseException(FAILED_TO_JOIN_CHECK);
        }

        //인증 코드 생성 (4자리 랜덤 숫자)
        Random rand  = new Random();
        String randomSum= "";
        for(int i=0; i<4; i++) {
            String ran = Integer.toString(rand.nextInt(10));
            randomSum+=ran;
        }
        int authCode = Integer.parseInt(randomSum);


        //인증코드 SMS 문자 알림 구현 - 시작
//        try {
//            String api_key = Secret.SMS_ALERT_API_KEY;  //본인의 API KEY
//            String api_secret = Secret.SMS_ALERT_API_SECRET;  //본인의 API SECRET
//            Message coolsms = new Message(api_key, api_secret);
//
//            // 4 params(to, from, type, text) are mandatory. must be filled
//            HashMap<String, String> params = new HashMap<String, String>();
//            params.put("to", patchJoinAuthReq.getPhoneNumber());    // 수신전화번호
//            params.put("from", Secret.SMS_PHONE_NUMBER);    // "발송할 번호 입력" ,  발신전화번호. 테스트시에는 발신,수신 둘다 본인 번호로 하면 됨
//            params.put("type", "SMS");
//            params.put("text", "당근마켓 휴대폰인증 메시지 : 인증번호는 " + "[" + authCode + "]" + "입니다.");
//            params.put("app_version", "test app 1.2"); // application name and version
//
//            JSONObject obj = (JSONObject) coolsms.send(params);
//            System.out.println(obj.toString()); //오류가 있나 확인
//
//            if(obj.toString().contains("error_list")){
//                throw new BaseException(POST_USERS_FAIL_ALERT_SMS);  //사용자에게 인증코드 알림 문자를 보내는데 실패하였습니다.
//            }
//
//        } catch (CoolsmsException e) {  //문자 알림 실패시
//            throw new BaseException(POST_USERS_FAIL_ALERT_SMS);  //사용자에게 인증코드 알림 문자를 보내는데 실패하였습니다.
//        }
        //인증코드 SMS 문자 알림 구현 - 끝


        //authCode DB에 저장 후 userIdx와 함께 반환
        try{
            //인증코드 변경
            int result = userDao.modifyAuthCode(authCode, patchJoinAuthReq.getPhoneNumber());
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_AUTHCODE);   //"인증코드에 해당하는 사용자가 없어서 인증코드를 변경하지 못했습니다."
            }

            //유저 idx 값 조회 (phoneNumber 활용)
            User user = userProvider.getUserIdx(patchJoinAuthReq.getPhoneNumber());

            //유저 idx, 인증코드 반환
            PostUserRes postUserRes =  new PostUserRes(user.getUserIdx(),authCode);
            return postUserRes;


        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR_MODIFY_FAIL_AUTHCODE);   //"인증 코드 변경에 실패했습니다."
        }

    }





/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /* 유저 로그아웃 - logout()  */
    @Transactional
    public void logout(PatchUserReq patchUserReq) throws BaseException {    //UserController.java에서 객체 값( id, nickName)을 받아와서...

        try{
            //유저 로그아웃
            int result = userDao.logout(patchUserReq);
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR_FAIL_LOGOUT);   //"로그아웃에 실패 하였습니다."
        }
    }



///// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 프로필 수정 - modifyInfo()  */
    @Transactional
    public void modifyInfo(PatchUserReq patchUserReq) throws BaseException {    //UserController.java에서 객체 값( id, nickName)을 받아와서...

        //닉네임 중복 검사
        if(userProvider.checkNickName(patchUserReq.getNickName()) ==1){              //닉네임이 중복이 되면 결과값인 1과 매핑이 되어 중복 여부를 판단 가능
            throw new BaseException(POST_USERS_EXISTS_NICKNAME); //"사용중인 닉네임 입니다."
        }

        try{
            //이미지 값 변경
            if(patchUserReq.getImage() != null){
                //이미지 변경
                int result = userDao.modifyImage(patchUserReq);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR_MODIFY_FAIL_USER_IMAGE);   //"사용자 이미지 변경에 실패하였습니다."
        }


        try{
        //닉네임 값 변경
        if(patchUserReq.getNickName() != null){
            //닉네임 정보 변경
            int result = userDao.modifyNickName(patchUserReq);
        }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR_MODIFY_FAIL_USER_NICKNAME);   //"사용자 닉네임 변경에 실패하였습니다."
        }




    }


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 회원탈퇴 (유저 비활성화) - deleteUser()  */
    @Transactional
    public void deleteUser(PatchUserReq patchUserReq) throws BaseException {    //UserController.java에서 객체 값( id, nickName)을 받아와서...

        //회원 탈퇴 여부 확인
        if(userProvider.checkdeleteUser(patchUserReq.getUserIdx()) == 1){      //닉네임이 중복이 되면 결과값인 1과 매핑이 되어 중복 여부를 판단 가능
            throw new BaseException(PATCH_USERS_DELETE_USER);                   //"이미 탈퇴된 계정입니다."
        }

        try{
            //회원 탈퇴
            int result = userDao.deleteUser(patchUserReq);
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR_DELETE_USER);   //'회원 탈퇴(유저 비활성화)에 실패하였습니다.'
        }
    }


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 회원탈퇴 (동네 비활성화) - deleteUser()  */
    @Transactional
    public void deleteRegion(PatchUserReq patchUserReq) throws BaseException {    //UserController.java에서 객체 값( id, nickName)을 받아와서...

        try{
            //동네 비활성화
            int result = userDao.deleteRegion(patchUserReq);

        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR_DELETE_USER_REGION);   //'회원 탈퇴(동네 비활성화)에 실패하였습니다.'
        }
    }




//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 사용자 차단 - blockUser()  */
    @Transactional
    public void blockUser(PostUserBlockReq postUserBlockReq) throws BaseException {    //UserController.java에서 객체 값( id, nickName)을 받아와서...

        //사용자 차단 여부 확인
        if(userProvider.checkBlcokUser(postUserBlockReq) == 1){              //닉네임이 중복이 되면 결과값인 1과 매핑이 되어 중복 여부를 판단 가능
            throw new BaseException(POST_USERS_BLOCKS_NICKNAME);        //"이미 차단한 사용자 입니다."
        }

        try{
            //사용자 차단
            int result = userDao.blockUser(postUserBlockReq);
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR_BLOCK_USER); //"사용자 차단에 실패했습니다. 닉네임을 확인해 주세요."
        }
    }


 /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 사용자 차단 해제 - blockCancell()  */
    @Transactional
    public void blockCancell(PatchUserBlockCancellReq patchUserBlockCancellReq) throws BaseException {    //UserController.java에서 객체 값( id, nickName)을 받아와서...

        try{
            //사용자 차단 해제
            int result = userDao.blockCancell(patchUserBlockCancellReq);
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR_BLOCK_CANCELL_USER); //"사용자 차단 해제에 실패했습니다. 닉네임을 확인해 주세요."
        }
    }



/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 미노출 사용자 추가 - hideUser()  */
    @Transactional
    public void hideUser(PostHideUserReq postHideUserReq) throws BaseException {

        //미노출 사용자 추가 여부 확인
        if(userProvider.checkHideUser(postHideUserReq) == 1){              //닉네임이 중복이 되면 결과값인 1과 매핑이 되어 중복 여부를 판단 가능
            throw new BaseException(POST_USERS_HIDDEN_NICKNAME);        //"이미 미노출 등록된 사용자입니다.."
        }

        try{
            //미노출 사용자 추가
            int result = userDao.hideUser(postHideUserReq);
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR_HIDDEN_USER); //"미노출 사용자 추가에 실패했습니다. 닉네임을 확인해 주세요."
        }

    }



////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 미노출 사용자 취소 - hideUserCancell()  */
    @Transactional
    public void hideUserCancell(PatchHideUserCancellReq patchHideUserCancellReq) throws BaseException {

        try{
            //미노출 사용자 취소
            int result = userDao.hideUserCancell(patchHideUserCancellReq);
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR_HIDDEN_CANCELL_USER); //"미노출 사용자 해제에 실패했습니다. 닉네임을 확인해 주세요."
        }
    }





////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 사용자 신고 - reportUser()  */
    @Transactional
    public void reportUser(PostUserReportReq postUserReportReq) throws BaseException {    //UserController.java에서 객체 값( id, nickName)을 받아와서...

        //사용자 신고 여부 확인
        if(userProvider.checkReportUser(postUserReportReq) == 1){              //중복이 되면 결과값인 1과 매핑이 되어 중복 여부를 판단 가능
            throw new BaseException(POST_USERS_REPORT_NICKNAME);        //"이미 신고한 사용자 입니다."
        }


        //사용자 신고
        try {
            int result = userDao.reportUser(postUserReportReq);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR_REPORT_USER); //"사용자 신고에 실패했습니다."
        }

    }




//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 상품 게시글 신고 - reportProduct()  */
    @Transactional
    public void reportProduct(PostProductReportReq postProductReportReq) throws BaseException {    //UserController.java에서 객체 값( id, nickName)을 받아와서...

        //상품 게시글 신고 여부 확인
        if(userProvider.checkReportProduct(postProductReportReq) == 1){              //중복이 되면 결과값인 1과 매핑이 되어 중복 여부를 판단 가능
            throw new BaseException(POST_USERS_REPORT_PRODUCT);        //"이미 신고한 상품 게시글 입니다."
        }


        //상품 게시글 신고
        try {
            int result = userDao.reportProduct(postProductReportReq);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR_REPORT_PRODUCT); //"상품 게시글 신고에 실패했습니다."
        }

    }














}
