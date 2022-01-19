package com.example.demo.src.sympathy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.sympathy.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexPhoneNumber;

@RestController
@RequestMapping("/town-activities")
public class SympathyController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final SympathyProvider sympathyProvider;
    @Autowired
    private final SympathyService sympathyService;
    @Autowired
    private final JwtService jwtService;

    public SympathyController(SympathyProvider sympathyProvider, SympathyService sympathyService, JwtService jwtService){
        this.sympathyProvider = sympathyProvider;
        this.sympathyService = sympathyService;
        this.jwtService = jwtService;
    }

    /**
     * 게시글 공감 API
     * [POST] /town-activities/postIdx/:userIdx/:sympathyIdx
     * @return BaseResponse<Integer>
     */
    // Path-variable
    @ResponseBody
    @PostMapping("/{postIdx}/{userIdx}/{sympathyIdx}")
    public BaseResponse<Integer> createInterestProduct(@PathVariable("postIdx") int postIdx, @PathVariable("userIdx") int userIdx, @PathVariable("sympathyIdx") int sympathyIdx) {
        try {
            /**
             * validation 처리해야될것
             * 1. 존재하는 사용자인지
             * 2. 존재하는 게시글인지
             * 3. 공감 범위
             */

            // 헤더 (인증코드)에서 userIdx 추출.
            int userIdxByJwt = jwtService.getUserIdx();

            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            int result = sympathyService.createSympathy(postIdx, userIdx, sympathyIdx);
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 게시글 공감 조회 API
     * [GET] /town-activities/:postIdx/:userIdx/sympathy
     * @return BaseResponse<GetSympathy>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/{postIdx}/{userIdx}/sympathy")
    public BaseResponse<List<GetSympathy>> getSympathy(@PathVariable("postIdx") int postIdx, @PathVariable("userIdx") int userIdx) {
        try{
            // 헤더 (인증코드)에서 userIdx 추출.
            int userIdxByJwt = jwtService.getUserIdx();

            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            List<GetSympathy> getSympathy = sympathyProvider.getSympathy(postIdx);
            return new BaseResponse<>(getSympathy);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 게시글 공감 취소 API
     * [PATCH] /town-activities/:postIdx/:userIdx/sympathy-status
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{postIdx}/{userIdx}/sympathy-status")
    public BaseResponse<String> patchSympathyStatus(@PathVariable("postIdx") int postIdx, @PathVariable("userIdx") int userIdx){
        try {
            // 헤더 (인증코드)에서 userIdx 추출.
            int userIdxByJwt = jwtService.getUserIdx();

            // userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            String result = sympathyService.patchSympathyStatus(postIdx, userIdx);
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}

