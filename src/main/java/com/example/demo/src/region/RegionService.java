package com.example.demo.src.region;

import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.region.model.*;
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
public class RegionService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final RegionDao regionDao;
    private final RegionProvider regionProvider;
    private final JwtService jwtService;

    @Autowired
    public RegionService(RegionDao regionDao, RegionProvider regionProvider, JwtService jwtService) {
        this.regionDao = regionDao;
        this.regionProvider = regionProvider;
        this.jwtService = jwtService;
    }

    public void createRegion(int userIdx, PostRegion postRegion) throws BaseException {
        try{
            int result = regionDao.createRegion(userIdx, postRegion);
            if(result == 0){
                //throw new BaseException(/*MODIFY_FAIL_USERNAME*/);
                System.out.println("실패, 예외는 곧 추가 예정");
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
