package com.example.demo.src.search;


import com.example.demo.src.alertKeyword.model.GetALLAlertProductRes;
import com.example.demo.src.alertKeyword.model.GetAlertPrductRes;
import com.example.demo.src.alertKeyword.model.GetAlertRegionRes;
import com.example.demo.src.alertKeyword.model.GetAlertkeywardRes;
import com.example.demo.src.search.model.*;

import com.example.demo.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;




@Repository
public class SearchDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }



 ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 검색어 등록 - createSearch()  */
    public int createSearch(PostSearchReq postSearchReq){   //UserService.java에서 객체 값(nickName)을 받아와서...
        //쿼리문 생성
        String createSearchQuery = "insert into Search (searchWord, userIdx) values (?,?)";

        //쿼리 파라미터 생성
        Object[] createSearchParams = new Object[]{postSearchReq.getSearchWord(), postSearchReq.getUserIdx()};

        //사용자 차단 쿼리문 수행 (0,1로 반환됨)
        return this.jdbcTemplate.update(createSearchQuery,createSearchParams);
    }


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 검색어 중복 등록 검사 - checkSearch()  */
    public int checkSearch(PostSearchReq postSearchReq){

        String checkSearchQuery = "select exists(select searchWord from Search where searchWord = ? and userIdx = ? and status = 1 )";

        //쿼리 파라미터 생성
        Object[] checkSearchParams = new Object[]{postSearchReq.getSearchWord(), postSearchReq.getUserIdx()};

        return this.jdbcTemplate.queryForObject(checkSearchQuery,
                int.class,
                checkSearchParams); //int형으로 쿼리 결과를 넘겨줌 (0,1)
    }



////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 최근 검색어 조회 - getRecentSearch() */
    public List<GetResentSearchRes> getRecentSearch (int userIdx){  //List<GetAlertPrductRes>

        //쿼리문 생성
        String getRecentSearchQuery = "select searchIdx, searchWord from Search where userIdx = ? and status = 1\n" +
                "order by createdAt DESC";

        //userIdx값 저장
        int getRecentSearchParams = userIdx;

        //쿼리문 실행
        return this.jdbcTemplate.query(getRecentSearchQuery,
                (rs, rowNum) -> new GetResentSearchRes(
                        rs.getInt("searchIdx"),
                        rs.getString("searchWord")),
                getRecentSearchParams);
    }


//////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 최근 검색어 삭제 - deleteResentSearch()  */
    public int deleteResentSearch(PatchResentSearchReq patchResentSearchReq){
        String deleteResentSearchQuery = "update Search set status = 0 where searchWord = ? and userIdx = ? and status = 1";

        //쿼리 파라미터
        Object[] deleteResentSearchParams = new Object[]{patchResentSearchReq.getSearchWord(), patchResentSearchReq.getUserIdx()};

        return this.jdbcTemplate.update(deleteResentSearchQuery,deleteResentSearchParams);
    }


////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 최근 검색어 전체 삭제 - deleteAllResentSearch()  */
    public int deleteAllResentSearch(int userIdx){
        String deleteAllResentSearchQuery = "update Search set status = 0 where userIdx = ? and status = 1";

        //쿼리 파라미터
        int deleteAllResentSearchParams = userIdx;

        return this.jdbcTemplate.update(deleteAllResentSearchQuery,deleteAllResentSearchParams);
    }


////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 연관 검색어 조회 - getRelationSearch() */
    public List<GetRelationSearchRes> getRelationSearch(String searchWord){

        //쿼리문 생성
        String getRelationSearchQuery = "select searchWord from Search group by searchWord having searchWord LIKE CONCAT('%',?,'%')";

        //쿼리 파라미터
        String getRelationSearchParams = searchWord;


        //쿼리문 실행
        return this.jdbcTemplate.query(getRelationSearchQuery,
                (rs, rowNum) -> new GetRelationSearchRes(
                        rs.getString("searchWord")),
                getRelationSearchParams);
    }


////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 검색에 따른 중고거래 글 조회 - getSearchToProduct() */
    public List<GetSearchToProductRes> getSearchToProduct(int userIdx, String searchWord){

        //쿼리문 생성
        String getSearchToProductQuery = "select p.productIdx as productIdx,\n" +
                "       pimg.image as image,\n" +
                "       p.title as title,\n" +
                "       p.regionName as regionName,\n" +
                " case when timestampdiff(second , p.createAt, current_timestamp) <60\n" +
                "      then concat(timestampdiff(second, p.createAt, current_timestamp),'초 전')\n" +
                "\n" +
                "      when timestampdiff(minute , p.createAt, current_timestamp) <60\n" +
                "      then concat(timestampdiff(minute, p.createAt, current_timestamp),'분 전')\n" +
                "\n" +
                "      when timestampdiff(hour , p.createAt, current_timestamp) <24\n" +
                "      then concat(timestampdiff(hour, p.createAt, current_timestamp),'시간 전')\n" +
                "\n" +
                "      when timestampdiff(day , p.createAt, current_timestamp) < 30\n" +
                "      then concat(timestampdiff(day, p.createAt, current_timestamp),'일 전')\n" +
                "\n" +
                "      when timestampdiff(month , p.createAt, current_timestamp) < 12\n" +
                "      then concat(timestampdiff(month, p.createAt, current_timestamp),'개월 전')\n" +
                "\n" +
                "      else concat(timestampdiff(year , p.createAt, current_timestamp), '년 전')\n" +
                "      end as createAt,\n" +
                "      concat(FORMAT(p.price,0),'원') as price,\n" +
                "       ifnull(c.chatCount,0) as chatCount,\n" +
                "       ifnull(pi.interestCount,0) as interestCount\n" +
                "\n" +
                "from Product p\n" +
                "left join(select productIdx, count(productIdx) as 'chatCount' from ChatRoom group by productIdx) c\n" +
                "    on p.productIdx = c.productIdx\n" +
                "left join(select productIdx, count(productIdx) as 'interestCount' from ProductInterest group by productIdx) as pi\n" +
                "    on p.productIdx = pi.productIdx\n" +
                "left join (select productIdx, image from ProductImage where firstImage = 1 and status = 1) pimg\n" +
                "    on p.productIdx = pimg.productIdx\n" +
                "\n" +
                "where\n" +
                " p.title LIKE CONCAT('%', ? , '%')\n" +
                "and p.regionName = (select regionName from Region where userIdx=? and mainStatus = 1 and status = 1)\n" +
                "and p.status = 1";

        //쿼리 파라미터
        Object[] getSearchToProductParams = new Object[]{searchWord, userIdx};


        //쿼리문 실행
        return this.jdbcTemplate.query(getSearchToProductQuery,
                (rs, rowNum) -> new GetSearchToProductRes(
                        rs.getInt("productIdx"),
                        rs.getString("image"),
                        rs.getString("title"),
                        rs.getString("regionName"),
                        rs.getString("createAt"),
                        rs.getString("price"),
                        rs.getInt("chatCount"),
                        rs.getInt("interestCount")),
                getSearchToProductParams);
    }




