package com.example.demo.src.category;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.category.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexPhoneNumber;

@RestController
@RequestMapping("/interest-categorys")
public class CategoryController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final CategoryProvider categoryProvider;
    @Autowired
    private final CategoryService categoryService;
    @Autowired
    private final JwtService jwtService;

    public CategoryController(CategoryProvider categoryProvider, CategoryService categoryService, JwtService jwtService){
        this.categoryProvider = categoryProvider;
        this.categoryService = categoryService;
        this.jwtService = jwtService;
    }

    /**
     * 관심 카테고리 조회 API
     * [GET] /interst-categorys/:userIdx
     * @return BaseResponse<GetProduct>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/{userIdx}") // (GET) 127.0.0.1:9000/interst-categorys/:userIdx
    public BaseResponse<List<GetCategoryInterest>> getCategoryInterest(@PathVariable("userIdx") int userIdx) {
        try{
            // 헤더 (인증코드)에서 userIdx 추출.
            int userIdxByJwt = jwtService.getUserIdx();

            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            // Get Category Interest
            List<GetCategoryInterest> getCategoryInterest = categoryProvider.getCategoryInterest(userIdx);
            return new BaseResponse<>(getCategoryInterest);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 관심 카테고리 등록 API
     * [POST] /interst-categorys/:userIdx/:categoryIdx
     * @return BaseResponse<String>
     */
    // Path-variable
    @ResponseBody
    @PostMapping("/{userIdx}/{categoryIdx}")
    public BaseResponse<String> createInterestProduct(@PathVariable("userIdx") int userIdx, @PathVariable("categoryIdx") int categoryIdx) {
        try {
            /**
             * validation 처리해야될것
             * 1. 존재하는 사용자인지
             * 2. 존재하는 카테고리인지
             */

            // 헤더 (인증코드)에서 userIdx 추출.
            int userIdxByJwt = jwtService.getUserIdx();

            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            //같다면 변경
            categoryService.createInterestCategory(userIdx, categoryIdx);
            String result = "관심 카테고리 등록 성공";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 관심 카테고리 취소 (관심 카테고리 상태 변경) API
     * [PATCH] /interst-categorys/:idx/status
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{idx}/status")
    public BaseResponse<String> patchProductInterest(@PathVariable("idx") int idx, @RequestBody PatchCategoryInterest patchCategoryInterest){
        try {
            /**
             * validation 처리해야될것
             * 1. 접근한 유저가 관심 목록을 누른 유저가 맞는지
             */

            // 헤더 (인증코드)에서 userIdx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            int userIdx = patchCategoryInterest.getUserIdx();

            // userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            // 데이터 베이스에서 idx 값으로 userIdx 값을 반환
            // userIdx와 접근한 유저가 같은지 확인

            categoryService.patchCategoryInterest(idx);
            String result = "관심 카테고리 취소 성공";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
