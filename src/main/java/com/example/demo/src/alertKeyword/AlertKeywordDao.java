package com.example.demo.src.alertKeyword;

import com.example.demo.src.alertKeyword.model.*;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;


@Repository
public class AlertKeywordDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 알림키워드 등록-  createKeyword() */
    public int createKeyword(PostAlertKeywordReq postAlertKeywordReq){  //UserServcie.java에서 postUserReq 객체를 받아옴


        //쿼리문 생성
        String createKeywordQuery = "insert into AlertKeyword (keyword, userIdx) VALUES (?,?)";

        //객체에 userIdx와 keyword 저장
        Object[] createKeywordParams = new Object[]{postAlertKeywordReq.getKeyword(), postAlertKeywordReq.getUserIdx()};  //postUserReq의 변수명과 유사해야함

        //쿼리문 수행 (유저 생성)
        this.jdbcTemplate.update(createKeywordQuery, createKeywordParams);


        //키워드 Idx 값을 반환
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
    public GetALLAlertProductRes getAlertProducts (List<GetAlertkeywardRes> getAlertkeywardRes, int userIdx){  //List<GetAlertPrductRes>

//        System.out.println("총 키워드 개수는" + getAlertkeywardRes.size());
//
//        for(int i=0; i < getAlertkeywardRes.size(); i++ ){
//            System.out.println("키워드는" + getAlertkeywardRes.get(i).getKeyword());
//        }

        //쿼리문 생성
        String getAlertProductsQuery = "select img.image as image,\n" +
                "       CONCAT('[', ?, ' 키워드 알림]') as keyword,\n" +
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
                "and p.title LIKE CONCAT('%', ? , '%')\n" +
                "and p.regionName IN (select regionName from Region where userIdx=? and keywordAlertStatus = 1 and status = 1)\n" +
                "and p.status = 1\n" +
                "and img.firstImage = 1\n" +
                "and img.status = 1\n" +
                "order by p.createAt DESC";


        //파라미터 초기화
        Object[] getAlertProductsParams = null;             // ?에 들어갈 파라미터
        List<GetAlertPrductRes> getAlertPrductRes =null;   //1키워드 한개에 대항 상품

        GetALLAlertProductRes getALLAlertProductRes = new GetALLAlertProductRes(null);  //모든 키워드에 대한 상품
        getALLAlertProductRes.setGetAlertPrductRes(new ArrayList<List<GetAlertPrductRes>>());


        for(int i=0; i < getAlertkeywardRes.size(); i++ ) {
            //파라미터 값 생성
            getAlertProductsParams = new Object[]{getAlertkeywardRes.get(i).getKeyword(), getAlertkeywardRes.get(i).getKeyword(), userIdx};

            //쿼리문 실행
            getAlertPrductRes = this.jdbcTemplate.query(getAlertProductsQuery,
                    (rs, rowNum) -> new GetAlertPrductRes(
                            rs.getString("image"),
                            rs.getString("keyword"),
                            rs.getString("regionName"),
                            rs.getString("title"),
                            rs.getString("createAt")),
                    getAlertProductsParams);

            if(getAlertPrductRes.isEmpty()){  //빈값이면 continue
                continue;
            }
            getALLAlertProductRes.getGetAlertPrductRes().add(getAlertPrductRes);
        }


//        //총 상품 개수 파악
//        System.out.println(getALLAlertProduct.getGetAlertPrductRes().size());
//
//        //키워드별 상품 개수 파악
//        for(int i = 0; i < getALLAlertProduct.getGetAlertPrductRes().size(); i++ ) {
//            System.out.println(getALLAlertProduct.getGetAlertPrductRes());
//        }





        return getALLAlertProductRes; //모든 키워드에 해당하는 상품 리턴
    }






}

