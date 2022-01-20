package com.example.demo.src.review;

import com.example.demo.config.BaseException;
import com.example.demo.src.review.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

//Provider : Read의 비즈니스 로직 처리
@Service
public class ReviewProvider {

    private final ReviewDao reviewDao;
    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ReviewProvider(ReviewDao reviewDao, JwtService jwtService) {
        this.reviewDao = reviewDao;
        this.jwtService = jwtService;
    }

    public List<GetReviewAboutUser> getReviewAboutUser(int receiverIdx) throws BaseException{
        try{
            int checkUser = reviewDao.checkUser(receiverIdx);
            if(checkUser == 0){throw new BaseException(DATABASE_ERRORS_NOT_EXITS_USER);}

            List<GetReviewAboutUser> getReviewAboutUser = reviewDao.getReviewAboutUser(receiverIdx);
            return getReviewAboutUser;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
