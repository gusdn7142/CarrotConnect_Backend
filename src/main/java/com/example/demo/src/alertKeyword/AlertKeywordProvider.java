package com.example.demo.src.alertKeyword;

import com.example.demo.config.BaseException;
import static com.example.demo.config.BaseResponseStatus.*;
import com.example.demo.src.alertKeyword.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


//Provider : Read의 비즈니스 로직 처리
@Service
public class AlertKeywordProvider {

    private final AlertKeywordDao alertkeywordDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public AlertKeywordProvider(AlertKeywordDao alertkeywordDao, JwtService jwtService) {
        this.alertkeywordDao = alertkeywordDao;
        this.jwtService = jwtService;
    }



///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 키워드 중복 검사 - checkKeyword()  */
    public int checkKeyword(PostAlertKeywordReq postAlertKeywordReq) throws BaseException{
        try{
            return alertkeywordDao.checkKeyword(postAlertKeywordReq);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR_CHECK_KEYWORD);   //"키워드 중복 검사에 실패하였습니다."
        }
    }


////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//    /* 알림 키워드 삭제 여부 확인 - checkdeleteKeyword()  */
//    public int checkdeleteKeyword(PatchAlertKeywordReq patchAlertKeywordReq) throws BaseException{
//        try{
//            return alertkeywordDao.checkdeleteKeyword(patchAlertKeywordReq);
//        } catch (Exception exception){
//            throw new BaseException(DATABASE_ERROR_CHECK_DELETE_KEYWORD);   //"키워드 삭제 여부 확인에 실패하였습니다."
//        }
//    }



//////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 알림 동네 활성화 여부 확인 - checkKeyword() */
    public int checkActivateRegionstatus(PatchAlertKeywordReq patchAlertKeywordReq) throws BaseException{
        try{
            return alertkeywordDao.checkActivateRegionstatus(patchAlertKeywordReq);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR_CHECK_ACTIVE_REGION_STATUS);   //"알림 동네 활성 상태 확인 실패하였습니다."
        }
    }


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 등록한 알림 키워드 조회 - getUserProfile() */
    public List<GetAlertkeywardRes> getAlertKeyward(int userIdx) throws BaseException {
        try {
            List<GetAlertkeywardRes> getAlertkeywardRes = alertkeywordDao.getAlertKeyward(userIdx);
            return getAlertkeywardRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR_GET_ALERT_KEYWORD);
        }
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 알림 설정한 동네 조회 - getAlertRegion() */
    public List<GetAlertRegionRes> getAlertRegion(int userIdx) throws BaseException {
        try {
            List<GetAlertRegionRes> getAlertRegionRes = alertkeywordDao.getAlertRegion(userIdx);
            return getAlertRegionRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR_GET_ALERT_REGION); //"알림 설정한 동네 조회에 실패하였습니다."
        }
    }


////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 키워드 알림 상품 조회 - getAlertProducts() */
    public GetALLAlertProductRes getAlertProducts(List<GetAlertkeywardRes> getAlertkeywardRes, int userIdx) throws BaseException {
        try {
        GetALLAlertProductRes getALLAlertProductRes = alertkeywordDao.getAlertProducts(getAlertkeywardRes, userIdx);
            return getALLAlertProductRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR_GET_ALERT_PRODUCT);
        }


    }









}









