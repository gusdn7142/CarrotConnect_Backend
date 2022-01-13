package com.example.demo.src.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import javax.servlet.http.HttpServletRequest;

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



///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 유저 로그아웃 API
     * [PATCH] /users:id/logout/
     * @return BaseResponse<String>
     */

    @ResponseBody
    @PatchMapping("/{id}/logout")
    public BaseResponse<String> logout (@PathVariable("id") int userIdx){   //BaseResponse<String>      //@PathVariable("id") int userIdx

        try {
//            /* 접근 제한 구현 */
//            //DB에서 JWT를 가져와 사용자의 IDX를 추출
//            //String jwt = userProvider.getUserToken(userIdx);
//            //int userIdxByJwt = jwtService.getUserIdx2(jwt);
//
            //클라이언트에서 받아온 토큰에서 Idx 추출
            int userIdxByJwt = jwtService.getUserIdx();

            System.out.println(userIdxByJwt);

            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

////            //로그아웃된 유저 (만료된 토큰 접근)인지 확인
////            HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest(); //토큰을 가져온다.
////            userProvider.checkByUser(request.getHeader("X-ACCESS-TOKEN"));
            /*접근 제한 구현끝 */

            PatchUserReq patchUserReq = new PatchUserReq(userIdx,null,null);
            //유저 로그아웃
            userService.logout(patchUserReq);
//
//
            String result = "유저가 로그아웃되었습니다.";   //정보 변경 성공시 메시지 지정
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }



    }


////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 프로필 수정 API
     * [PATCH] /users/:idx
     * @return BaseResponse<String>
     */

    @ResponseBody
    @PatchMapping("/{id}/profile")  //jwt가 탈취될수 있기 때문에 path-variable 방식 사용
    public BaseResponse<String> modifyInfo(@PathVariable("id") int userIdx, @RequestBody PatchUserReq patchUserReq){


        try {
            /* 접근 제한 구현 */
            //DB에서 JWT를 가져와 사용자의 IDX를 추출
            //String jwt = userProvider.getUserToken(userIdx);
            //int userIdxByJwt = jwtService.getUserIdx2(jwt);

            //클라이언트에서 받아온 토큰에서 Idx 추출
            int userIdxByJwt = jwtService.getUserIdx();

            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            //로그아웃된 유저 (만료된 토큰 접근)인지 확인
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(); //토큰을 가져온다.
            userProvider.checkByUser(request.getHeader("X-ACCESS-TOKEN"));
            /*접근 제한 구현 끝 */


            //유저 정보를 객체에 넣음
            patchUserReq.setUserIdx(userIdx);

            //유저 정보 변경
            userService.modifyInfo(patchUserReq);  //userService.java로 patchUserReq객체 값 전송



            String result = "회원 정보 변경이 완료되었습니다.";   //정보 변경 성공시 메시지 지정
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));  //어떤상황에서?.. 오류 뱉어줌
        }
    }




///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 프로필 조회 API
     * [GET] /users/:idx/profile
     * @return BaseResponse<GetUserRes>
     */

    /* GET 방식 - Path-variable (패스 베리어블) */
    @ResponseBody             // JSON 혹은 xml 로 요청에 응답할수 있게 해주는 Annotation
    @GetMapping("/{id}/profile") // (GET) 127.0.0.1:9000/users/:userIdx
    public BaseResponse<GetUserRes> getUserProfile(@PathVariable("id") int userIdx) {              //BaseResponse<GetUserRes>

        try {
            /* 접근 제한 구현 */
            //DB에서 JWT를 가져와 사용자의 IDX를 추출
            //String jwt = userProvider.getUserToken(userIdx);
            //int userIdxByJwt = jwtService.getUserIdx2(jwt);

            //클라이언트에서 받아온 토큰에서 Idx 추출
            int userIdxByJwt = jwtService.getUserIdx();

            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            //로그아웃된 유저 (만료된 토큰 접근)인지 확인
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(); //토큰을 가져온다.
            userProvider.checkByUser(request.getHeader("X-ACCESS-TOKEN"));
            /*접근 제한 구현 끝 */


            //프로필 조회 - getUserProfile()
            GetUserRes getUserRes = userProvider.getUserProfile(userIdx);



            return new BaseResponse<>(getUserRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }

    }










}
