package com.example.demo.src.hidden;


import com.example.demo.src.hidden.model.*;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import static com.example.demo.config.BaseResponseStatus.*;

import com.example.demo.src.user.UserProvider;

import com.example.demo.src.user.model.GetUserBlockRes;
import com.example.demo.src.user.model.PatchUserBlockCancellReq;
import com.example.demo.src.user.model.PostUserBlockReq;
import com.example.demo.utils.JwtService;
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
import static com.example.demo.utils.ValidationRegex.*;





@RestController
@RequestMapping("hidden-users")
public class HiddenController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final HiddenProvider hiddenProvider;
    @Autowired
    private final HiddenService hiddenService;
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final UserProvider userProvider;

    public HiddenController(HiddenProvider hiddenProvider, HiddenService hiddenService, JwtService jwtService, UserProvider userProvider) {
        this.hiddenProvider = hiddenProvider;
        this.hiddenService = hiddenService;
        this.jwtService = jwtService;
        this.userProvider = userProvider;
    }





//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 48. 미노출 사용자 추가 API
     * [Post] /hidden-users/:userIdx
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PostMapping("/{userIdx}")
    public BaseResponse<String> hideUser(@PathVariable("userIdx") int userIdx, @RequestBody PostHideUserReq postHideUserReq){   //BaseResponse<String>

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


            //미노출 사용자 추가
            postHideUserReq.setUserIdx(userIdx);
            hiddenService.hideUser(postHideUserReq);


            String result = "더이상 목록에서 보이지 않습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));  //어떤상황에서?.. 오류 뱉어줌
        }



    }



//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 49. 미노출 사용자 취소 API
     * [Post] /hidden-users/:userIdx/status
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{userIdx}/status")
    public BaseResponse<String> hideUserCancell(@PathVariable("userIdx") int userIdx, @RequestBody PatchHideUserCancellReq patchHideUserCancellReq){   //BaseResponse<String>

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


            //미노출 사용자 취소
            patchHideUserCancellReq.setUserIdx(userIdx);
            hiddenService.hideUserCancell(patchHideUserCancellReq);


            String result = patchHideUserCancellReq.getHideCancellNickName() + "님의 게시글이 다시 보입니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }


    }






///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 50. 미노출 사용자 조회 API
     * [GET] /hidden-users/:userIdx
     * @return BaseResponse<GetUserRes>
     */

    /* GET 방식 - Path-variable (패스 베리어블) */
    @ResponseBody
    @GetMapping("/{userIdx}")
    public BaseResponse<List<GetHiddenUserRes>> getHiddenUser(@PathVariable("userIdx") int userIdx) {

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

            //로그아웃된 유저 인지 확인
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(); //토큰을 가져온다.
            userProvider.checkByUser(request.getHeader("X-ACCESS-TOKEN"));
            /*접근 제한 구현 끝 */


            //미노출 사용자 정보 조회 - getBlockUser()
            List<GetHiddenUserRes> getHiddenUserRes = hiddenProvider.getHiddenUser(userIdx);



            return new BaseResponse<>(getHiddenUserRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }

    }

























}