package com.example.demo.src.sympathy;

import com.example.demo.src.user.UserProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.sympathy.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

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
    @Autowired
    private final UserProvider userProvider;

    public SympathyController(SympathyProvider sympathyProvider, SympathyService sympathyService, JwtService jwtService, UserProvider userProvider){
        this.sympathyProvider = sympathyProvider;
        this.sympathyService = sympathyService;
        this.jwtService = jwtService;
        this.userProvider = userProvider;
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
            if(sympathyIdx < 1 || sympathyIdx > 7){return new BaseResponse<>(POST_SYMPATHIES_INVALID);}

            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt){return new BaseResponse<>(INVALID_USER_JWT);}
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            userProvider.checkByUser(request.getHeader("X-ACCESS-TOKEN"));

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
            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt){return new BaseResponse<>(INVALID_USER_JWT);}
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            userProvider.checkByUser(request.getHeader("X-ACCESS-TOKEN"));

            List<GetSympathy> getSympathy = sympathyProvider.getSympathy(postIdx);
            if(getSympathy.size() == 0){return new BaseResponse<>(GET_SYMPATHIES_FAIL);}
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
            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt){return new BaseResponse<>(INVALID_USER_JWT);}
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            userProvider.checkByUser(request.getHeader("X-ACCESS-TOKEN"));

            sympathyService.patchSympathyStatus(postIdx, userIdx);
            String result = "성공";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}

