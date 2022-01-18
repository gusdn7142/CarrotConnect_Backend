package com.example.demo.src.search;


import com.example.demo.config.BaseException;
import static com.example.demo.config.BaseResponseStatus.*;

import com.example.demo.src.alertKeyword.model.GetAlertkeywardRes;
import com.example.demo.src.search.model.*;
import com.example.demo.src.user.model.GetUserRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;





//Provider : Read의 비즈니스 로직 처리
@Service
public class SearchProvider {

    private final SearchDao searchDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public SearchProvider(SearchDao searchDao, JwtService jwtService) {
        this.searchDao = searchDao;
        this.jwtService = jwtService;
    }



//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 검색어 중복 등록 검사 - checkSearch()  */
    public int checkSearch(PostSearchReq postSearchReq) throws BaseException{
        try{
            return searchDao.checkSearch(postSearchReq);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR_CHECK_SEARCH);   //"검색어 중복 등록 검사에 실패하였습니다."
        }
    }


////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 연관 검색어 조회 - getRelationSearch() */
    public List<GetRelationSearchRes> getRelationSearch(String searchWord) throws BaseException {
        try {
            List<GetRelationSearchRes> getRelationSearchRes = searchDao.getRelationSearch(searchWord);
            return getRelationSearchRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR_GET_RELATION_SEARCH);  //"연관 검색어 조회에 실패하였습니다."
        }
    }



//////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 최근 검색어 조회 - getRecentSearch() */
    public List<GetResentSearchRes> getRecentSearch(int userIdx) throws BaseException {
        try {
            List<GetResentSearchRes> getResentSearchRes = searchDao.getRecentSearch(userIdx);
            return getResentSearchRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR_GET_RECENT_SEARCH);  //"최근 검색어 조회에 실패하였습니다."
        }
    }


///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 검색에 따른 중고거래 글 조회 - getSearchToProduct() */
    public List<GetSearchToProductRes> getSearchToProduct(int userIdx, String searchWord) throws BaseException {
        try {
            List<GetSearchToProductRes> getSearchToProductRes = searchDao.getSearchToProduct(userIdx, searchWord);
            return getSearchToProductRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR_GET_SEARCH_PRODUCT);  //"검색에 맞는 중고거래 글을 불러오지 못하였습니다."
        }
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 검색에 따른 동네생활 글 조회 - getSearchToTownActivity() */
    public List<GetSearchToTownActivityRes> getSearchToTownActivity(int userIdx, String searchWord) throws BaseException {
        try {
            List<GetSearchToTownActivityRes> getSearchToTownActivityRes = searchDao.getSearchToTownActivity(userIdx, searchWord);
            return getSearchToTownActivityRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR_GET_SEARCH_TOWN_ACTIVITY);  //"검색에 맞는 동네생활 글을 불러오지 못하였습니다."
        }
    }


/////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 검색에 따른 사용자 조회 - getSearchToUser() */
    public List<GetSearchToUserRes> getSearchToUser(int userIdx, String searchWord) throws BaseException {
        try {
            List<GetSearchToUserRes> getSearchToUserRes = searchDao.getSearchToUser(userIdx, searchWord);
            return getSearchToUserRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR_GET_SEARCH_USER);  //"검색에 맞는 사용자들을 불러오지 못하였습니다."
        }
    }



}