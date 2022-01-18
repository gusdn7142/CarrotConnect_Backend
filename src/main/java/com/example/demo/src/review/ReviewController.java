package com.example.demo.src.review;

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

            //같다면 변경
            reviewService.createReview(senderIdx, receiverIdx, productIdx, postReview);
            String result = "거래 후기 등록 성공";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
