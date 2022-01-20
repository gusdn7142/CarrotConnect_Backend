package com.example.demo.src.townActivity;

import com.example.demo.config.BaseException;
import static com.example.demo.config.BaseResponseStatus.*;

import com.example.demo.src.townActivity.model.*;
import com.example.demo.src.user.model.GetUserRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


//Provider : Read의 비즈니스 로직 처리
@Service
public class TAProvider {

    private final TADao taDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public TAProvider(TADao taDao, JwtService jwtService) {
        this.taDao = taDao;
        this.jwtService = jwtService;
    }




////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 전체 동네생활 게시글 조회 - getTownActivity() */
    public List<GetTownActivityRes> getTownActivity(int userIdx) throws BaseException {

        try {
            List<GetTownActivityRes> getTownActivityRes = taDao.getTownActivity(userIdx);
            return getTownActivityRes;
        } catch (Exception exception) {    //에러가 있다면 (의미적 validation 처리)
            throw new BaseException(DATABASE_ERROR_TOWN_ACTIVITY_INFO);
        }
    }



/////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 지역으로 동네생활 게시글 조회  - getTownActivitytoRegion() */
    public List<GetTownActivityRes> getTownActivitytoRegion(int userIdx, String regionName) throws BaseException {

        try {
            List<GetTownActivityRes> getTownActivityRes = taDao.getTownActivitytoRegion(userIdx, regionName);
            return getTownActivityRes;
        } catch (Exception exception) {    //에러가 있다면 (의미적 validation 처리)
            throw new BaseException(DATABASE_ERROR_TOWN_ACTIVITY_INFO);
        }
    }


//////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 주제별 동네생활 게시글 조회  - getTownActivitytoTopic() */
    public List<GetTownActivitytoTopicRes> getTownActivitytoTopic(int userIdx, String topicName) throws BaseException {

        try {
            List<GetTownActivitytoTopicRes> getTownActivitytoTopicRes = taDao.getTownActivitytoTopic(userIdx, topicName);

            return getTownActivitytoTopicRes;
        } catch (Exception exception) {    //에러가 있다면 (의미적 validation 처리)
            throw new BaseException(DATABASE_ERROR_TOWN_ACTIVITY_INFO);
        }
    }


 ///////////////////////////////////////////////////////////////////////////////////////////////////
    /* 특정 동네생활 게시글 조회  - getTownActivitytoIdx() */
    public GetTownActivitytoIdxRes getTownActivitytoIdx(int userIdx, int townActivityIdx) throws BaseException {

        try {
            GetTownActivitytoIdxRes getTownActivitytoIdxRes = taDao.getTownActivitytoIdx(userIdx, townActivityIdx);

            return getTownActivitytoIdxRes;
        } catch (Exception exception) {    //에러가 있다면 (의미적 validation 처리)
            throw new BaseException(DATABASE_ERROR_TOWN_ACTIVITY_INFO);
        }



    }


/////////////////////////////////////////////////////////////////////////////////////////////////
    /* 동네생활 나의 게시글 목록 조회  - getTownActivityMe() */
    public List<GetTownActivityMeRes> getTownActivityMe(int userIdx) throws BaseException {

        try {
            List<GetTownActivityMeRes> getTownActivityMeRes = taDao.getTownActivityMe(userIdx);

            return getTownActivityMeRes;
        } catch (Exception exception) {    //에러가 있다면 (의미적 validation 처리)
            throw new BaseException(DATABASE_ERROR_TOWN_ACTIVITY_INFO);
        }

    }




//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 게시글 중복 검사  - checkTownPost()  */
    public int checkTownPost(PostTownActivityReq postTownActivityReq) throws BaseException{
        try{
            return taDao.checkTownPost(postTownActivityReq);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR_CHECK_TOWN_ACTIVITY);   //"게시글 중복 검사에 실패하였습니다."
        }
    }





}








