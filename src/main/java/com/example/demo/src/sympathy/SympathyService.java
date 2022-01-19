package com.example.demo.src.sympathy;

import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.sympathy.model.*;
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
            int result = sympathyDao.createSympathy(postIdx, userIdx, sympathyIdx);
            if(result == 0){
                //throw new BaseException(/*MODIFY_FAIL_USERNAME*/);
                System.out.println("실패, 예외는 곧 추가 예정");
                return result;
            }
            return result;
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public String patchSympathyStatus(int postIdx, int userIdx) throws BaseException {
        try{
            int result = sympathyDao.patchSympathyStatus(postIdx, userIdx);
            String message = "공감 취소 성공";

            if(result == 0){
                //throw new BaseException(/*MODIFY_FAIL_USERNAME*/);
                System.out.println("실패, 예외는 곧 추가 예정");
                message = "취소에 실패했습니다.";
                return message;
            }
            return message;
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}

