package com.example.demo.src.review;

import com.example.demo.src.product.model.GetProductPurchased;
import com.example.demo.src.product.model.PatchProductInterest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.review.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexPhoneNumber;

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

    public ReviewController(ReviewProvider reviewProvider, ReviewService reviewService, JwtService jwtService){
        this.reviewProvider = reviewProvider;
        this.reviewService = reviewService;
        this.jwtService = jwtService;
    }

    /**
     * 거래 후기 등록 API
     * [POST] /reviews/:senderIdx/:receiverIdx/:productIdx
     * @return BaseResponse<String>
     */
    // Path-variable
    @ResponseBody
    @PostMapping("/{senderIdx}/{receiverIdx}/{productIdx}")
    public BaseResponse<String> createReview(@PathVariable("senderIdx") int senderIdx,
                                             @PathVariable("receiverIdx") int receiverIdx,
                                             @PathVariable("productIdx") int productIdx,
                                             @RequestBody PostReview postReview) {
        try {
            /**
             * validation 처리해야될것
             * 1. 존재하는 사용자인지
             * 2. 존재하는 상품인지
             */

            // 헤더 (인증코드)에서 userIdx 추출.
            int userIdxByJwt = jwtService.getUserIdx();

            //userIdx와 접근한 유저가 같은지 확인
            if(senderIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            String result = reviewService.createReview(senderIdx, receiverIdx, productIdx, postReview);
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
            // 헤더 (인증코드)에서 userIdx 추출.
            int userIdxByJwt = jwtService.getUserIdx();

            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            // Get Review About User
            List<GetReviewAboutUser> getReviewAboutUser = reviewProvider.getReviewAboutUser(receiverIdx);
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
            // 헤더 (인증코드)에서 userIdx 추출.
            int userIdxByJwt = jwtService.getUserIdx();

            // userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            String result = reviewService.patchReviewStatus(userIdx, reviewIdx);
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
