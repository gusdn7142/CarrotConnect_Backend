package com.example.demo.src.alertKeyword;


import com.example.demo.src.alertKeyword.model.*;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import static com.example.demo.config.BaseResponseStatus.*;


//import com.example.demo.src.user.model.*;

import com.example.demo.utils.JwtService;
import com.example.demo.src.user.UserProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@RequestMapping("alert-keywords")
public class AlertKeywordController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AlertKeywordProvider alertKeywordProvider;
    @Autowired
    private final AlertKeywordService alertKeywordService;
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final UserProvider userProvider;


    public AlertKeywordController(AlertKeywordProvider alertKeywordProvider, AlertKeywordService alertKeywordService, JwtService jwtService, UserProvider userProvider) {
        this.alertKeywordProvider = alertKeywordProvider;
        this.alertKeywordService = alertKeywordService;
        this.jwtService = jwtService;
        this.userProvider = userProvider;
    }





//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 41. 알림키워드 등록 API
     * [POST] /alert-keywords/:userIdx
     * @return BaseResponse<PostKeywordReq>   => keywordIdx 값 리턴
     */

    @ResponseBody
    @PostMapping("/{userIdx}")
    public BaseResponse<PostAlertKeywordRes> createKeyword(@PathVariable("userIdx") int userIdx, @RequestBody PostAlertKeywordReq postAlertKeywordReq) {

        try {
        /* 접근 제한 구현 */
        //(jwt 토큰 만료 여부 확인 +) 클라이언트에서 받아온 토큰에서 Idx 추출
        int userIdxByJwt = jwtService.getUserIdx();

        //userIdx와 접근한 유저가 같은지 확인
        if(userIdx != userIdxByJwt){
            return new BaseResponse<>(INVALID_USER_JWT);
        }

        //로그아웃된 유저 인지 확인
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(); //토큰을 가져온다.
        userProvider.checkByUser(request.getHeader("X-ACCESS-TOKEN"));
        /*접근 제한 구현 끝 */


        //키워드 입력란 NULL 값 체크
        if(postAlertKeywordReq.getKeyword() == null){
            return new BaseResponse<>(POST_KEYWORDS_EMPTY_KEYWORD);
        }


        //알림키워드 등록 (keywordIdx 반환)
        postAlertKeywordReq.setUserIdx(userIdx);
        PostAlertKeywordRes PostAlertKeywordRes = alertKeywordService.createKeyword(postAlertKeywordReq);


        return new BaseResponse<>(PostAlertKeywordRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }






    }




////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 42. 알림 키워드 삭제 API
     * [PATCH] /alert-keywords/:userIdx/status
     * @return BaseResponse<String>
     */

    @ResponseBody
    @PatchMapping("/{userIdx}/status")
    public BaseResponse<String> deleteKeyword(@PathVariable("userIdx") int userIdx, @RequestBody PatchAlertKeywordReq patchAlertKeywordReq){


        try {
            /* 접근 제한 구현 */
            //(jwt 토큰 만료 여부 확인 +) 클라이언트에서 받아온 토큰에서 Idx 추출
            int userIdxByJwt = jwtService.getUserIdx();

            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            //로그아웃된 유저 인지 확인
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(); //토큰을 가져온다.
            userProvider.checkByUser(request.getHeader("X-ACCESS-TOKEN"));
            /*접근 제한 구현 끝 */


            //알림 키워드 삭제
            patchAlertKeywordReq.setUserIdx(userIdx);
            alertKeywordService.deleteKeyword(patchAlertKeywordReq);  //userService.java로 patchUserReq객체 값 전송



            String result = " 알림 키워드 " + patchAlertKeywordReq.getKeyword() + "가 삭제되었습니다.";   //정보 변경 성공시 메시지 지정
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));  //어떤상황에서?.. 오류 뱉어줌
        }
    }




