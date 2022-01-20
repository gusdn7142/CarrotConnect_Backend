package com.example.demo.src.review;

import com.example.demo.src.user.UserProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.review.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final ReviewProvider reviewProvider;
    @Autowired
    private final ReviewService reviewService;
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final UserProvider userProvider;

    public ReviewController(ReviewProvider reviewProvider, ReviewService reviewService, JwtService jwtService, UserProvider userProvider){
        this.reviewProvider = reviewProvider;
        this.reviewService = reviewService;
        this.jwtService = jwtService;
        this.userProvider = userProvider;
    }

    /**
     * 거래 후기 등록 API
     * [POST] /reviews/:senderIdx/:receiverIdx/:productIdx
     * @return BaseResponse<Integer>
     */
    // Path-variable
    @ResponseBody
    @PostMapping("/{senderIdx}/{receiverIdx}/{productIdx}")
    public BaseResponse<Integer> createReview(@PathVariable("senderIdx") int senderIdx,
                                             @PathVariable("receiverIdx") int receiverIdx,
                                             @PathVariable("productIdx") int productIdx,
                                             @RequestBody PostReview postReview) {
        try {
            if(postReview.getContent() == null){return new BaseResponse<>(POST_REVIEWS_EMPTY);}
            if(postReview.getContent().length() < 1 || postReview.getContent().length() > 150){return new BaseResponse<>(POST_REVIEWS_INVALID);}
            if(postReview.getPreference() == 0){return new BaseResponse<>(POST_REVIEWS_EMPTY_PREFERENCE);}
            if(postReview.getPreference() < 0 || postReview.getPreference() > 3){return new BaseResponse<>(POST_REVIEWS_INVALID_PREFERENCE);}
            if(postReview.getTypeIdx().size() == 0){return new BaseResponse<>(POST_REVIEWS_EMPTY_MANNER);}
            for(int i = 0; i < postReview.getTypeIdx().size(); i++){
                if(postReview.getTypeIdx().get(i) < 1 || postReview.getTypeIdx().get(i) > 8){
                    return new BaseResponse<>(POST_REVIEWS_INVALID_MANNER);
                }
            }

            int userIdxByJwt = jwtService.getUserIdx();
            if(senderIdx != userIdxByJwt){return new BaseResponse<>(INVALID_USER_JWT);}
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            userProvider.checkByUser(request.getHeader("X-ACCESS-TOKEN"));

            int result = reviewService.createReview(senderIdx, receiverIdx, productIdx, postReview);
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 거래 후기 조회 API
     * [GET] /reviews/:userIdx/:receiverIdx
     * @return BaseResponse<GetReviewAboutUser>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/{userIdx}/{receiverIdx}") // (GET) 127.0.0.1:9000/reviews/:userIdx/:receiverIdx
    public BaseResponse<List<GetReviewAboutUser>> getReviewAboutUser(@PathVariable("userIdx") int userIdx, @PathVariable("receiverIdx") int receiverIdx) {
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt){return new BaseResponse<>(INVALID_USER_JWT);}
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            userProvider.checkByUser(request.getHeader("X-ACCESS-TOKEN"));

            List<GetReviewAboutUser> getReviewAboutUser = reviewProvider.getReviewAboutUser(receiverIdx);
            if(getReviewAboutUser.size() == 0){return new BaseResponse<>(GET_REVIEWS_FAIL);}
            return new BaseResponse<>(getReviewAboutUser);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 거래 후기 삭제 API
     * [PATCH] /reviews/:userIdx/:reviewIdx
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{userIdx}/{reviewIdx}")
    public BaseResponse<String> patchReviewStatus(@PathVariable("userIdx") int userIdx, @PathVariable("reviewIdx") int reviewIdx){
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt){return new BaseResponse<>(INVALID_USER_JWT);}
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            userProvider.checkByUser(request.getHeader("X-ACCESS-TOKEN"));

            reviewService.patchReviewStatus(userIdx, reviewIdx);
            String result = "성공";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
