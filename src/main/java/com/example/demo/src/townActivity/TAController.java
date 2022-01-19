package com.example.demo.src.townActivity;

import com.example.demo.src.townActivity.model.*;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import static com.example.demo.config.BaseResponseStatus.*;

import com.example.demo.src.user.UserProvider;

import com.example.demo.src.user.model.GetUserRes;
import com.example.demo.src.user.model.PatchUserReq;
import com.example.demo.utils.JwtService;
import org.apache.tomcat.util.json.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static com.example.demo.utils.ValidationRegex.*;


import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;




@RestController
@RequestMapping("town-activitiys")
public class TAController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final TAProvider taProvider;
    @Autowired
    private final TAService taService;
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final UserProvider userProvider;


    public TAController(TAProvider taProvider, TAService taService, JwtService jwtService, UserProvider userProvider) {
        this.taProvider = taProvider;
        this.taService = taService;
        this.jwtService = jwtService;
        this.userProvider = userProvider;
    }




//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 57. 동네생활 게시글 등록 API
     * [POST] /town-activitiys/:userIdx
     * @return BaseResponse<String>
     */
    // Body
    @ResponseBody
    @PostMapping("/{userIdx}")
    public BaseResponse<String> createTownPost(@PathVariable("userIdx") int userIdx, @RequestBody PostTownActivityReq postTownActivityReq) {

        //주제 입력란 NULL 값 체크
        if(postTownActivityReq.getTopicName() == null){
            return new BaseResponse<>(POST_TOWN_ACTIVITY_EMPTY_TOPICNAME);
        }

        //내용 입력란 NULL 값 체크
        if(postTownActivityReq.getContent() == null){
            return new BaseResponse<>(POST_TOWN_ACTIVITY_EMPTY_CONTENT);
        }


        try{
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


            //동네생활 게시글 등록
            postTownActivityReq.setUserIdx(userIdx);
            int townActivityIdx = taService.createTownPost(postTownActivityReq);

            //동네생활 게시글의 이미지 따로 등록
            if(postTownActivityReq.getImageList().size() != 0) {
                taService.createTownImage(postTownActivityReq, townActivityIdx);
            }


            String result = "동네생활 게시글 등록에 성공하였습니다.";

            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }




///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 58. 동네생활 게시글 조회 (전체, 지역) API
     * [GET] /town-activitiys/:userIdx/all?regionName=
     * @return BaseResponse<GetTownActivityRes>
     */

    /* GET 방식 */
    @ResponseBody
    @GetMapping("/{userIdx}/all")
    public BaseResponse<List<GetTownActivityRes>> getTownActivity(@PathVariable("userIdx") int userIdx, @RequestParam(required = false) String regionName ) {              // @RequestParam(required = false) String nickName

        try {
            /* 접근 제한 구현 */
            //DB에서 JWT를 가져와 사용자의 IDX를 추출
            //String jwt = userProvider.getUserToken(userIdx);
            //int userIdxByJwt = jwtService.getUserIdx2(jwt);

            //클라이언트에서 받아온 토큰에서 Idx 추출
            int userIdxByJwt = jwtService.getUserIdx();

            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            //로그아웃된 유저 인지 확인
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(); //토큰을 가져온다.
            userProvider.checkByUser(request.getHeader("X-ACCESS-TOKEN"));
            /*접근 제한 구현 끝 */


            /* 동네생활 게시글 조회 - getTownActivity() */
            List<GetTownActivityRes> getTownActivityRes = null;

            if(regionName == null){          //지역명 입력이 안되어 있으면
                //System.out.println(nickName);
                getTownActivityRes  = taProvider.getTownActivity(userIdx);
            }
            else {                           //지역명 입력이 되어 있으면
                getTownActivityRes  = taProvider.getTownActivitytoRegion(userIdx, regionName);
            }



            return new BaseResponse<>(getTownActivityRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }

    }





///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 59. 주제별 동네생활 게시글 조회  API
     * [GET] /town-activitiys/:userIdx/all?&topicName=
     * @return BaseResponse<GetTownActivitytoTopicRes>
     */

    /* GET 방식 */
    @ResponseBody
    @GetMapping("/{userIdx}/topic")
    public BaseResponse<List<GetTownActivitytoTopicRes>> getTownActivitytoTopic (@PathVariable("userIdx") int userIdx, @RequestParam String topicName ) {              // @RequestParam(required = false) String nickName

        try {
            /* 접근 제한 구현 */
            //DB에서 JWT를 가져와 사용자의 IDX를 추출
            //String jwt = userProvider.getUserToken(userIdx);
            //int userIdxByJwt = jwtService.getUserIdx2(jwt);

            //클라이언트에서 받아온 토큰에서 Idx 추출
            int userIdxByJwt = jwtService.getUserIdx();

            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            //로그아웃된 유저 인지 확인
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(); //토큰을 가져온다.
            userProvider.checkByUser(request.getHeader("X-ACCESS-TOKEN"));
            /*접근 제한 구현 끝 */


            /* 주제별 동네생활 게시글 조회 - getTownActivitytoTopic() */
            List<GetTownActivitytoTopicRes> getTownActivitytoTopicRes  = taProvider.getTownActivitytoTopic(userIdx, topicName);



            return new BaseResponse<>(getTownActivitytoTopicRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }

    }




///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 60. 특정 동네생활 게시글 조회  API
     * [GET] /town-activitiys/:userIdx/:townActivityIdx
     * @return BaseResponse<GetTownActivitytoTopicRes>
     */

    /* GET 방식 */
    @ResponseBody
    @GetMapping("/{userIdx}/{townActivityIdx}")
    public BaseResponse<GetTownActivitytoIdxRes> getTownActivitytoIdx (@PathVariable("userIdx") int userIdx, @PathVariable("townActivityIdx") int townActivityIdx) {              // @RequestParam(required = false) String nickName

        try {
            /* 접근 제한 구현 */
            //DB에서 JWT를 가져와 사용자의 IDX를 추출
            //String jwt = userProvider.getUserToken(userIdx);
            //int userIdxByJwt = jwtService.getUserIdx2(jwt);

            //클라이언트에서 받아온 토큰에서 Idx 추출
            int userIdxByJwt = jwtService.getUserIdx();

            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            //로그아웃된 유저 인지 확인
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(); //토큰을 가져온다.
            userProvider.checkByUser(request.getHeader("X-ACCESS-TOKEN"));
            /*접근 제한 구현 끝 */


            /* 특정 동네생활 게시글 조회 - getTownActivitytoIdx() */
            GetTownActivitytoIdxRes getTownActivitytoIdxRes  = taProvider.getTownActivitytoIdx(userIdx, townActivityIdx);



            return new BaseResponse<>(getTownActivitytoIdxRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }

    }









///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 61. 동네생활 나의 게시글 조회  API
     * [GET] /town-activitiys/:userIdx/
     * @return BaseResponse<GetTownActivityMeRes>
     */

    /* GET 방식 */
    @ResponseBody
    @GetMapping("/{userIdx}")
    public BaseResponse<List<GetTownActivityMeRes>> getTownActivityMe (@PathVariable("userIdx") int userIdx) {              // @RequestParam(required = false) String nickName

        try {
            /* 접근 제한 구현 */
            //DB에서 JWT를 가져와 사용자의 IDX를 추출
            //String jwt = userProvider.getUserToken(userIdx);
            //int userIdxByJwt = jwtService.getUserIdx2(jwt);

            //클라이언트에서 받아온 토큰에서 Idx 추출
            int userIdxByJwt = jwtService.getUserIdx();

            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            //로그아웃된 유저 인지 확인
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(); //토큰을 가져온다.
            userProvider.checkByUser(request.getHeader("X-ACCESS-TOKEN"));
            /*접근 제한 구현 끝 */



            /* 동네생활 나의 게시글 조회 - getTownActivityMe() */
            List<GetTownActivityMeRes> getTownActivityMeRes  = taProvider.getTownActivityMe(userIdx);



            return new BaseResponse<>(getTownActivityMeRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }

    }



/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//    /**
//     * 61. 동네생활 나의 게시글 세부 조회  API
//     * [GET] /town-activitiys/:userIdx/:townActivityIdx
//     * @return BaseResponse<GetTownActivityMeRes>
//     */
//
//    /* GET 방식 */
//    @ResponseBody
//    @GetMapping("/{userIdx}/detail/{townActivityIdx}")
//    public BaseResponse<GetTownActivityMeDetailRes> getTownActivityMeDetail (@PathVariable("userIdx") int userIdx, @PathVariable("townActivityIdx") int townActivityIdx) {
//
//        try {
//            /* 접근 제한 구현 */
//            //DB에서 JWT를 가져와 사용자의 IDX를 추출
//            //String jwt = userProvider.getUserToken(userIdx);
//            //int userIdxByJwt = jwtService.getUserIdx2(jwt);
//
//            //클라이언트에서 받아온 토큰에서 Idx 추출
//            int userIdxByJwt = jwtService.getUserIdx();
//
//            //userIdx와 접근한 유저가 같은지 확인
//            if(userIdx != userIdxByJwt){
//                return new BaseResponse<>(INVALID_USER_JWT);
//            }
//
//            //로그아웃된 유저 인지 확인
//            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(); //토큰을 가져온다.
//            userProvider.checkByUser(request.getHeader("X-ACCESS-TOKEN"));
//            /*접근 제한 구현 끝 */
//
//
//
//            /* 동네생활 나의 게시글 세부 조회 - getTownActivityMeDetail() */
//            GetTownActivityMeDetailRes getTownActivityMeDetailRes  = taProvider.getTownActivityMeDetail(userIdx, townActivityIdx);
//
//
//
//            return new BaseResponse<>(getTownActivityMeDetailRes);
//        } catch (BaseException exception) {
//            return new BaseResponse<>((exception.getStatus()));
//        }
//
//    }



////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 62. 동네생활 게시글 수정 API
     * [PATCH] /town-activitiys/:userIdx/:townActivityIdx
     * @return BaseResponse<String>
     */

    @ResponseBody
    @PatchMapping("/{userIdx}/{townActivityIdx}")  //jwt가 탈취될수 있기 때문에 path-variable 방식 사용
    public BaseResponse<String> modifyTownActivity(@PathVariable("userIdx") int userIdx, @PathVariable("townActivityIdx") int townActivityIdx, @RequestBody PatchTownActivityReq patchTownActivityReq){


        try {
            /* 접근 제한 구현 */
            //DB에서 JWT를 가져와 사용자의 IDX를 추출
            //String jwt = userProvider.getUserToken(userIdx);
            //int userIdxByJwt = jwtService.getUserIdx2(jwt);

            //클라이언트에서 받아온 토큰에서 Idx 추출
            int userIdxByJwt = jwtService.getUserIdx();

            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            //로그아웃된 유저 (만료된 토큰 접근)인지 확인
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(); //토큰을 가져온다.
            userProvider.checkByUser(request.getHeader("X-ACCESS-TOKEN"));
            /*접근 제한 구현 끝 */


            //체에 넣음
            patchTownActivityReq.setUserIdx(userIdx);
            patchTownActivityReq.setTownActivityIdx(townActivityIdx);

            //동네생활 나의 게시글 수정
            taService.modifyTownActivity(patchTownActivityReq);



            String result = "게시글 변경이 완료되었습니다.";   //정보 변경 성공시 메시지 지정
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));  //어떤상황에서?.. 오류 뱉어줌
        }
    }



////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 63. 동네생활 게시글 삭제 API
     * [PATCH] /town-activitiys/:userIdx/status/:townActivityIdx
     * @return BaseResponse<String>
     */

    @ResponseBody
    @PatchMapping("/{userIdx}/status/{townActivityIdx}")
    public BaseResponse<String> deleteTownActivity(@PathVariable("userIdx") int userIdx, @PathVariable("townActivityIdx") int townActivityIdx){

        try {
            /* 접근 제한 구현 */
            //DB에서 JWT를 가져와 사용자의 IDX를 추출
            //String jwt = userProvider.getUserToken(userIdx);
            //int userIdxByJwt = jwtService.getUserIdx2(jwt);

            //클라이언트에서 받아온 토큰에서 Idx 추출
            int userIdxByJwt = jwtService.getUserIdx();

            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            //로그아웃된 유저 (만료된 토큰 접근)인지 확인
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(); //토큰을 가져온다.
            userProvider.checkByUser(request.getHeader("X-ACCESS-TOKEN"));
            /*접근 제한 구현 끝 */


            //객체에 넣음
            PatchTownActivityReq patchTownActivityReq = new PatchTownActivityReq(null,null,0,null,userIdx,townActivityIdx,null,null,null);

            //동네생활 나의 게시글 수정
            taService.deleteTownActivity(patchTownActivityReq);



            String result = "게시글 삭제가 완료되었습니다.";   //정보 변경 성공시 메시지 지정
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));  //어떤상황에서?.. 오류 뱉어줌
        }
    }











////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//    /**
//     * 57. 테스트 API
//     */
//    // Body
//    @ResponseBody
//    @PostMapping("/test")
//    public int test(@RequestBody (required = false) PostTownActivityReq PostTownActivityReq) {
//
//
//
////        try {
//
//        System.out.println("json 형태 출력" + PostTownActivityReq.getImageList());
//        System.out.println("json 형태 출력" + PostTownActivityReq.getImageList().getClass().getName());
//
//        ArrayList<String> imageKeyList = new ArrayList<>();
//        ArrayList<String> imageValueList = new ArrayList<>();
//
//
//            //JSONObject imageObject = new JSONObject(imageString);
//            Iterator i = PostTownActivityReq.getImageList().keys();  //이미지 키를 받아와 저장
//
//
//            while(i.hasNext()){
//                String b = i.next().toString();
//                imageKeyList.add(b);
//
//                System.out.println(b);
//            }




//        String data = PostTownActivityReq.getImageList().toString();  // JSONObject를 String 에 담기
//        System.out.print(data);
//
//        JSONObject json = new JSONObject(PostTownActivityReq);
//        JSONObject jObj = json.optJSONObject("imageList");
//        System.out.println("찐 출력" + jObj);










//        Iterator m = PostTownActivityReq.getImageList().keys();  //이미지 키를 받아와 저장
//
//        while(m.hasNext()){
//            String b = m.next().toString();
//            imageKeyList.add(b);
//
//            System.out.println( "이게 찐이다" + b);
//        }







////        String imageList =
////                    "{" +
////                            "\"image\""+":{" +
////                            "\"image1\":\"default image1\"," +
////                            "\"image2\":\"default image2\"," +
////                            "\"image3\":\"default image3\"," +
////                            "\"image4\":\"default image4\"" +
////                            "}" +
////                    "}";
//
//
//
//            //json 객체 생성
//              JSONObject jsonObject = new JSONObject(PostTownActivityReq.getImageList());
//              System.out.println("json 객체 출력" + jsonObject);
//
//
//            //image key 값 가져옴
//               String imageString = jsonObject.getString("image");
//
//            //image value 한개값 추출출
//            JSONObject imageObject = new JSONObject(imageString);
//            Iterator i = imageObject.keys();  //이미지 키를 받아와 저장
//


//            while(i.hasNext()){
//                String b = i.next().toString();
//                imageKeyList.add(b);
//
//                System.out.println(b);
//            }
//
//
//
//            for(int j = 0; j<imageKeyList.size();j++)
//            {
//                imageValueList.add(imageObject.getString(imageKeyList.get(j)));
//                System.out.println((j+1) + "번쨰 이미지->" +imageKeyList.get(j));
//                System.out.println((j+1)+"번째 이미지->"+imageValueList.get(j));
//            }
//
//
//

//        }
//        catch (JSONException e) {
//            e.printStackTrace();
//        }



//
//        return 1;
//    }







}