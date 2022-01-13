package com.example.demo.src.user;



import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.AES128;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

import static com.example.demo.config.BaseResponseStatus.*;

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
    public PostUserRes createUser(PostUserReq postUserReq) throws BaseException {

        //전화번호 중복 검사
       if(userProvider.checkphoneNumber(postUserReq.getPhoneNumber()) ==1){              //닉네임이 중복이 되면 결과값인 1과 매핑이 되어 중복 여부를 판단 가능
            throw new BaseException(POST_USERS_EXISTS_PHONENUMBER); //"이미 가입된 전화번호 입니다."
        }


        //유저 등록
        try{
            //유저 테이블에서 유저 등록
            PostUserRes postUserRes = userDao.createUser(postUserReq);
            //return new PostUserRes(userIdx);

            //유저 idx, 인증코드 반환
            return postUserRes;

        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR_CREATE_USER);  //유저 생성 실패 에러
        }
    }



//////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 동네 등록 -  createRegion() */
    public void createRegion(PostUserReq postUserReq) throws BaseException {

        //동네 등록
        try{
            int regionIdx = userDao.createRegion(postUserReq);  //유저 IDX, 위치, 위도, 경도 전송
//            postUserRes.setRegionIdx(regionIdx);  //지역 idx 출력시키기 위해 추가

            //지역 idx
//            return postUserRes;

        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR_CREATE_REGION);  //동네 등록 실패 에러
        }

    }


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 로그인시 jwt토큰을 DB에 저장 - saveJwt() */
    public void saveJwt(PostLoginRes postLoginRes) throws BaseException {    //UserController.java에서 객체 값( useridx, jwt)을 받아와서...

        //userDao.java에서 쿼리문 수행결과로 받아온 (0 or 1)값을 result 변수에 저장
        int result = userDao.saveJwt(postLoginRes);

        if(result == 0){
            throw new BaseException(SAVE_FAIL_jwt);   //result 값이 0 (DB에서 userName 값의 수정 실패시) 이면 에러코드 반환
        }
    }








//////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 회원가입 인증 - userJoinCheck() */
    public PostUserRes JoinAuth(PatchJoinAuthReq patchJoinAuthReq) throws BaseException {


        //폰번호에 해당하는 유저가 존재하는지 확인
        if (userProvider.JoinCheck(patchJoinAuthReq.getPhoneNumber()) == 0) {    //가입되지 않은 유저입니다.
            System.out.println("회원가입이 되지 않은 유저입니다.");
            throw new BaseException(FAILED_TO_JOIN_CHECK);
        }


        //authCode 생성 후 userIdx와 함께 반환
        try{
            //인증 코드 생성 (1000번 ~ 9999번 사이)
            int min = 1000;
            int max = 9999;
            int authCode = (int) ((Math.random() * (max - min)) + min);
            System.out.println("인증 코드는" + authCode + "번 입니다.");


            //인증코드 변경
            int result = userDao.modifyAuthCode(authCode, patchJoinAuthReq.getPhoneNumber());
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_AUTHCODE);   //"인증코드에 해당하는 사용자가 없어서 인증코드를 변경하지 못했습니다."
            }

            //유저 idx 값 조회 (phoneNumber 활용)
            User user = userProvider.getUserIdx(patchJoinAuthReq.getPhoneNumber());

            System.out.println(user.getMannerTemp());
            System.out.println(user.getTradeRate());


            //유저 idx, 인증코드 반환
            PostUserRes postUserRes =  new PostUserRes(user.getUserIdx(),authCode);
            return postUserRes;


        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR_MODIFY_FAIL_AUTHCODE);   //"인증 코드 변경에 실패했습니다."
        }

    }





//// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /* 유저 로그아웃 - logout()  */
    public void logout(PatchUserReq patchUserReq) throws BaseException {    //UserController.java에서 객체 값( id, nickName)을 받아와서...
        try{
            //유저 로그아웃
            int result = userDao.logout(patchUserReq);
            if(result == 0){
                throw new BaseException(logout_FAIL_USER);   //"이미 로그아웃 되었습니다."
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR_FAIL_LOGOUT);   //"로그아웃에 실패 하였습니다."
        }
    }



///// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 프로필 수정 - modifyInfo()  */
    public void modifyInfo(PatchUserReq patchUserReq) throws BaseException {    //UserController.java에서 객체 값( id, nickName)을 받아와서...

        try{
        //닉네임 값 변경
        if(patchUserReq.getNickName() != null){
            //닉네임 정보 변경
            int result = userDao.modifyNickName(patchUserReq);
//            if(result == 0){
//                throw new BaseException(MODIFY_FAIL_NICKNAME);   //DB에서 nickName 값의 수정 실패시이면 에러코드 반환  (이미 인가과정이 있어서 필요 없음)
//            }
        }
        //이미지 값 변경
        if(patchUserReq.getImage() != null){
            //이미지 변경
            int result = userDao.modifyImage(patchUserReq);
//            if(result == 0){
//                throw new BaseException(MODIFY_FAIL_IMAGE);   //DB에서 Password 값의 수정 실패시이면 에러코드 반환 (이미 인가과정이 있어서 필요 없음)
//            }
        }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR_MODIFY_FAIL_USER);   //"사용자 정보 변경에 실패하였습니다."
        }


    }











}
