package com.example.demo.src.region;

import com.example.demo.config.BaseException;
import com.example.demo.src.region.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public int createRegion(int userIdx, PostRegion postRegion) throws BaseException {
        try{
            int count = regionDao.checkCount(userIdx);
            if(count > 2) {throw new BaseException(POST_REGIONS_EXIST);}

            int exist = regionDao.checkRegion(userIdx, postRegion.getRegionName());
            if(exist == 1) {throw new BaseException(PATCH_REGIONS_EXITS_NOW);}

            int result = regionDao.createRegion(userIdx, postRegion);
            return result;
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void patchRegionStatus(int idx, int userIdx) throws BaseException {
        try{
            int check = regionDao.checkRegionIdx(idx);
            if(check == 0){throw new BaseException(DATABASE_ERROR_NOT_EXIST_REGION_ID);}

            int count = regionDao.checkCount(userIdx);
            if(count == 1) {throw new BaseException(PATCH_REGIONS_FAIL_MIN);}

            int access = regionDao.checkRegionAccess(idx, userIdx);
            if(access == 0) {throw new BaseException(DATABASE_ERROR_NOT_ACCESS_REGION);}

            int result = regionDao.patchRegionStatus(idx, userIdx);
            if(result == 0){throw new BaseException(PATCH_REGIONS_FAIL);}
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void patchRegionAuth(int idx, int userIdx) throws BaseException {
        try{
            int check = regionDao.checkRegionIdx(idx);
            if(check == 0){throw new BaseException(DATABASE_ERROR_NOT_EXIST_REGION_ID);}

            int now = regionDao.checkRegionNow(idx, userIdx);
            if(now == 0){throw new BaseException(PATCH_REGIONS_FAIL_NOW);}

            int access = regionDao.checkRegionAccess(idx, userIdx);
            if(access == 0) {throw new BaseException(DATABASE_ERROR_NOT_ACCESS_REGION);}

            int result = regionDao.patchRegionAuth(idx, userIdx);
            if(result == 0){throw new BaseException(PATCH_REGIONS_FAIL_AUTH);}
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void patchRegionNow(int idx, int userIdx) throws BaseException {
        try{
            int check = regionDao.checkRegionIdx(idx);
            if(check == 0){throw new BaseException(DATABASE_ERROR_NOT_EXIST_REGION_ID);}

            int now = regionDao.checkRegionNow(idx, userIdx);
            if(now == 1){throw new BaseException(PATCH_REGIONS_FAIL_NOW);}

            int access = regionDao.checkRegionAccess(idx, userIdx);
            if(access == 0) {throw new BaseException(DATABASE_ERROR_NOT_ACCESS_REGION);}

            int result = regionDao.patchRegionNow(idx, userIdx);
            if(result == 0){throw new BaseException(PATCH_REGIONS_FAIL_SET_NOW);}
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
