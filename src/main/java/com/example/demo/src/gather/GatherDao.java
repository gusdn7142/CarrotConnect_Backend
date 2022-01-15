package com.example.demo.src.gather;

import com.example.demo.src.alertKeyword.model.GetAlertkeywardRes;
import com.example.demo.src.alertKeyword.model.PatchAlertKeywordReq;
import com.example.demo.src.alertKeyword.model.PostAlertKeywordReq;
import com.example.demo.src.gather.model.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;


@Repository
public class GatherDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }





    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 모아보기 추가 -  createGather() */
    public int createGather(PostGatherReq postGatherReq){  //UserServcie.java에서 postUserReq 객체를 받아옴


        //쿼리문 생성
        String createGatherQuery = "insert into Gather (userIdx, selectUserIdx) values (?,(select userIdx from User where nickName = ?))";

        //객체에 userIdx와 keyword 저장
        Object[] createGatherParams = new Object[]{postGatherReq.getUserIdx(), postGatherReq.getNickName() };  //postUserReq의 변수명과 유사해야함

        //쿼리문 수행 (유저 생성)
        this.jdbcTemplate.update(createGatherQuery, createGatherParams);


        //모아보기 Idx 값을 반환
        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);

//        PostKeywordRes postKeywordRes = new PostUserRes(userIdx,authCode);
//        return postUserRes;



    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 모아보기한 사용자 중복 검사 - checkGaterUser()  */
    public int checkGaterUser(PostGatherReq postGatherReq){

        //쿼리문 생성
        String checkGaterUserQuery = "select exists(select selectUserIdx from Gather where userIdx = ? and selectUserIdx = (select userIdx from User where nickName = ?) and status = 1)";

        //파라미터 값 생성
        Object[] checkGaterUserParams = new Object[]{postGatherReq.getUserIdx(), postGatherReq.getNickName() };


        //쿼리문 실행
        return this.jdbcTemplate.queryForObject(checkGaterUserQuery,
                int.class,
                checkGaterUserParams); //int형으로 쿼리 결과를 넘겨줌 (0,1)

    }


/////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 모아보기 취소 - deleteGather()  */
    public int deleteGather(PatchGatherReq patchGatherReq){   //UserService.java에서 객체 값(nickName)을 받아와서...

        //쿼리문 생성
        String deleteGatherQuery = "update Gather set status = 0 where userIdx = ? and selectUserIdx = (select userIdx from User where nickName = ?)";

        //파라미터 값 생성
        Object[] deleteGatherParams = new Object[]{patchGatherReq.getUserIdx(), patchGatherReq.getNickName()};

        //모아보기 취소(비활성화) 쿼리문 수행 (0,1로 반환됨)
        return this.jdbcTemplate.update(deleteGatherQuery,deleteGatherParams);
    }


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 모아보기한 사용자 조회 - getUserProfile() */
    public List<GetGatherUserRes> getGatherUser(int userIdx){

        //쿼리문 생성
        String getGatherUserQuery = "select u.image as image,\n" +
                "       u.nickName as nickName,\n" +
                "       r.regionName as regionName\n" +
                "from Gather g, Region r, User u\n" +
                "\n" +
                "where g.selectUserIdx = r.userIdx\n" +
                "and g.selectUserIdx = u.userIdx\n" +
                "and g.userIdx = ?\n" +
                "and r.mainStatus = 1\n" +
                "and g.status = 1\n" +
                "and r.status = 1\n" +
                "and u.status = 1";

        //userIdx값 저장
        int getGatherUserParams = userIdx;

        //쿼리문 실행
        return this.jdbcTemplate.query(getGatherUserQuery,
                (rs, rowNum) -> new GetGatherUserRes(
                        rs.getString("image"),
                        rs.getString("nickName"),
                        rs.getString("regionName")),
                getGatherUserParams);
    }




/////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 모아보기한 상품 조회 - getGatherProduct() */
    public List<GetGatherProductRes> getGatherProduct(int userIdx){

        //쿼리문 생성
        String getGatherProductQuery = "select pimg.image as image,\n" +
                "       p.title as title,\n" +
                "       u.nickName as nickName,\n" +
                "       r.regionName as regionName,\n" +
                "       p.price as price,\n" +
                "       p.saleStatus as saleStatus\n" +
                "\n" +
                "from Gather g, Region r, User u  , Product p, ProductImage pimg\n" +
                "\n" +
                "where g.selectUserIdx = r.userIdx\n" +
                "and g.selectUserIdx = u.userIdx\n" +
                "and g.selectUserIdx = p.userIdx\n" +
                "and p.productIdx = pimg.productIdx\n" +
                "\n" +
                "and r.mainStatus = 1\n" +
                "and pimg.firstImage = 1\n" +
                "\n" +
                "and g.status = 1\n" +
                "and r.status = 1\n" +
                "and u.status = 1\n" +
                "and p.status = 1\n" +
                "and pimg.status = 1\n" +
                "and g.userIdx = ?\n" +
                "order by p.createAt DESC";

        //userIdx값 저장
        int getGatherProductParams = userIdx;

        //쿼리문 실행
        return this.jdbcTemplate.query(getGatherProductQuery,
                (rs, rowNum) -> new GetGatherProductRes(
                        rs.getString("image"),
                        rs.getString("title"),
                        rs.getString("nickName"),
                        rs.getString("regionName"),
                        rs.getString("price"),
                        rs.getInt("saleStatus")),
                getGatherProductParams);
    }





}

