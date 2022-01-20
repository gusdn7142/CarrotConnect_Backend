package com.example.demo.src.category;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.category.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.example.demo.src.user.UserProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

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
    @Autowired
    private final UserProvider userProvider;

    public CategoryController(CategoryProvider categoryProvider, CategoryService categoryService, JwtService jwtService, UserProvider userProvider){
        this.categoryProvider = categoryProvider;
        this.categoryService = categoryService;
        this.jwtService = jwtService;
        this.userProvider = userProvider;
    }

    /**
     * 관심 카테고리 조회 API
     * [GET] /interst-categorys/:userIdx
     * @return BaseResponse<GetCategoryInterest>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/{userIdx}") // (GET) 127.0.0.1:9000/interst-categorys/:userIdx
    public BaseResponse<List<GetCategoryInterest>> getCategoryInterest(@PathVariable("userIdx") int userIdx) {
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt){return new BaseResponse<>(INVALID_USER_JWT);}
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            userProvider.checkByUser(request.getHeader("X-ACCESS-TOKEN"));

            List<GetCategoryInterest> getCategoryInterest = categoryProvider.getCategoryInterest(userIdx);
            if(getCategoryInterest.size() == 0){return new BaseResponse<>(GET_CATEGORIES_FAIL);}
            return new BaseResponse<>(getCategoryInterest);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 관심 카테고리 등록 API
     * [POST] /interst-categorys/:userIdx/:categoryIdx
     * @return BaseResponse<Integer>
     */
    // Path-variable
    @ResponseBody
    @PostMapping("/{userIdx}/{categoryIdx}")
    public BaseResponse<Integer> createInterestProduct(@PathVariable("userIdx") int userIdx, @PathVariable("categoryIdx") int categoryIdx) {
        try {
            if(categoryIdx < 0 || categoryIdx > 17){return new BaseResponse<>(POST_PRODUCTS_INVALID_CATEGORY);}

            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt){return new BaseResponse<>(INVALID_USER_JWT);}
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            userProvider.checkByUser(request.getHeader("X-ACCESS-TOKEN"));

            int result = categoryService.createInterestCategory(userIdx, categoryIdx);
            if(result == 0){return new BaseResponse<>(POST_CATEGORIES_FAIL);}
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
            int userIdxByJwt = jwtService.getUserIdx();
            int userIdx = patchCategoryInterest.getUserIdx();
            if(userIdx != userIdxByJwt){return new BaseResponse<>(INVALID_USER_JWT);}HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            userProvider.checkByUser(request.getHeader("X-ACCESS-TOKEN"));

            categoryService.patchCategoryInterest(idx, userIdx);
            String result = "성공";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
