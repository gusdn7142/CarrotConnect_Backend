package com.example.demo.src.region;

import com.example.demo.src.user.UserProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.region.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

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
    @Autowired
    private final UserProvider userProvider;

    public RegionController(RegionProvider regionProvider, RegionService regionService, JwtService jwtService, UserProvider userProvider){
        this.regionProvider = regionProvider;
        this.regionService = regionService;
        this.jwtService = jwtService;
        this.userProvider = userProvider;
    }

    /**
     * 내 동네 추가 API
     * [POST] /regions/:userIdx
     * @return BaseResponse<Integer>
     */
    // Path-variable
    @ResponseBody
    @PostMapping("/{userIdx}")
    public BaseResponse<Integer> createRegion(@PathVariable("userIdx") int userIdx, @RequestBody PostRegion postRegion) {
        try {
            if(postRegion.getRegionName() == null){return new BaseResponse<>(POST_REGIONS_EMPTY_NAME);}
            if(postRegion.getRegionName().length() <= 3 || postRegion.getRegionName().length() > 15){return new BaseResponse<>(POST_REGIONS_INVALID_NAME);}
            if(postRegion.getLongitude() == 0 || postRegion.getLatitude() == 0){return new BaseResponse<>(POST_REGIONS_EMPTY_LATITUDE_LONGITUDE);}
            if(postRegion.getLongitude() < -180 || postRegion.getLongitude() > 180 || postRegion.getLatitude() < -90 || postRegion.getLatitude() > 90){return new BaseResponse<>(POST_REGIONS_INVALID_LATITUDE_LONGITUDE);}
            if(postRegion.getKeywordAlertStatus() < 0 || postRegion.getKeywordAlertStatus() > 1) {return new BaseResponse<>(POST_REGIONS_INVALID_ALERT);}

            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt){return new BaseResponse<>(INVALID_USER_JWT);}
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            userProvider.checkByUser(request.getHeader("X-ACCESS-TOKEN"));

            int result = regionService.createRegion(userIdx, postRegion);
            if(result == 0){return new BaseResponse<>(POST_REGIONS_FAIL);}
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 내 동네 조회 API
     * [GET] /regions/:userIdx
     * @return BaseResponse<GetRegion>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/{userIdx}") // (GET) 127.0.0.1:9000/regions/:userIdx
    public BaseResponse<List<GetRegion>> getRegion(@PathVariable("userIdx") int userIdx) {
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt){return new BaseResponse<>(INVALID_USER_JWT);}
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            userProvider.checkByUser(request.getHeader("X-ACCESS-TOKEN"));

            List<GetRegion> getRegion = regionProvider.getRegion(userIdx);
            if(getRegion.size() == 0){return new BaseResponse<>(GET_REGIONS_FAIL);}
            return new BaseResponse<>(getRegion);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 내 동네 삭제 API
     * [PATCH] /regions/:idx/userIdx/status
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{idx}/{userIdx}/status")
    public BaseResponse<String> patchRegionStatus(@PathVariable("idx") int idx, @PathVariable("userIdx") int userIdx){
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt){return new BaseResponse<>(INVALID_USER_JWT);}
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            userProvider.checkByUser(request.getHeader("X-ACCESS-TOKEN"));

            regionService.patchRegionStatus(idx, userIdx);
            String result = "성공";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 내 동네 인증하기 API
     * [PATCH] /regions/:idx/userIdx/auth-status
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{idx}/{userIdx}/auth-status")
    public BaseResponse<String> patchRegionAuth(@PathVariable("idx") int idx, @PathVariable("userIdx") int userIdx){
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt){return new BaseResponse<>(INVALID_USER_JWT);}
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            userProvider.checkByUser(request.getHeader("X-ACCESS-TOKEN"));

            regionService.patchRegionAuth(idx, userIdx);
            String result = "성공";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 현재 내 동네 설정하기 API
     * [PATCH] /regions/:idx/:userIdx/now-status
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{idx}/{userIdx}/now-status")
    public BaseResponse<String> patchRegionNow(@PathVariable("idx") int idx, @PathVariable("userIdx") int userIdx){
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt){return new BaseResponse<>(INVALID_USER_JWT);}
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            userProvider.checkByUser(request.getHeader("X-ACCESS-TOKEN"));

            regionService.patchRegionNow(idx, userIdx);
            String result = "성공";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
