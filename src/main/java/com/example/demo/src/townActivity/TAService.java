package com.example.demo.src.townActivity;

import com.example.demo.config.BaseException;
import static com.example.demo.config.BaseResponseStatus.*;
import com.example.demo.src.townActivity.model.*;
import com.example.demo.src.user.model.PatchUserReq;
import com.example.demo.src.user.model.PostUserReq;
import com.example.demo.src.user.model.PostUserRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;





// Service Create, Update, Delete 의 로직 처리
@Service
public class TAService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final TADao taDao;
    private final TAProvider taProvider;
    private final JwtService jwtService;


    @Autowired
    public TAService(TADao taDao, TAProvider taProvider, JwtService jwtService) {
        this.taDao = taDao;
        this.taProvider = taProvider;
        this.jwtService = jwtService;

    }



/////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 동네생활 게시글 등록 -  createTownPost() */
    public int createTownPost(PostTownActivityReq postTownActivityReq) throws BaseException {

        //게시글 중복 검사 (이 유저가 이 주제와 내용으로 작성한 적이 있는지 검사)
        if(taProvider.checkTownPost (postTownActivityReq) ==1){              //전화번호가 중복이 되면 결과값인 1과 매핑이 되어 중복 여부를 판단 가능
            throw new BaseException(POST_TOWNACTIVITY_EXISTS);         //"이미 작성하셨던 게시글 입니다."
        }

        try{
            //동네 생활 게시글 등록
            int townActivityIdx = taDao.createTownPost(postTownActivityReq);
            return townActivityIdx;

        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR_CREATE_TOWN_ACTIVITY);  //동네 생활 게시글을 DB에 등록하지 못하였습니다.
        }

    }


/////////////////////////////////////////////////////////////////////////////////////////////////
    /* 동네생활 게시글의 이미지 등록 -  createTownImage() */
    public void createTownImage(PostTownActivityReq postTownActivityReq, int townActivityIdx) throws BaseException {

        try{
            //동네 생활 게시글의 이미지 등록
            int townActivityImageIdx = taDao.createTownImage(postTownActivityReq, townActivityIdx);
            //return new PostUserRes(userIdx);

        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR_CREATE_TOWN_ACTIVITY_IMAGE);  //동네 생활 게시글 등록 과정에서 이미지를 DB에 등록하지 못하였습니다.
        }

    }



////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 동네생활 게시글 수정 - modifyTownActivity()  */
    public void modifyTownActivity(PatchTownActivityReq patchTownActivityReq) throws BaseException {

        try{
            //동네생활 게시글 주제 변경 (null이 아니면)
            if(patchTownActivityReq.getTopicName() != null){
                //게시글 주제 변경
                int result = taDao.modifyTopicName(patchTownActivityReq);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR_MODIFY_TOWN_ACTIVITY_TOPICNAME);   //"게시글 주제 변경시 오류가 발생하였습니다."
        }

        try{
            //동네생활 게시글 이미지 변경 (null이 아니면)
            if(patchTownActivityReq.getImage() != null){
                //이미지 변경
                int result = taDao.modifyImage(patchTownActivityReq);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR_MODIFY_TOWN_ACTIVITY_IMAGE);   //"이미지 변경시 오류가 발생하였습니다."
        }

        try{
            //동네생활 게시글 내용 변경 (null이 아니면)
            if(patchTownActivityReq.getContent() != null){
                //게시글 정보 변경
                int result = taDao.modifyContent(patchTownActivityReq);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR_MODIFY_TOWN_ACTIVITY_CONTENT);   //"게시글 내용 변경시 오류가 발생하였습니다."
        }





    }



////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 동네생활 게시글 삭제 - deleteTownActivity()  */
    public void deleteTownActivity(PatchTownActivityReq patchTownActivityReq) throws BaseException {

        try{
            int result = taDao.deleteTownActivity(patchTownActivityReq);
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR_DELETE_TOWN_ACTIVITY);   //"게시글 삭제시 오류가 발생하였습니다."
        }

    }




}

