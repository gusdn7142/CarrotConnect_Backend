package com.example.demo.src.gather;


import com.example.demo.src.alertKeyword.model.GetAlertkeywardRes;
import com.example.demo.src.alertKeyword.model.PatchAlertKeywordReq;
import com.example.demo.src.gather.model.*;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import static com.example.demo.config.BaseResponseStatus.*;

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
@RequestMapping("gathers")
public class GatherController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final GatherProvider gatherProvider;
    @Autowired
    private final GatherService gatherService;
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final UserProvider userProvider;


    public GatherController(GatherProvider gatherProvider, GatherService gatherService, JwtService jwtService, UserProvider userProvider) {
        this.gatherProvider = gatherProvider;
        this.gatherService = gatherService;
        this.jwtService = jwtService;
        this.userProvider = userProvider;
    }



//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 33. 모아보기 추가 API
     * [POST] /gathers/:userIdx
     * @return BaseResponse<PostKeywordReq>
     */

    @ResponseBody
    @PostMapping("/{userIdx}")
    public BaseResponse<PostGatherRes> createGather(@PathVariable("userIdx") int userIdx, @RequestBody PostGatherReq postGatherReq) {

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



            //모아보기 추가 (gatherIdx 반환)
            postGatherReq.setUserIdx(userIdx);
            PostGatherRes postGatherRes = gatherService.createGather(postGatherReq);

            return new BaseResponse<>(postGatherRes);


        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }


////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 34. 모아보기 취소 API
     * [PATCH] /gathers/:userIdx/status
     * @return BaseResponse<String>
     */

    @ResponseBody
    @PatchMapping("/{userIdx}/status")
    public BaseResponse<String> deleteGather(@PathVariable("userIdx") int userIdx, @RequestBody PatchGatherReq patchGatherReq){


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


            //모아보기 취소
            patchGatherReq.setUserIdx(userIdx);
            gatherService.deleteGather(patchGatherReq);  //userService.java로 patchUserReq객체 값 전송


            String result = patchGatherReq.getNickName() + "님이 모아보기에서 해제되었습니다.";   //정보 변경 성공시 메시지 지정
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));  //어떤상황에서?.. 오류 뱉어줌
        }
    }






///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 36. 모아보기한 사용자 조회 API API
     * [GET] /gathers/:userIdx/users
     * @return BaseResponse<GetAlertkeywardRes>
     */

    @ResponseBody
    @GetMapping("/{userIdx}/users")
    public BaseResponse<List<GetGatherUserRes>> getGatherUser(@PathVariable("userIdx") int userIdx) {              //BaseResponse<GetUserRes>

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


            //모아보기한 사용자 조회 - getGatherUser()
            List<GetGatherUserRes> getGatherUserRes = gatherProvider.getGatherUser(userIdx);



            return new BaseResponse<>(getGatherUserRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }




    }




///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 36. 모아보기한 상품 조회 API API
     * [GET] /gathers/:userIdx/users
     * @return BaseResponse<GetAlertkeywardRes>
     */

    @ResponseBody
    @GetMapping("/{userIdx}/products")
    public BaseResponse<List<GetGatherProductRes>> getGatherProduct(@PathVariable("userIdx") int userIdx) {              //BaseResponse<GetUserRes>

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


            //모아보기한 상품 조회 - getGatherProduct()
            List<GetGatherProductRes> getGatherProductRes = gatherProvider.getGatherProduct(userIdx);



            return new BaseResponse<>(getGatherProductRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }




    }


























}