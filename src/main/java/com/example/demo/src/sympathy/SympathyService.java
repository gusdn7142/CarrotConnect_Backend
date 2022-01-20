package com.example.demo.src.sympathy;

import com.example.demo.config.BaseException;
import com.example.demo.src.sympathy.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Service
public class SympathyService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final SympathyDao sympathyDao;
    private final SympathyProvider sympathyProvider;
    private final JwtService jwtService;

    @Autowired
    public SympathyService(SympathyDao sympathyDao, SympathyProvider sympathyProvider, JwtService jwtService) {
        this.sympathyDao = sympathyDao;
        this.sympathyProvider = sympathyProvider;
        this.jwtService = jwtService;
    }

    public int createSympathy(int postIdx, int userIdx, int sympathyIdx) throws BaseException {
        try{
            int checkPost = sympathyDao.checkPost(postIdx);
            if(checkPost == 0){throw new BaseException(DATABASE_ERROR_NOT_EXITS_POST);}

            int checkSympathy = sympathyDao.checkSympathy(postIdx, userIdx);
            if(checkSympathy == 1){throw new BaseException(POST_SYMPATHIES_EXITS);}

            int result = sympathyDao.createSympathy(postIdx, userIdx, sympathyIdx);
            if(result == 0){throw new BaseException(POST_SYMPATHIES_FAIL);}
            return result;
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void patchSympathyStatus(int postIdx, int userIdx) throws BaseException {
        try{
            int checkPost = sympathyDao.checkPost(postIdx);
            if(checkPost == 0){throw new BaseException(DATABASE_ERROR_NOT_EXITS_POST);}

            int checkSympathy = sympathyDao.checkSympathy(postIdx, userIdx);
            if(checkSympathy == 0){throw new BaseException(DATABASE_ERROR_NOT_EXITS_SYMPATHIES);}

            int result = sympathyDao.patchSympathyStatus(postIdx, userIdx);
            if(result == 0){throw new BaseException(PATCH_SYMPATHIES_FAIL);}
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}

