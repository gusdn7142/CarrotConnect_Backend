package com.example.demo.src.alertKeyword;

import com.example.demo.src.alertKeyword.model.*;


import com.example.demo.src.user.model.GetUserRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;


@Repository
public class AlertKeywordDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }




//        SYSTEM_ID 임의로 생성 : 유저의 이메일중 아이디 앞 뒤 부분에 추가값
//        String user_id= postUserReq.getEmail().substring(0, postUserReq.getEmail().indexOf("@"));
//        postUserReq.setSystemId("YOUTUBE" + user_id + "1A3B5"); //시스템_ID 생성


//        //기본 닉네임 생성 (핸드폰 뒷번호 4자리만 붙인다.)
//        String default_nickName = "당근 유저" + postUserReq.getPhoneNumber().substring(postUserReq.getPhoneNumber().length()-4, postUserReq.getPhoneNumber().length());
//        System.out.println(default_nickName);
//
//        //인증 코드 생성 (1000번 ~ 9999번 사이)
//        int min = 1000;
//        int max = 9999;
//        int authCode = (int) ((Math.random() * (max - min)) + min);
//        System.out.println("인증 코드는" + authCode + "번 입니다.");

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 알림키워드 등록-  createKeyword() */
    public int createKeyword(PostAlertKeywordReq postAlertKeywordReq){  //UserServcie.java에서 postUserReq 객체를 받아옴


        //쿼리문 생성
        String createKeywordQuery = "insert into AlertKeyword (keyword, userIdx) VALUES (?,?)";

        //객체에 userIdx와 keyword 저장
        Object[] createKeywordParams = new Object[]{postAlertKeywordReq.getKeyword(), postAlertKeywordReq.getUserIdx()};  //postUserReq의 변수명과 유사해야함

        //쿼리문 수행 (유저 생성)
        this.jdbcTemplate.update(createKeywordQuery, createKeywordParams);


        //유저 Idx 값을 반환
        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);

