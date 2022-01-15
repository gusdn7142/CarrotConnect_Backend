package com.example.demo.src.gather;

import com.example.demo.config.BaseException;
import static com.example.demo.config.BaseResponseStatus.*;

import com.example.demo.src.alertKeyword.model.GetAlertkeywardRes;
import com.example.demo.src.alertKeyword.model.PostAlertKeywordReq;
import com.example.demo.src.gather.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


//Provider : Read의 비즈니스 로직 처리
@Service
public class GatherProvider {

    private final GatherDao gatherDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public GatherProvider(GatherDao gatherDao, JwtService jwtService) {
        this.gatherDao = gatherDao;
        this.jwtService = jwtService;
    }




////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 모아보기한 사용자 중복 검사 - checkGaterUser()  */
    public int checkGaterUser(PostGatherReq postGatherReq) throws BaseException{
        try{
            return gatherDao.checkGaterUser(postGatherReq);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR_CHECK_GATHER);   //"모아보기한 사용자 중복 검사에 실패하였습니다."
        }
    }


/////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 모아보기한 사용자 조회 - getGatherUser() */
    public List<GetGatherUserRes> getGatherUser(int userIdx) throws BaseException {
        try {
            List<GetGatherUserRes> getGatherUserRes = gatherDao.getGatherUser(userIdx);
            return getGatherUserRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR_GET_GATHER_USER);  //모아보기한 사용자 조회에 실패하였습니다.
        }
    }


//////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 모아보기한 상품 조회 - getGatherProduct() */
    public List<GetGatherProductRes> getGatherProduct(int userIdx) throws BaseException {
        try {
            List<GetGatherProductRes> getGatherProductRes = gatherDao.getGatherProduct(userIdx);
            return getGatherProductRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR_GET_GATHER_PRODUCT);  //모아보기한 상품 조회에 실패하였습니다.
        }
    }






}









