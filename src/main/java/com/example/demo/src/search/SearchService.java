package com.example.demo.src.search;


import com.example.demo.config.BaseException;
import static com.example.demo.config.BaseResponseStatus.*;
import com.example.demo.src.search.model.*;
import com.example.demo.src.user.model.PatchUserReq;
import com.example.demo.src.user.model.PostUserBlockReq;
import com.example.demo.src.user.model.PostUserReq;
import com.example.demo.src.user.model.PostUserRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


// Service Create, Update, Delete 의 로직 처리
@Service
public class SearchService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final SearchDao searchDao;
    private final SearchProvider searchProvider;
    private final JwtService jwtService;


    @Autowired
    public SearchService(SearchDao searchDao, SearchProvider searchProvider, JwtService jwtService) {
        this.searchDao = searchDao;
        this.searchProvider = searchProvider;
        this.jwtService = jwtService;

    }



/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 검색어 등록 - createSearch()  */
    @Transactional
    public void createSearch(PostSearchReq postSearchReq) throws BaseException {    //UserController.java에서 객체 값( id, nickName)을 받아와서...

        //검색어 중복 등록 검사
        if(searchProvider.checkSearch(postSearchReq) == 1){              //닉중복이 되면 결과값인 1과 매핑이 되어 중복 여부를 판단 가능
            throw new BaseException(POST_SEARCHS_CHECK);        //"이미 등록한 검색어 입니다."
        }


        try{
            //검색어 등록
            int result = searchDao.createSearch(postSearchReq);
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR_CREATE_SEARCH); //"검색어 등록에 실패하였습니다."
        }
    }



/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 최근 검색어 삭제 - deleteResentSearch()  */
    @Transactional
    public void deleteResentSearch(PatchResentSearchReq patchResentSearchReq) throws BaseException {

        try{
            //최근 검색어 삭제
            int result = searchDao.deleteResentSearch(patchResentSearchReq);
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR_DELETE_RESENT_SEARCH); //"최근 검색어 삭제에 실패하였습니다."
        }
    }


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 최근 검색어 전체 삭제 - deleteResentSearch()  */
    @Transactional
    public void deleteAllResentSearch(int userIdx) throws BaseException {

        try{
            //최근 검색어 전체 삭제
            int result = searchDao.deleteAllResentSearch(userIdx);
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR_DELETE_ALL_RESENT_SEARCH); //"최근 검색어 전체 삭제에 실패하였습니다."
        }
    }







}