//////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 검색에 따른 동네생활 글 조회 - getSearchToTownActivity() */
    public List<GetSearchToTownActivityRes> getSearchToTownActivity(int userIdx, String searchWord){

        //쿼리문 생성
        String getSearchToTownActivityQuery = "select t.townActivityIdx as townActivityIdx,\n" +
                "       timg.image,\n" +
                "       t.topicName as topicName,\n" +
                "       t.regionName as regionName,\n" +
                " case when timestampdiff(second , t.createAt, current_timestamp) <60\n" +
                "      then concat(timestampdiff(second, t.createAt, current_timestamp),'초 전')\n" +
                "      when timestampdiff(minute , t.createAt, current_timestamp) <60\n" +
                "      then concat(timestampdiff(minute, t.createAt, current_timestamp),'분 전')\n" +
                "      when timestampdiff(hour , t.createAt, current_timestamp) <24\n" +
                "      then concat(timestampdiff(hour, t.createAt, current_timestamp),'시간 전')\n" +
                "      when timestampdiff(day , t.createAt, current_timestamp) < 30\n" +
                "      then concat(timestampdiff(day, t.createAt, current_timestamp),'일 전')\n" +
                "      when timestampdiff(month , t.createAt, current_timestamp) < 12\n" +
                "      then concat(timestampdiff(month, t.createAt, current_timestamp),'개월 전')\n" +
                "      else concat(timestampdiff(year , t.createAt, current_timestamp), '년 전')\n" +
                "      end as createAt,\n" +
                "       t.content as content,\n" +
                "       t.sympathyCount as sympathyCount,\n" +
                "       t.commentCount as commentCount\n" +
                "\n" +
                "from TownActivity t\n" +
                "left join (select townActivityIdx, firstImage, image from TownActivityImage where firstImage = 1 and status = 1) timg\n" +
                "    on t.townActivityIdx = timg.townActivityIdx\n" +
                "\n" +
                "where\n" +
                " t.content LIKE CONCAT('%', ? , '%')\n" +
                "and t.regionName = (select regionName from Region where userIdx=? and mainStatus = 1 and status = 1)\n" +
                "and t.status = 1";

        //쿼리 파라미터
        Object[] getSearchToTownActivityParams = new Object[]{searchWord, userIdx};


        //쿼리문 실행
        return this.jdbcTemplate.query(getSearchToTownActivityQuery,
                (rs, rowNum) -> new GetSearchToTownActivityRes(
                        rs.getInt("townActivityIdx"),
                        rs.getString("image"),
                        rs.getString("topicName"),
                        rs.getString("regionName"),
                        rs.getString("createAt"),
                        rs.getString("content"),
                        rs.getInt("sympathyCount"),
                        rs.getInt("commentCount")),
                getSearchToTownActivityParams);
    }


////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 검색에 따른 사용자 조회 - getSearchToUser() */
    public List<GetSearchToUserRes> getSearchToUser(int userIdx, String searchWord){

        //쿼리문 생성
        String getSearchToUserQuery = "select u.userIdx as userIdx,\n" +
                "       u.image as image,\n" +
                "       u.nickName as nickName,\n" +
                "       r.regionName as regionName\n" +
                "from Region r, User u\n" +
                "\n" +
                "where r.userIdx = u.userIdx\n" +
                "and u.nickName LIKE CONCAT('%',?,'%')\n" +
                "and u.status = 1\n" +
                "and r.status = 1\n" +
                "and r.mainStatus = 1\n" +
                "and r.regionName = (select regionName from Region r2 where r2.userIdx = (select u2.userIdx from User u2 where u2.userIdx = ?))";

        //쿼리 파라미터
        Object[] getSearchToUserParams = new Object[]{searchWord, userIdx};


        //쿼리문 실행
        return this.jdbcTemplate.query(getSearchToUserQuery,
                (rs, rowNum) -> new GetSearchToUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("image"),
                        rs.getString("nickName"),
                        rs.getString("regionName")),
                getSearchToUserParams);
    }






}
