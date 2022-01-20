package com.example.demo.src.review;

import com.example.demo.config.BaseException;
import com.example.demo.src.review.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Service
public class ReviewService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ReviewDao reviewDao;
    private final ReviewProvider reviewProvider;
    private final JwtService jwtService;

    @Autowired
    public ReviewService(ReviewDao reviewDao, ReviewProvider reviewProvider, JwtService jwtService) {
        this.reviewDao = reviewDao;
        this.reviewProvider = reviewProvider;
        this.jwtService = jwtService;
    }

    public int createReview(int senderIdx, int receiverIdx, int productIdx, PostReview postReview) throws BaseException {
        try{
            int checkProdcut = reviewDao.checkProduct(productIdx);
            if(checkProdcut == 0){throw new BaseException(DATABASE_ERROR_NOT_EXIST_PRODUCT);}

            int checkUser = reviewDao.checkUser(receiverIdx);
            if(checkUser == 0){throw new BaseException(DATABASE_ERROR_NOT_EXISTS_USER);}

            int result = reviewDao.createReview(senderIdx, receiverIdx, productIdx, postReview);
            if(result == 0){throw new BaseException(POST_REVIEWS_FAIL);}
            return result;
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void patchReviewStatus(int userIdx, int reviewIdx) throws BaseException {
        try{
            int checkReview = reviewDao.checkReview(reviewIdx);
            if(checkReview == 0){throw new BaseException(DATABASE_ERROR_NOT_EXITS_REVIEW);}

            int checkAccess = reviewDao.checkAccess(userIdx, reviewIdx);
            if(checkAccess == 0){throw new BaseException(DATABASE_ERROR_NOT_ACCESS_REVIEW_USER);}

            int result = reviewDao.patchReviewStatus(userIdx, reviewIdx);
            if(result == 0){throw new BaseException(PATCH_REVIEWS_FAIL);}
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
