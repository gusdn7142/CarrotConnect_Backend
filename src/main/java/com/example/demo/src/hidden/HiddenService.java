package com.example.demo.src.hidden;

import com.example.demo.config.BaseException;
import static com.example.demo.config.BaseResponseStatus.*;

import com.example.demo.src.hidden.model.PatchHideUserCancellReq;
import com.example.demo.src.hidden.model.PostHideUserReq;
import com.example.demo.src.user.model.PatchUserBlockCancellReq;
import com.example.demo.src.user.model.PostUserBlockReq;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;






// Service Create, Update, Delete 의 로직 처리
@Service
public class HiddenService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final HiddenDao hiddenDao;
    private final HiddenProvider hiddenProvider;
    private final JwtService jwtService;


    @Autowired
    public HiddenService(HiddenDao hiddenDao, HiddenProvider hiddenProvider, JwtService jwtService) {
        this.hiddenDao = hiddenDao;
        this.hiddenProvider = hiddenProvider;
        this.jwtService = jwtService;

    }




//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 미노출 사용자 추가 - hideUser()  */
    public void hideUser(PostHideUserReq postHideUserReq) throws BaseException {

        //미노출 사용자 추가 여부 확인
        if(hiddenProvider.checkHideUser(postHideUserReq) == 1){              //닉네임이 중복이 되면 결과값인 1과 매핑이 되어 중복 여부를 판단 가능
            throw new BaseException(POST_USERS_HIDDEN_NICKNAME);        //"이미 미노출된 사용자입니다.."
        }

        try{
            //미노출 사용자 추가
            int result = hiddenDao.hideUser(postHideUserReq);
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR_HIDDEN_USER); //"미노출 사용자 추가에 실패했습니다. 닉네임을 확인해 주세요."
        }



    }



//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 미노출 사용자 취소 - hideUserCancell()  */
    public void hideUserCancell(PatchHideUserCancellReq patchHideUserCancellReq) throws BaseException {

        try{
            //미노출 사용자 취소
            int result = hiddenDao.hideUserCancell(patchHideUserCancellReq);
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR_HIDDEN_CANCELL_USER); //"미노출 사용자 해제에 실패했습니다. 닉네임을 확인해 주세요."
        }
    }







}