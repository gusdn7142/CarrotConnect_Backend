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

    public String createRegion(int userIdx, PostRegion postRegion) throws BaseException {
        try{
            int result = regionDao.createRegion(userIdx, postRegion);
            String message = "regionIdx: " + result;
            if(result == 0){
                //throw new BaseException(/*MODIFY_FAIL_USERNAME*/);
                System.out.println("실패, 예외는 곧 추가 예정");
                message = "내 동네 추가 실패";
                return message;
            }
            return message;
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public String patchRegionStatus(int idx, int userIdx) throws BaseException {
        try{
            int result = regionDao.patchRegionStatus(idx, userIdx);
            String message = "내 동네 삭제 성공";

            if(result == 0){
                //throw new BaseException(/*MODIFY_FAIL_USERNAME*/);
                System.out.println("실패, 예외는 곧 추가 예정");
                message = "삭제에 실패했습니다.";
                return message;
            }
            return message;
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public String patchRegionAuth(int idx, int userIdx) throws BaseException {
        try{
            int result = regionDao.patchRegionAuth(idx, userIdx);
            String message = "내 동네 인증 성공";

            if(result == 0){
                //throw new BaseException(/*MODIFY_FAIL_USERNAME*/);
                System.out.println("실패, 예외는 곧 추가 예정");
                message = "인증에 실패했습니다.";
                return message;
            }
            return message;
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public String patchRegionNow(int idx, int userIdx) throws BaseException {
        try{
            int result = regionDao.patchRegionNow(idx, userIdx);
            String message = "현재 내 동네 설정 성공";

            if(result == 0){
                //throw new BaseException(/*MODIFY_FAIL_USERNAME*/);
                System.out.println("실패, 예외는 곧 추가 예정");
                message = "현재 내 동네 설정에 실패했습니다.";
                return message;
            }
            return message;
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
