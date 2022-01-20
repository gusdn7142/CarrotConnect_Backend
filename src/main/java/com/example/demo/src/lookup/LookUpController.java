package com.example.demo.src.lookup;

import com.example.demo.src.user.UserProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/lookups")
public class LookUpController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final LookUpService lookUpService;
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final UserProvider userProvider;

    public LookUpController(LookUpService lookUpService, JwtService jwtService, UserProvider userProvider){
        this.lookUpService = lookUpService;
        this.jwtService = jwtService;
        this.userProvider = userProvider;
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
            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt){return new BaseResponse<>(INVALID_USER_JWT);}
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            userProvider.checkByUser(request.getHeader("X-ACCESS-TOKEN"));

            lookUpService.createLookUpProduct(userIdx, productIdx);
            String result = "성공";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
