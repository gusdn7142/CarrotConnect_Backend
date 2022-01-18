package com.example.demo.src.search;


import com.example.demo.src.alertKeyword.model.GetAlertkeywardRes;
import com.example.demo.src.search.model.*;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import static com.example.demo.config.BaseResponseStatus.*;

import com.example.demo.src.user.UserProvider;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@RequestMapping("searchs")
public class SearchController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final SearchProvider searchProvider;
    @Autowired
    private final SearchService searchService;
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final UserProvider userProvider;


    public SearchController(SearchProvider searchProvider, SearchService searchService, JwtService jwtService, UserProvider userProvider) {
        this.searchProvider = searchProvider;
        this.searchService = searchService;
        this.jwtService = jwtService;
        this.userProvider = userProvider;
    }



//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 72. 검색어 등록 API
     * [Post] /searchs/:userIdx
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PostMapping("/{userIdx}")
    public BaseResponse<String> createSearch(@PathVariable("userIdx") int userIdx, @RequestBody PostSearchReq postSearchReq){   //BaseResponse<String>

        try {
            /* 접근 제한 구현 */
            //DB에서 JWT를 가져와 사용자의 IDX를 추출
            //String jwt = userProvider.getUserToken(userIdx);
            //int userIdxByJwt = jwtService.getUserIdx2(jwt);

            //(jwt 토큰 만료 여부 확인 +) 클라이언트에서 받아온 토큰에서 Idx 추출
            int userIdxByJwt = jwtService.getUserIdx();

            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            //로그아웃된 유저 인지 확인
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(); //토큰을 가져온다.
            userProvider.checkByUser(request.getHeader("X-ACCESS-TOKEN"));
            /*접근 제한 구현 끝 */


            //검색어 등록
            postSearchReq.setUserIdx(userIdx);
            searchService.createSearch(postSearchReq);


            String result = postSearchReq.getSearchWord() + "에 대한 검색어 등록에 성공하였습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));  //어떤상황에서?.. 오류 뱉어줌
        }



    }


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 73. 최근 검색어 조회 API
     * [GET] /searchs/:userIdx/recent
     * @return BaseResponse<GetResentSearchRes>
     */

    @ResponseBody                // JSON 혹은 xml 로 요청에 응답할수 있게 해주는 Annotation
    @GetMapping("/{userIdx}/recent")
    public BaseResponse<List<GetResentSearchRes>> getRecentSearch(@PathVariable("userIdx") int userIdx) {              //BaseResponse<GetUserRes>

        try {
            /* 접근 제한 구현 */
            //DB에서 JWT를 가져와 사용자의 IDX를 추출
            //String jwt = userProvider.getUserToken(userIdx);
            //int userIdxByJwt = jwtService.getUserIdx2(jwt);

            //(jwt 토큰 만료 여부 확인 +) 클라이언트에서 받아온 토큰에서 Idx 추출
            int userIdxByJwt = jwtService.getUserIdx();

            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            //로그아웃된 유저 인지 확인
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(); //토큰을 가져온다.
            userProvider.checkByUser(request.getHeader("X-ACCESS-TOKEN"));
            /*접근 제한 구현 끝 */


            //최근 검색어 조회 - getRecentSearch()
            List<GetResentSearchRes> getResentSearchRes = searchProvider.getRecentSearch(userIdx);



            return new BaseResponse<>(getResentSearchRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }

    }



//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 74. 최근 검색어 삭제 API
     * [Patch] /searchs/:userIdx/recent-status
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{userIdx}/recent-status")
    public BaseResponse<String> deleteResentSearch(@PathVariable("userIdx") int userIdx, @RequestBody PatchResentSearchReq patchResentSearchReq){

        try {
            /* 접근 제한 구현 */
            //DB에서 JWT를 가져와 사용자의 IDX를 추출
            //String jwt = userProvider.getUserToken(userIdx);
            //int userIdxByJwt = jwtService.getUserIdx2(jwt);

            //(jwt 토큰 만료 여부 확인 +) 클라이언트에서 받아온 토큰에서 Idx 추출
            int userIdxByJwt = jwtService.getUserIdx();

            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            //로그아웃된 유저 인지 확인
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(); //토큰을 가져온다.
            userProvider.checkByUser(request.getHeader("X-ACCESS-TOKEN"));
            /*접근 제한 구현 끝 */


            //최근 검색어 삭제
            patchResentSearchReq.setUserIdx(userIdx);
            searchService.deleteResentSearch(patchResentSearchReq);


            String result = "검색어 " + patchResentSearchReq.getSearchWord() + "가(이) 삭제되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));  //어떤상황에서?.. 오류 뱉어줌
        }
    }



//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 75. 최근 검색어 전체 삭제 API
     * [Patch] /searchs/:userIdx/all-recent-status
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{userIdx}/all-recent-status")
    public BaseResponse<String> deleteAllResentSearch(@PathVariable("userIdx") int userIdx){

        try {
            /* 접근 제한 구현 */
            //DB에서 JWT를 가져와 사용자의 IDX를 추출
            //String jwt = userProvider.getUserToken(userIdx);
            //int userIdxByJwt = jwtService.getUserIdx2(jwt);

            //(jwt 토큰 만료 여부 확인 +) 클라이언트에서 받아온 토큰에서 Idx 추출
            int userIdxByJwt = jwtService.getUserIdx();

            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            //로그아웃된 유저 인지 확인
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(); //토큰을 가져온다.
            userProvider.checkByUser(request.getHeader("X-ACCESS-TOKEN"));
            /*접근 제한 구현 끝 */


            //최근 검색어 전체 삭제
            searchService.deleteAllResentSearch(userIdx);


            String result = "최근 검색어가 모두 삭제되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));  //어떤상황에서?.. 오류 뱉어줌
        }
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 76. 연관 검색어 조회 API
     * [GET] /searchs/:userIdx/relation?searchWord=
     * @return BaseResponse<GetResentSearchRes>
     */

    @ResponseBody
    @GetMapping("/{userIdx}/relation")
    public BaseResponse<List<GetRelationSearchRes>> getRelationSearch(@PathVariable("userIdx") int userIdx,  @RequestParam String searchWord) {              //BaseResponse<GetUserRes>

        try {
            /* 접근 제한 구현 */
            //DB에서 JWT를 가져와 사용자의 IDX를 추출
            //String jwt = userProvider.getUserToken(userIdx);
            //int userIdxByJwt = jwtService.getUserIdx2(jwt);

            //(jwt 토큰 만료 여부 확인 +) 클라이언트에서 받아온 토큰에서 Idx 추출
            int userIdxByJwt = jwtService.getUserIdx();

            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            //로그아웃된 유저 인지 확인
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(); //토큰을 가져온다.
            userProvider.checkByUser(request.getHeader("X-ACCESS-TOKEN"));
            /*접근 제한 구현 끝 */


            //연관 검색어 조회 - getRelationSearch()
            List<GetRelationSearchRes> getRelationSearchRes = searchProvider.getRelationSearch(searchWord);



            return new BaseResponse<>(getRelationSearchRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }

    }



///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 77. 검색에 따른 중고거래 글 조회 API
     * [GET] /searchs/:userIdx/products?searchWord=
     * @return BaseResponse<GetSearchToProductRes>
     */

    @ResponseBody
    @GetMapping("/{userIdx}/products")
    public BaseResponse<List<GetSearchToProductRes>> getSearchToProduct(@PathVariable("userIdx") int userIdx,  @RequestParam String searchWord) {              //BaseResponse<GetUserRes>

        try {
            /* 접근 제한 구현 */
            //DB에서 JWT를 가져와 사용자의 IDX를 추출
            //String jwt = userProvider.getUserToken(userIdx);
            //int userIdxByJwt = jwtService.getUserIdx2(jwt);

            //(jwt 토큰 만료 여부 확인 +) 클라이언트에서 받아온 토큰에서 Idx 추출
            int userIdxByJwt = jwtService.getUserIdx();

            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            //로그아웃된 유저 인지 확인
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(); //토큰을 가져온다.
            userProvider.checkByUser(request.getHeader("X-ACCESS-TOKEN"));
            /*접근 제한 구현 끝 */



            //검색에 따른 중고거래 글 조회 - getSearchToProduct()
            List<GetSearchToProductRes> getSearchToProductRes = searchProvider.getSearchToProduct(userIdx,searchWord);


            return new BaseResponse<>(getSearchToProductRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }




    }



///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 78. 검색에 따른 동네생활 글 조회 API
     * [GET] /searchs/:userIdx/town-activitys?searchWord=
     * @return BaseResponse<GetSearchToTownActivityRes>
     */

    @ResponseBody
    @GetMapping("/{userIdx}/town-activitys")
    public BaseResponse<List<GetSearchToTownActivityRes>> getSearchToTownActivity(@PathVariable("userIdx") int userIdx,  @RequestParam String searchWord) {              //BaseResponse<GetUserRes>

        try {
            /* 접근 제한 구현 */
            //DB에서 JWT를 가져와 사용자의 IDX를 추출
            //String jwt = userProvider.getUserToken(userIdx);
            //int userIdxByJwt = jwtService.getUserIdx2(jwt);

            //(jwt 토큰 만료 여부 확인 +) 클라이언트에서 받아온 토큰에서 Idx 추출
            int userIdxByJwt = jwtService.getUserIdx();

            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            //로그아웃된 유저 인지 확인
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(); //토큰을 가져온다.
            userProvider.checkByUser(request.getHeader("X-ACCESS-TOKEN"));
            /*접근 제한 구현 끝 */



            //검색에 따른 동네생활 글 조회 - getSearchToTownActivity()
            List<GetSearchToTownActivityRes> getSearchToTownActivityRes = searchProvider.getSearchToTownActivity(userIdx,searchWord);




            return new BaseResponse<>(getSearchToTownActivityRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }



    }




///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 78. 검색에 따른 사용자 조회 API
     * [GET] /searchs/:userIdx/users?searchWord=
     * @return BaseResponse<GetSearchToUserRes>
     */

    @ResponseBody
    @GetMapping("/{userIdx}/users")
    public BaseResponse<List<GetSearchToUserRes>> getSearchToUser(@PathVariable("userIdx") int userIdx,  @RequestParam String searchWord) {              //BaseResponse<GetUserRes>

        try {
            /* 접근 제한 구현 */
            //DB에서 JWT를 가져와 사용자의 IDX를 추출
            //String jwt = userProvider.getUserToken(userIdx);
            //int userIdxByJwt = jwtService.getUserIdx2(jwt);

            //(jwt 토큰 만료 여부 확인 +) 클라이언트에서 받아온 토큰에서 Idx 추출
            int userIdxByJwt = jwtService.getUserIdx();

            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            //로그아웃된 유저 인지 확인
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(); //토큰을 가져온다.
            userProvider.checkByUser(request.getHeader("X-ACCESS-TOKEN"));
            /*접근 제한 구현 끝 */


            //검색에 따른 사용자 조회 - getSearchToUser()
            List<GetSearchToUserRes> getSearchToUserRes = searchProvider.getSearchToUser(userIdx,searchWord);


            return new BaseResponse<>(getSearchToUserRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }



    }






}
