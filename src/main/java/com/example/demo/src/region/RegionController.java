package com.example.demo.src.region;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.region.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexPhoneNumber;

@RestController
@RequestMapping("/regions")
public class RegionController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final RegionProvider regionProvider;
    @Autowired
    private final RegionService regionService;
    @Autowired
    private final JwtService jwtService;

    public RegionController(RegionProvider regionProvider, RegionService regionService, JwtService jwtService){
        this.regionProvider = regionProvider;
        this.regionService = regionService;
        this.jwtService = jwtService;
    }

    /**
     * 내 동네 추가 API
     * [POST] /regions/:userIdx
     * @return BaseResponse<String>
     */
    // Path-variable
    @ResponseBody
    @PostMapping("/{userIdx}")
    public BaseResponse<String> createRegion(@PathVariable("userIdx") int userIdx, @RequestBody PostRegion postRegion) {
        try {
            /**
             * validation 처리해야될것
             * 1. 존재하는 사용자인지
             * 2. 올바른 값들이 들어오는지
             */

            // 헤더 (인증코드)에서 userIdx 추출.
            int userIdxByJwt = jwtService.getUserIdx();

            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            System.out.println("here");
            regionService.createRegion(userIdx, postRegion);
            String result = "내 동네 추가 성공";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