//        PostKeywordRes postKeywordRes = new PostUserRes(userIdx,authCode);
//        return postUserRes;



    }


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 키워드 중복 검사 - checkKeyword() */
    public int checkKeyword(PostAlertKeywordReq postAlertKeywordReq){

        //쿼리문 생성
        String checkKeywordQuery = "select exists(select keyword from AlertKeyword where keyword = ? and userIdx = ? and status = 1)";

        //파라미터 값 생성
        Object[] checkKeywordParams = new Object[]{postAlertKeywordReq.getKeyword(), postAlertKeywordReq.getUserIdx()};


        //쿼리문 실행
        return this.jdbcTemplate.queryForObject(checkKeywordQuery,
                int.class,
                checkKeywordParams); //int형으로 쿼리 결과를 넘겨줌 (0,1)

    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 알림 키워드 삭제 - deleteKeyword()  */
    public int deleteKeyword(PatchAlertKeywordReq patchAlertKeywordReq){   //UserService.java에서 객체 값(nickName)을 받아와서...

        //쿼리문 생성
        String deleteKeywordQuery = "update AlertKeyword set status = 0 where keyword = ? and userIdx = ?";

        //파라미터 값 생성
        Object[] deleteKeywordParams = new Object[]{patchAlertKeywordReq.getKeyword(), patchAlertKeywordReq.getUserIdx()};

        //알림 키워드 삭제(비활성화) 쿼리문 수행 (0,1로 반환됨)
        return this.jdbcTemplate.update(deleteKeywordQuery,deleteKeywordParams);
    }


////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//    /* 알림 키워드 삭제 여부 확인 - checkdeleteKeyword() */
//    public int checkdeleteKeyword(PatchAlertKeywordReq patchAlertKeywordReq){
//
//        //쿼리문 생성
//        String checkKeywordQuery = "select exists(select keyword from AlertKeyword where keyword = ? and userIdx = ? and status = 0)";
//
//        //파라미터 값 생성
//        Object[] checkKeywordParams = new Object[]{patchAlertKeywordReq.getKeyword(), patchAlertKeywordReq.getUserIdx()};
//
//
//        //쿼리문 실행
//        return this.jdbcTemplate.queryForObject(checkKeywordQuery,
//                int.class,
//                checkKeywordParams); //int형으로 쿼리 결과를 넘겨줌 (0,1)
//
//    }




//////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 알림 동네 활성화 여부 확인 - checkActivateRegionstatus() */
    public int checkActivateRegionstatus(PatchAlertKeywordReq patchAlertKeywordReq){

        //쿼리문 생성
        String checkActivateRegionstatusQuery = "select exists(select keywordAlertStatus from Region where regionName  = ? and userIdx = ? and keywordAlertStatus = 1)";

        //파라미터 값 생성
        Object[] checkActivateRegionstatusParams = new Object[]{patchAlertKeywordReq.getRegionName(), patchAlertKeywordReq.getUserIdx()};


        //쿼리문 실행
        return this.jdbcTemplate.queryForObject(checkActivateRegionstatusQuery,
                int.class,
                checkActivateRegionstatusParams); //int형으로 쿼리 결과를 넘겨줌 (0,1)

    }




///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 등록한 알림 키워드 조회 - getUserProfile() */
    public List<GetAlertkeywardRes> getAlertKeyward(int userIdx){

        //쿼리문 생성
        String getAlertKeywardQuery = "select alertKeywordIdx, keyword from AlertKeyword where userIdx = ? and status = 1";

        //userIdx값 저장
        int getAlertKeywardParams = userIdx;

        //쿼리문 실행
        return this.jdbcTemplate.query(getAlertKeywardQuery,
                (rs, rowNum) -> new GetAlertkeywardRes(
                        rs.getString("alertKeywordIdx"),
                        rs.getString("keyword")),
                getAlertKeywardParams);
    }


//////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 알림 설정한 동네 조회 - getAlertRegion() */
    public List<GetAlertRegionRes> getAlertRegion(int userIdx){

        //쿼리문 생성
        String getAlertRegionQuery = "select regionName, keywordAlertStatus from Region where userIdx = ? and status = 1";

        //userIdx값 저장
        int getAlertRegionParams = userIdx;

        //쿼리문 실행
        return this.jdbcTemplate.query(getAlertRegionQuery,
                (rs, rowNum) -> new GetAlertRegionRes(
                        rs.getString("regionName"),
                        rs.getInt("keywordAlertStatus")),
                getAlertRegionParams);
    }



///////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 알림 동네 활성화 - activateRegionstatus()  */
    public int activateRegionstatus(PatchAlertKeywordReq patchAlertKeywordReq){   //UserService.java에서 객체 값(nickName)을 받아와서...

        //쿼리문 생성
        String activateRegionstatusQuery = "update Region set keywordAlertStatus = 1 where regionName = ? and userIdx = ? and keywordAlertStatus = 0";

        //파라미터 값 생성
        Object[] activateRegionstatusParams = new Object[]{patchAlertKeywordReq.getRegionName(), patchAlertKeywordReq.getUserIdx()};

        //알림 동네 변경 쿼리문 수행 (0,1로 반환됨)
        return this.jdbcTemplate.update(activateRegionstatusQuery,activateRegionstatusParams);


    }

///////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 알림 동네 비활성화 - activateRegionstatus()  */
    public int InActivateRegionstatus(PatchAlertKeywordReq patchAlertKeywordReq){   //UserService.java에서 객체 값(nickName)을 받아와서...

        //쿼리문 생성
        String InActivateRegionstatusQuery = "update Region set keywordAlertStatus = 0 where regionName = ? and userIdx = ? and keywordAlertStatus = 1";

        //파라미터 값 생성
        Object[] InActivateRegionstatusParams = new Object[]{patchAlertKeywordReq.getRegionName(), patchAlertKeywordReq.getUserIdx()};

        //알림 동네 변경 쿼리문 수행 (0,1로 반환됨)
        return this.jdbcTemplate.update(InActivateRegionstatusQuery,InActivateRegionstatusParams);


    }


///////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 키워드 알림 상품 조회 - getAlertProducts() */
    public List<GetAlertPrductRes> getAlertProducts(int userIdx){

        //쿼리문 생성
        String getAlertProductsQuery = "select img.image as image,\n" +
                "       CONCAT('[', '선풍기', ' 키워드 알림]') as keyword,\n" +
                "       p.regionName as regionName,\n" +
                "       p.title as title,\n" +
                "\n" +
                "       case when timestampdiff(second , p.createAt, current_timestamp) <60\n" +
                "           then concat(timestampdiff(second, p.createAt, current_timestamp),'초 전')\n" +
                "\n" +
                "           when timestampdiff(minute , p.createAt, current_timestamp) <60\n" +
                "           then concat(timestampdiff(minute, p.createAt, current_timestamp),'분 전')\n" +
                "\n" +
                "           when timestampdiff(hour , p.createAt, current_timestamp) <24\n" +
                "           then concat(timestampdiff(hour, p.createAt, current_timestamp),'시간 전')\n" +
                "\n" +
                "           when timestampdiff(day , p.createAt, current_timestamp) < 30\n" +
                "           then concat(timestampdiff(day, p.createAt, current_timestamp),'일 전')\n" +
                "\n" +
                "           when timestampdiff(month , p.createAt, current_timestamp) < 12\n" +
                "           then concat(timestampdiff(month, p.createAt, current_timestamp),'개월 전')\n" +
                "\n" +
                "           else concat(timestampdiff(year , p.createAt, current_timestamp), '년 전')\n" +
                "       end as createAt\n" +
                "\n" +
                "from Product p, ProductImage img\n" +
                "\n" +
                "where p.productIdx = img.productIdx\n" +
                "and p.title LIKE CONCAT('%', '선풍기' , '%')\n" +
                "and p.regionName IN (select regionName from Region where userIdx=? and keywordAlertStatus = 1 and status = 1)\n" +
                "and p.status = 1\n" +
                "and img.firstImage = 1\n" +
                "and img.status = 1\n" +
                "order by p.createAt DESC";

        //userIdx값 저장
        int getAlertProductsParams = userIdx;

        //쿼리문 실행
        return this.jdbcTemplate.query(getAlertProductsQuery,
                (rs, rowNum) -> new GetAlertPrductRes(
                        rs.getString("image"),
                        rs.getString("keyword"),
                        rs.getString("regionName"),
                        rs.getString("title"),
                        rs.getString("createAt")),
                getAlertProductsParams);
    }






}

