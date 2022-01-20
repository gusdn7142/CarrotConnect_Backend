package com.example.demo.src.alertKeyword;


import com.example.demo.config.BaseException;
import static com.example.demo.config.BaseResponseStatus.*;
import com.example.demo.src.alertKeyword.model.*;

import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


// Service Create, Update, Delete 의 로직 처리
@Service
public class AlertKeywordService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final AlertKeywordDao alertKeywordDao;
    private final AlertKeywordProvider alertKeywordProvider;
    private final JwtService jwtService;


    @Autowired
    public AlertKeywordService(AlertKeywordDao alertKeywordDao, AlertKeywordProvider alertKeywordProvider, JwtService jwtService) {
        this.alertKeywordDao = alertKeywordDao;
        this.alertKeywordProvider = alertKeywordProvider;
        this.jwtService = jwtService;

    }


//////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 알림키워드 등록-  createKeyword() */
    @Transactional
    public PostAlertKeywordRes createKeyword(PostAlertKeywordReq postAlertKeywordReq) throws BaseException {

        //키워드 중복 검사
        if(alertKeywordProvider.checkKeyword(postAlertKeywordReq) ==1){
            throw new BaseException(POST_KEYWORDS_EXISTS_KEYWORD); //"이미 추가된 키워드에요"
        }


        //키워드 등록
        try{
            //키워드 등록
            int alertKeywordIdx = alertKeywordDao.createKeyword(postAlertKeywordReq);
            //return new PostUserRes(userIdx);

            //키워드 idx
            return new PostAlertKeywordRes(alertKeywordIdx);


        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR_CREATE_KEYWORD);  //키워드를 DB에 등록하지 못하였습니다.
        }


    }


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 알림 키워드 삭제 - deleteKeyword()  */
    @Transactional
    public void deleteKeyword(PatchAlertKeywordReq patchAlertKeywordReq) throws BaseException {    //UserController.java에서 객체 값( id, nickName)을 받아와서...

        try{
            //알림 키워드 삭제
            int result = alertKeywordDao.deleteKeyword(patchAlertKeywordReq);
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR_DELETE_KEYWORD);   //'알림 키워드 삭제에 실패하였습니다.'
        }
    }



//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 알림 동네 활성화 - activateRegionstatus()  */
    @Transactional
    public void activateRegionstatus(PatchAlertKeywordReq patchAlertKeywordReq) throws BaseException {    //UserController.java에서 객체 값( id, nickName)을 받아와서...

        //알림 동네 활성화 여부 확인
        if(alertKeywordProvider.checkActivateRegionstatus(patchAlertKeywordReq) == 1){  //이미 활성화 되어 있다면
            throw new BaseException(PATCH_KEYWORDS_ACTIVE_REGION);      //"이미 알림 동네가 활성화된 상태입니다"
        }


        try{
            //알림 동네 활성화
            int result = alertKeywordDao.activateRegionstatus(patchAlertKeywordReq);
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR_ACTIVE_REGION_STATUS);   //'알림 동네 활성화에 실패하였습니다.'
        }


    }



///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 알림 동네 비활성화 - InActivateRegionstatus()  */
    @Transactional
    public void InActivateRegionstatus(PatchAlertKeywordReq patchAlertKeywordReq) throws BaseException {    //UserController.java에서 객체 값( id, nickName)을 받아와서...

        //알림 동네 활성화 여부 확인
        if(alertKeywordProvider.checkActivateRegionstatus(patchAlertKeywordReq) == 0){  //이미 비활성화 되어 있다면
            throw new BaseException(PATCH_KEYWORDS_INACTIVE_REGION);      //"이미 알림 동네가 비활성화된 상태입니다"
        }


        try{
            //알림 동네 활성화
            int result = alertKeywordDao.InActivateRegionstatus(patchAlertKeywordReq);
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR_INACTIVE_REGION_STATUS);   //'알림 동네 비활성화에 실패하였습니다.'
        }


    }









}









