package com.example.demo.src.lookup;

import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.utils.AES128;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

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

    public String createLookUpProduct(int userIdx, int productIdx) throws BaseException {
        try{
            int result = lookUpDao.createLookUpProduct(userIdx, productIdx);
            String message = "lookupIdx: " + result;
            if(result == 0){
                //throw new BaseException(/*MODIFY_FAIL_USERNAME*/);
                System.out.println("실패, 예외는 곧 추가 예정");
                message = "조회한 상품 등록 실패";
                return message;
            }
            return message;
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
