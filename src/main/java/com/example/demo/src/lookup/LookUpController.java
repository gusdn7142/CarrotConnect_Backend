package com.example.demo.src.lookup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexPhoneNumber;

@RestController
@RequestMapping("/lookups")
public class LookUpController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final LookUpService lookUpService;
    @Autowired
    private final JwtService jwtService;

    public LookUpController(LookUpService lookUpService, JwtService jwtService){
        this.lookUpService = lookUpService;
        this.jwtService = jwtService;
    }

    /**
     * 조회 상품 등록 API
     * [POST] /lookups/:userIdx/:productIdx
     * @return BaseResponse<String>
     */
    // Path-variable
    @ResponseBody
    @PostMapping("/{userIdx}/{productIdx}")
    public BaseResponse<String> createLookUpProduct(@PathVariable("userIdx") int userIdx, @PathVariable("productIdx") int productIdx) {
        try {
            /**
             * validation 처리해야될것
             * 1. 존재하는 사용자인지
             * 2. 존재하는 상품인지
             */

            // 헤더 (인증코드)에서 userIdx 추출.
            int userIdxByJwt = jwtService.getUserIdx();

            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            //같다면 변경
            lookUpService.createLookUpProduct(userIdx, productIdx);
            String result = "조회한 상품 등록 성공";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
