package com.example.demo.src.hidden;

import com.example.demo.config.BaseException;
import static com.example.demo.config.BaseResponseStatus.*;
import com.example.demo.src.hidden.model.*;
import com.example.demo.src.user.model.GetUserBlockRes;
import com.example.demo.src.user.model.PostUserBlockReq;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;




//Provider : Read의 비즈니스 로직 처리
@Service
public class HiddenProvider {

    private final HiddenDao hiddenDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public HiddenProvider(HiddenDao hiddenDao, JwtService jwtService) {
        this.hiddenDao = hiddenDao;
        this.jwtService = jwtService;
    }


////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 미노출 사용자 추가 여부 확인 - checkBlcokUser()  */
    public int checkHideUser(PostHideUserReq postHideUserReq) throws BaseException{
        try{
            return hiddenDao.checkHideUser(postHideUserReq);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR_HIDDEN_CHECK_USER);   //"미노출 사용자 추가 확인에 실패하였습니다."
        }
    }



////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 미노출 사용자 정보 조회 - getHiddenUser() */
    public List<GetHiddenUserRes> getHiddenUser(int userIdx) throws BaseException {
        try {
            List<GetHiddenUserRes> getHiddenUserRes = hiddenDao.getHiddenUser(userIdx);
            return getHiddenUserRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR_HIDDEN_USER_INFO);   //"미노출 사용자 조회에 실패하였습니다."
        }
    }













}
