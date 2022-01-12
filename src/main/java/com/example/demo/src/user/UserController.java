package com.example.demo.src.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.*;

@RestController
@RequestMapping("users")
public class UserController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final UserProvider userProvider;
    @Autowired
    private final UserService userService;
    @Autowired
    private final JwtService jwtService;


    public UserController(UserProvider userProvider, UserService userService, JwtService jwtService){
        this.userProvider = userProvider;
        this.userService = userService;
        this.jwtService = jwtService;
    }



//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 회원가입 API
     * [POST] /users
     * @return BaseResponse<PostUserRes>
     */
    // Body
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostUserRes> createUser(@RequestBody PostUserReq postUserReq) {

        //전화번호 입력란 NULL 값 체크
        if(postUserReq.getPhoneNumber() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_PHONENUMBER);
        }

        //전화번호 정규표현식 (010필수 총 8자리 수 형식)
        if(!isRegexPhoneNumber(postUserReq.getPhoneNumber())){
            return new BaseResponse<>(POST_USERS_INVALID_PHONENUMBER);
        }



        try{
            //유저 등록 (userIdx, authCode 반환)
            PostUserRes postUserRes = userService.createUser(postUserReq);

            //동네 등록
            postUserReq.setUserIdx(postUserRes.getUserIdx());    //동네정보에 들어갈 userIdx 설정
            userService.createRegion(postUserReq);

            return new BaseResponse<>(postUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }




    }




//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 로그인 API
     * [POST] /users/logIn
     * @return BaseResponse<PostLoginRes>
     */
    @ResponseBody
    @PostMapping("/login")
    public BaseResponse<PostLoginRes> logIn(@RequestBody PostLoginReq postLoginReq){

        //인증코드 입력란 NULL 값 체크
        if(postLoginReq.getAuthCode() == null){
            return new BaseResponse<>(POST_lOGINS_EMPTY_AUTHCODE);
        }

        //인증코드 정규표현식 (총 4자리 수 형식)
        if(!isRegexAuthCode(postLoginReq.getAuthCode())){
            return new BaseResponse<>(POST_lOGINS_INVALID_AUTHCODE);
        }



        try{
            /* 로그인 진행 (useridx, authCode 전송) */
            PostLoginRes postLoginRes = userProvider.logIn(postLoginReq);  //postLoginRes 변수에 userIdx와 jwt를 리턴


            //JWT 토큰 만료시간 확인 (추후 삭제)
            //jwtService.getJwtContents(postLoginRes.getJwt());


            // jwt토큰을 DB에 저장 (중복 부여는 고려하지 않아도 된다...)
            userService.saveJwt(postLoginRes);



            return new BaseResponse<>(postLoginRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }







//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 회원가입 인증 API
     * [POST] /users/logIn
     * @return BaseResponse<PostLoginRes>
     */
    @ResponseBody
    @PatchMapping("/join-auth")
    public BaseResponse<PostUserRes> JoinAuth(@RequestBody PatchJoinAuthReq patchJoinAuthReq){
        try{

            //전화번호 입력란 NULL 값 체크
            if(patchJoinAuthReq.getPhoneNumber() == null){
                return new BaseResponse<>(POST_USERS_EMPTY_PHONENUMBER);
            }

            //전화번호 정규표현식 (010필수 총 8자리 수 형식)
            if(!isRegexPhoneNumber(patchJoinAuthReq.getPhoneNumber())){
                return new BaseResponse<>(POST_USERS_INVALID_PHONENUMBER);
            }


            /* 회원가입 인증 진행 (userIdx 전송 -> userIdx, authCode 반환) */
            PostUserRes postUserRes = userService.JoinAuth(patchJoinAuthReq);  //postLoginRes 변수에 userIdx와 jwt를 리턴

//        System.out.println(patchJoinAuthReq);


            return new BaseResponse<>(postUserRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }


    }







}
