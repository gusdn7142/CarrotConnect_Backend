package com.example.demo.src.gather;


import com.example.demo.config.BaseException;
import static com.example.demo.config.BaseResponseStatus.*;

import com.example.demo.src.alertKeyword.model.PatchAlertKeywordReq;
import com.example.demo.src.alertKeyword.model.PostAlertKeywordReq;
import com.example.demo.src.alertKeyword.model.PostAlertKeywordRes;
import com.example.demo.src.gather.model.*;

import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


// Service Create, Update, Delete 의 로직 처리
@Service
public class GatherService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final GatherDao gatherDao;
    private final GatherProvider gatherProvider;
    private final JwtService jwtService;


    @Autowired
    public GatherService(GatherDao gatherDao, GatherProvider gatherProvider, JwtService jwtService) {
        this.gatherDao = gatherDao;
        this.gatherProvider = gatherProvider;
        this.jwtService = jwtService;

    }

///////////////////////////////////////////////////////////////////////////////////////////////////
    /* 모아보기 추가 -  createGather() */
    @Transactional
    public PostGatherRes createGather(PostGatherReq postGatherReq) throws BaseException {

        //모아보기한 사용자 중복 검사
        if(gatherProvider.checkGaterUser(postGatherReq) ==1){
            throw new BaseException(POST_KEYWORDS_EXISTS_GATHER); //"이미 모아보기에 추가된 사용자에요"
        }


        //모아보기 등록
        try{
            //모아보기 등록
            int gatherIdx = gatherDao.createGather(postGatherReq);
            //return new PostUserRes(userIdx);

            //모아보기 idx와 성공메시지 리턴
            return new PostGatherRes(gatherIdx, "모아보기에 추가되었습니다.");


        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR_CREATE_GATHER);  //모아보기할 사용자를 DB에 등록하지 못하였습니다.
        }

    }


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 모아보기 취소 - deleteGather()  */
    @Transactional
    public void deleteGather(PatchGatherReq patchGatherReq) throws BaseException {    //UserController.java에서 객체 값( id, nickName)을 받아와서...

        try{
            //모아보기 취소
            int result = gatherDao.deleteGather(patchGatherReq);
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR_DELETE_GATHER);   //'모아보기 취소에 실패하였습니다.'
        }

    }












}