///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 43. 알림 키워드 조회 API
     * [GET] /alert-keywords/:userIdx
     * @return BaseResponse<GetAlertkeywardRes>
     */

    @ResponseBody                // JSON 혹은 xml 로 요청에 응답할수 있게 해주는 Annotation
    @GetMapping("/{userIdx}") // (GET) 127.0.0.1:9000/users/:userIdx
    public BaseResponse<List<GetAlertkeywardRes>> getAlertKeyward(@PathVariable("userIdx") int userIdx) {              //BaseResponse<GetUserRes>

        try {
            /* 접근 제한 구현 */
            //(jwt 토큰 만료 여부 확인 +) 클라이언트에서 받아온 토큰에서 Idx 추출
            int userIdxByJwt = jwtService.getUserIdx();

            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            //로그아웃된 유저 인지 확인
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(); //토큰을 가져온다.
            userProvider.checkByUser(request.getHeader("X-ACCESS-TOKEN"));
            /*접근 제한 구현 끝 */


            //알림 키워드 조회 - getAlertKeyward()
            List<GetAlertkeywardRes> getAlertkeywardRes = alertKeywordProvider.getAlertKeyward(userIdx);



            return new BaseResponse<>(getAlertkeywardRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }




    }




/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 44. 알림 설정한 동네 조회 API
     * [GET] /alert-keywords/:userIdx/regions
     * @return BaseResponse<GetAlertRegionRes>
     */

    @ResponseBody                // JSON 혹은 xml 로 요청에 응답할수 있게 해주는 Annotation
    @GetMapping("/{userIdx}/regions") // (GET) 127.0.0.1:9000/users/:userIdx
    public BaseResponse<List<GetAlertRegionRes>> getAlertRegion (@PathVariable("userIdx") int userIdx) {              //BaseResponse<GetUserRes>

        try {
            /* 접근 제한 구현 */
            //(jwt 토큰 만료 여부 확인 +) 클라이언트에서 받아온 토큰에서 Idx 추출
            int userIdxByJwt = jwtService.getUserIdx();

            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            //로그아웃된 유저 인지 확인
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(); //토큰을 가져온다.
            userProvider.checkByUser(request.getHeader("X-ACCESS-TOKEN"));
            /*접근 제한 구현 끝 */


            //알림 설정한 동네 조회 - getAlertRegion()
            List<GetAlertRegionRes> getAlertRegionRes = alertKeywordProvider.getAlertRegion(userIdx);


            return new BaseResponse<>(getAlertRegionRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }




    }




////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 45. 알림 설정한 동네 변경 API
     * [PATCH] /alert-keywords/:userIdx/status
     * @return BaseResponse<String>
     */

    @ResponseBody
    @PatchMapping("/{userIdx}/regions")
    public BaseResponse<String> modifyAlertRegion(@PathVariable("userIdx") int userIdx, @RequestBody PatchAlertKeywordReq patchAlertKeywordReq){


        try {
            /* 접근 제한 구현 */
            //(jwt 토큰 만료 여부 확인 +) 클라이언트에서 받아온 토큰에서 Idx 추출
            int userIdxByJwt = jwtService.getUserIdx();

            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            //로그아웃된 유저 인지 확인
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(); //토큰을 가져온다.
            userProvider.checkByUser(request.getHeader("X-ACCESS-TOKEN"));
            /*접근 제한 구현 끝 */



            patchAlertKeywordReq.setUserIdx(userIdx);
            String result = "";

            //알림 동네 활성화
            if(patchAlertKeywordReq.getRegionAlertStatus() == 1){
                alertKeywordService.activateRegionstatus(patchAlertKeywordReq);
                result = patchAlertKeywordReq.getRegionName() + "에 대한 동네 알림이 활성화 되었습니다.";   //정보 변경 메시지 지정
            }
            //알림 동네 비활성화
            else if(patchAlertKeywordReq.getRegionAlertStatus() == 0){
                alertKeywordService.InActivateRegionstatus(patchAlertKeywordReq);
                result = patchAlertKeywordReq.getRegionName() + "에 대한 동네 알림이 비활성화 되었습니다.";   //정보 변경  메시지 지정
            }
            else{
                result = "동네 알림 활성화 변수" + patchAlertKeywordReq.getRegionAlertStatus() + "를 올바르게 입력하셨는지 확인해 주세요.";
            }


            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));  //어떤상황에서?.. 오류 뱉어줌
        }
    }



/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 46. 키워드 알림 상품 조회 API
     * [GET] /alert-keywords/:userIdx/products
     * @return BaseResponse<GetUserRes>
     */

    @ResponseBody
    @GetMapping("/{userIdx}/products")
    public BaseResponse<GetALLAlertProductRes>  getAlertProducts (@PathVariable("userIdx") int userIdx) {           //BaseResponse<List<GetAlertPrductRes>>

        try {
            /* 접근 제한 구현 */
            //DB에서 JWT를 가져와 사용자의 IDX를 추출
            //String jwt = userProvider.getUserToken(userIdx);
            //int userIdxByJwt = jwtService.getUserIdx2(jwt);

            //(jwt 토큰 만료 여부 확인 +) 클라이언트에서 받아온 토큰에서 Idx 추출
            int userIdxByJwt = jwtService.getUserIdx();

            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            //로그아웃된 유저 인지 확인
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(); //토큰을 가져온다.
            userProvider.checkByUser(request.getHeader("X-ACCESS-TOKEN"));
            /*접근 제한 구현 끝 */


            //알림 키워드 불러오기 - getAlertKeyward()
            List<GetAlertkeywardRes> getAlertkeywardRes = alertKeywordProvider.getAlertKeyward(userIdx);


           //키워드 알림 상품 조회 - getAlertProducts()
            GetALLAlertProductRes getALLAlertProductRes = alertKeywordProvider.getAlertProducts(getAlertkeywardRes, userIdx);


            //키워드 알림 상품이 1개도 없을때
            if(getALLAlertProductRes.getGetAlertPrductRes().isEmpty()){
                return new BaseResponse<>(GET_KEYWORDS_EMPTY_KEYWORD);
            }


            return new BaseResponse<>(getALLAlertProductRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }




    }








}