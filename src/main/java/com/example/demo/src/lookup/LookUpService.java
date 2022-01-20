package com.example.demo.src.lookup;

import com.example.demo.config.BaseException;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Service
public class LookUpService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final LookUpDao lookUpDao;
    private final JwtService jwtService;

    @Autowired
    public LookUpService(LookUpDao lookUpDao, JwtService jwtService) {
        this.lookUpDao = lookUpDao;
        this.jwtService = jwtService;

    }

    public void createLookUpProduct(int userIdx, int productIdx) throws BaseException {
        try{
            int check = lookUpDao.checkProduct(productIdx);
            if(check == 0){throw new BaseException(DATABASE_ERROR_NOT_EXIST_PRODUCT);}

            int result = lookUpDao.createLookUpProduct(userIdx, productIdx);
            if(result == 0){throw new BaseException(POST_LOOK_UPS_FAIL);}
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
