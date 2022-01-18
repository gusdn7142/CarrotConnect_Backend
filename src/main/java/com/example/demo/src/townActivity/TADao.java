package com.example.demo.src.townActivity;


import com.example.demo.src.townActivity.model.*;

import com.example.demo.src.user.model.GetUserRes;
import com.example.demo.src.user.model.PatchUserReq;
import com.example.demo.src.user.model.PostUserReq;
import com.example.demo.src.user.model.PostUserRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;



@Repository
public class TADao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }





///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 동네 생활 게시글 등록 -  createTownPost() */
    public int createTownPost(PostTownActivityReq postTownActivityReq){


        //쿼리문 생성
        String createTownPostQuery = "insert into TownActivity (topicName, content, regionName, placeName, placeAddress, userIdx)\n" +
                "values (?, ?, (select regionName from Region where userIdx = ? and status = 1 and mainStatus = 1), ?, ?, ?)";
        Object[] createTownPostParams = new Object[]{postTownActivityReq.getTopicName(), postTownActivityReq.getContent(),postTownActivityReq.getUserIdx(), postTownActivityReq.getPlcaeName(), postTownActivityReq.getPlaceAddress(), postTownActivityReq.getUserIdx() };  //postUserReq의 변수명과 유사해야함

        //쿼리문 수행 (동네생활 게시글 생성)
        this.jdbcTemplate.update(createTownPostQuery, createTownPostParams);

        //동네생활 게시글 Idx 값을 반환
        String lastInserIdQuery = "select last_insert_id()";
        int townActivityIdx= this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);

        //PostUserRes postUserRes = new PostUserRes(userIdx,authCode);

        return townActivityIdx;

    }



////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 동네 생활 게시글의 이미지 등록 -  createTownPost() */
    public int createTownImage(PostTownActivityReq postTownActivityReq, int townActivityIdx){


        //쿼리문 생성
        String createTownImageQuery = "insert into TownActivityImage (image, firstImage, townActivityIdx)\n" +
                "values (?, ?, ?)";

        Object[] createTownImageParams = new Object[]{postTownActivityReq.getImage(), postTownActivityReq.getFirstImage(), townActivityIdx     };

        //쿼리문 수행 (동네생활 게시글 생성)
        this.jdbcTemplate.update(createTownImageQuery, createTownImageParams);

        //동네생활 게시글의 이미지 Idx 값을 반환
        String lastInserIdQuery = "select last_insert_id()";
        int townActivityImageIdx= this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);

        //PostUserRes postUserRes = new PostUserRes(userIdx,authCode);

        return townActivityImageIdx;

    }


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 전체 동네생활 게시글 조회 - getTownActivity() */
    public List<GetTownActivityRes> getTownActivity(int userIdx){

        //쿼리문 생성
        String getTownActivityQuery = "select t.townActivityIdx as townActivityIdx,\n" +
                "       r.regionName as userRegionName,\n" +
                "       t.topicName as topicName,\n" +
                "       timg.image as image,\n" +
                "       t.content as content,\n" +
                "       u.nickName as nickName,\n" +
                "       t.regionName as regionName,\n" +
                "       case when timestampdiff(second , t.createAt, current_timestamp) <60\n" +
                "           then concat(timestampdiff(second, t.createAt, current_timestamp),'초 전')\n" +
                "\n" +
                "           when timestampdiff(minute , t.createAt, current_timestamp) <60\n" +
                "           then concat(timestampdiff(minute, t.createAt, current_timestamp),'분 전')\n" +
                "\n" +
                "           when timestampdiff(hour , t.createAt, current_timestamp) <24\n" +
                "           then concat(timestampdiff(hour, t.createAt, current_timestamp),'시간 전')\n" +
                "\n" +
                "           when timestampdiff(day , t.createAt, current_timestamp) < 30\n" +
                "           then concat(timestampdiff(day, t.createAt, current_timestamp),'일 전')\n" +
                "\n" +
                "           when timestampdiff(month , t.createAt, current_timestamp) < 12\n" +
                "           then concat(timestampdiff(month, t.createAt, current_timestamp),'개월 전')\n" +
                "\n" +
                "           else concat(timestampdiff(year , t.createAt, current_timestamp), '년 전')\n" +
                "       end as createAt,\n" +
                "       t.commentCount as commentCount,\n" +
                "       t.sympathyCount as sympathyCount\n" +
                "\n" +
                "from TownActivity t left join (select townActivityIdx, image from TownActivityImage where firstImage = 1 and status= 1 ) timg\n" +
                "    on t.townActivityIdx = timg.townActivityIdx\n" +
                "join User u\n" +
                "    on t.userIdx = u.userIdx\n" +
                "\n" +
                "join (select regionName from Region where userIdx = ? and mainStatus = 1 and status = 1) r\n" +
                "    on t.regionName = r.regionName\n" +
                "\n" +
                "where t.status = 1\n" +
                "order by t.createAt desc";

        //userIdx를 객체에 저장.
        int getTownActivityParams = userIdx;      //파라미터(id) 값 저장

        //쿼리문 실행
        return this.jdbcTemplate.query(getTownActivityQuery,          //하나의 행을 불러오기 때문에 jdbcTemplate.queryForObject 실행
                (rs, rowNum) -> new GetTownActivityRes(
                        rs.getInt("townActivityIdx"),             //각 칼럼은 DB와 매칭이 되어야 한다.
                        rs.getString("userRegionName"),
                        rs.getString("topicName"),
                        rs.getString("image"),
                        rs.getString("content"),
                        rs.getString("nickName"),
                        rs.getString("regionName"),
                        rs.getString("createAt"),
                        rs.getInt("commentCount"),
                        rs.getInt("sympathyCount")),
                getTownActivityParams);
    }



//////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 지역으로 동네생활 게시글 조회  - getTownActivitytoRegion() */
    public List<GetTownActivityRes> getTownActivitytoRegion(int userIdx, String regionName){

        //쿼리문 생성
        String getTownActivitytoRegionQuery = "select t.townActivityIdx as townActivityIdx,\n" +
                "       r.regionName as userRegionName,\n" +
                "       t.topicName as topicName,\n" +
                "       timg.image as image,\n" +
                "       t.content as content,\n" +
                "       u.nickName as nickName,\n" +
                "       t.regionName as regionName,\n" +
                "       case when timestampdiff(second , t.createAt, current_timestamp) <60\n" +
                "           then concat(timestampdiff(second, t.createAt, current_timestamp),'초 전')\n" +
                "\n" +
                "           when timestampdiff(minute , t.createAt, current_timestamp) <60\n" +
                "           then concat(timestampdiff(minute, t.createAt, current_timestamp),'분 전')\n" +
                "\n" +
                "           when timestampdiff(hour , t.createAt, current_timestamp) <24\n" +
                "           then concat(timestampdiff(hour, t.createAt, current_timestamp),'시간 전')\n" +
                "\n" +
                "           when timestampdiff(day , t.createAt, current_timestamp) < 30\n" +
                "           then concat(timestampdiff(day, t.createAt, current_timestamp),'일 전')\n" +
                "\n" +
                "           when timestampdiff(month , t.createAt, current_timestamp) < 12\n" +
                "           then concat(timestampdiff(month, t.createAt, current_timestamp),'개월 전')\n" +
                "\n" +
                "           else concat(timestampdiff(year , t.createAt, current_timestamp), '년 전')\n" +
                "       end as createAt,\n" +
                "       t.commentCount as commentCount,\n" +
                "       t.sympathyCount as sympathyCount\n" +
                "\n" +
                "from TownActivity t left join (select townActivityIdx, image from TownActivityImage where firstImage = 1 and status= 1 ) timg\n" +
                "    on t.townActivityIdx = timg.townActivityIdx\n" +
                "join User u\n" +
                "    on t.userIdx = u.userIdx\n" +
                "\n" +
                "join (select regionName from Region where userIdx = ? and regionName = ? and status = 1) r\n" +
                "    on t.regionName = r.regionName\n" +
                "\n" +
                "where t.status = 1\n" +
                "order by t.createAt desc";

        //userIdx를 객체에 저장.
        Object[] getTownActivitytoRegionParams = new Object[]{userIdx, regionName};

        //쿼리문 실행
        return this.jdbcTemplate.query(getTownActivitytoRegionQuery,          //하나의 행을 불러오기 때문에 jdbcTemplate.queryForObject 실행
                (rs, rowNum) -> new GetTownActivityRes(
                        rs.getInt("townActivityIdx"),             //각 칼럼은 DB와 매칭이 되어야 한다.
                        rs.getString("userRegionName"),
                        rs.getString("topicName"),
                        rs.getString("image"),
                        rs.getString("content"),
                        rs.getString("nickName"),
                        rs.getString("regionName"),
                        rs.getString("createAt"),
                        rs.getInt("commentCount"),
                        rs.getInt("sympathyCount")),
                getTownActivitytoRegionParams);
    }



//////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 주제별 동네생활 게시글 조회 - getTownActivitytoTopic() */
    public List<GetTownActivitytoTopicRes> getTownActivitytoTopic(int userIdx, String topicName){

        //쿼리문 생성
        String getTownActivitytoTopicQuery = "select t.townActivityIdx as townActivityIdx,\n" +
                "       t.topicName as topicName,\n" +
                "       timg.image as image,\n" +
                "       t.content as content,\n" +
                "       u.nickName as nickName,\n" +
                "       t.regionName as regionName,\n" +
                "       case when timestampdiff(second , t.createAt, current_timestamp) <60\n" +
                "           then concat(timestampdiff(second, t.createAt, current_timestamp),'초 전')\n" +
                "\n" +
                "           when timestampdiff(minute , t.createAt, current_timestamp) <60\n" +
                "           then concat(timestampdiff(minute, t.createAt, current_timestamp),'분 전')\n" +
                "\n" +
                "           when timestampdiff(hour , t.createAt, current_timestamp) <24\n" +
                "           then concat(timestampdiff(hour, t.createAt, current_timestamp),'시간 전')\n" +
                "\n" +
                "           when timestampdiff(day , t.createAt, current_timestamp) < 30\n" +
                "           then concat(timestampdiff(day, t.createAt, current_timestamp),'일 전')\n" +
                "\n" +
                "           when timestampdiff(month , t.createAt, current_timestamp) < 12\n" +
                "           then concat(timestampdiff(month, t.createAt, current_timestamp),'개월 전')\n" +
                "\n" +
                "           else concat(timestampdiff(year , t.createAt, current_timestamp), '년 전')\n" +
                "       end as createAt,\n" +
                "       t.commentCount as commentCount,\n" +
                "       t.sympathyCount as sympathyCount\n" +
                "\n" +
                "from TownActivity t left join (select townActivityIdx, image from TownActivityImage where firstImage = 1 and status= 1 ) timg\n" +
                "    on t.townActivityIdx = timg.townActivityIdx\n" +
                "join User u\n" +
                "    on t.userIdx = u.userIdx\n" +
                "\n" +
                "join (select regionName from Region where userIdx = ? and mainStatus = 1 and status = 1) r\n" +
                "    on t.regionName = r.regionName\n" +
                "\n" +
                "where t.status = 1 and t.topicName = ?\n" +
                "order by t.createAt desc";

        //쿼리 파라미터 생성
        Object[] getTownActivitytoTopicParams = new Object[]{userIdx, topicName};

        //쿼리문 실행
        return this.jdbcTemplate.query(getTownActivitytoTopicQuery,          //하나의 행을 불러오기 때문에 jdbcTemplate.queryForObject 실행
                (rs, rowNum) -> new GetTownActivitytoTopicRes(
                        rs.getInt("townActivityIdx"),             //각 칼럼은 DB와 매칭이 되어야 한다.
                        rs.getString("topicName"),
                        rs.getString("image"),
                        rs.getString("content"),
                        rs.getString("nickName"),
                        rs.getString("regionName"),
                        rs.getString("createAt"),
                        rs.getInt("commentCount"),
                        rs.getInt("sympathyCount")),
                getTownActivitytoTopicParams);
    }



///////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 특정 동네생활 게시글 조회 - getTownActivitytoIdx() */
    public GetTownActivitytoIdxRes getTownActivitytoIdx(int userIdx, int townActivityIdx){

        //쿼리문 생성
        String getTownActivitytoIdxQuery = "select t.townActivityIdx as townActivityIdx,\n" +
                "       t.topicName as topicName,\n" +
                "       timg.image as image,\n" +
                "       t.content as content,\n" +
                "       u.nickName as nickName,\n" +
                "       t.regionName as regionName,\n" +
                "       r.authCount as authCount,\n" +
                "       case when timestampdiff(second , t.createAt, current_timestamp) <60\n" +
                "           then concat(timestampdiff(second, t.createAt, current_timestamp),'초 전')\n" +
                "\n" +
                "           when timestampdiff(minute , t.createAt, current_timestamp) <60\n" +
                "           then concat(timestampdiff(minute, t.createAt, current_timestamp),'분 전')\n" +
                "\n" +
                "           when timestampdiff(hour , t.createAt, current_timestamp) <24\n" +
                "           then concat(timestampdiff(hour, t.createAt, current_timestamp),'시간 전')\n" +
                "\n" +
                "           when timestampdiff(day , t.createAt, current_timestamp) < 30\n" +
                "           then concat(timestampdiff(day, t.createAt, current_timestamp),'일 전')\n" +
                "\n" +
                "           when timestampdiff(month , t.createAt, current_timestamp) < 12\n" +
                "           then concat(timestampdiff(month, t.createAt, current_timestamp),'개월 전')\n" +
                "\n" +
                "           else concat(timestampdiff(year , t.createAt, current_timestamp), '년 전')\n" +
                "       end as createAt,\n" +
                "       t.commentCount as commentCount,\n" +
                "       t.sympathyCount as sympathyCount\n" +
                "\n" +
                "from TownActivity t left join (select townActivityIdx, image from TownActivityImage where firstImage = 1 and status= 1 ) timg\n" +
                "    on t.townActivityIdx = timg.townActivityIdx\n" +
                "join User u\n" +
                "    on t.userIdx = u.userIdx\n" +
                "join Region r\n" +
                "    on u.userIdx = r.userIdx\n" +
                "\n" +
                "\n" +
                "where t.townActivityIdx = ?\n" +
                "and r.mainStatus = 1\n" +
                "and r.status = 1\n"+
                "and t.status = 1";


        //userIdx를 객체에 저장.
        int getTownActivitytoIdxParams = townActivityIdx;

        //쿼리문 실행
        return this.jdbcTemplate.queryForObject(getTownActivitytoIdxQuery,          //하나의 행을 불러오기 때문에 jdbcTemplate.queryForObject 실행
                (rs, rowNum) -> new GetTownActivitytoIdxRes(
                        rs.getInt("townActivityIdx"),             //각 칼럼은 DB와 매칭이 되어야 한다.
                        rs.getString("topicName"),
                        rs.getString("image"),
                        rs.getString("content"),
                        rs.getString("nickName"),
                        rs.getString("regionName"),
                        rs.getInt("authCount"),
                        rs.getString("createAt"),
                        rs.getInt("commentCount"),
                        rs.getInt("sympathyCount")),
                getTownActivitytoIdxParams);
    }





 /////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 동네생활 나의 게시글 조회 - getTownActivityMe() */
    public List<GetTownActivityMeRes> getTownActivityMe(int userIdx){

        //쿼리문 생성
        String getTownActivityMeQuery = "select townActivityIdx,\n" +
                "       topicName,\n" +
                "       content,\n" +
                "       commentCount\n" +
                "from TownActivity\n" +
                "where userIdx = ? and status = 1";

        //userIdx를 객체에 저장.
        int getTownActivityMeParams = userIdx;

        //쿼리문 실행
        return this.jdbcTemplate.query(getTownActivityMeQuery,          //하나의 행을 불러오기 때문에 jdbcTemplate.queryForObject 실행
                (rs, rowNum) -> new GetTownActivityMeRes(
                        rs.getInt("townActivityIdx"),             //각 칼럼은 DB와 매칭이 되어야 한다.
                        rs.getString("topicName"),
                        rs.getString("content"),
                        rs.getInt("commentCount")),
                getTownActivityMeParams);
    }




/////////////////////////////////////////////////////////////////////////////////////////////////////
//    /* 동네생활 나의 게시글 세부 조회 - getTownActivityMeDetail() */
//    public GetTownActivityMeDetailRes getTownActivityMeDetail(int userIdx, int townActivityIdx){
//
//        //쿼리문 생성
//        String getTownActivityMeDetailQuery = "select t.townActivityIdx as townActivityIdx,\n" +
//                "       t.topicName as topicName,\n" +
//                "       u.nickName as nickName ,\n" +
//                "       r.regionName as regionName,\n" +
//                "       r.authCount as authCount,\n" +
//                "       case when timestampdiff(second , t.createAt, current_timestamp) <60\n" +
//                "           then concat(timestampdiff(second, t.createAt, current_timestamp),'초 전')\n" +
//                "\n" +
//                "           when timestampdiff(minute , t.createAt, current_timestamp) <60\n" +
//                "           then concat(timestampdiff(minute, t.createAt, current_timestamp),'분 전')\n" +
//                "\n" +
//                "           when timestampdiff(hour , t.createAt, current_timestamp) <24\n" +
//                "           then concat(timestampdiff(hour, t.createAt, current_timestamp),'시간 전')\n" +
//                "\n" +
//                "           when timestampdiff(day , t.createAt, current_timestamp) < 30\n" +
//                "           then concat(timestampdiff(day, t.createAt, current_timestamp),'일 전')\n" +
//                "\n" +
//                "           when timestampdiff(month , t.createAt, current_timestamp) < 12\n" +
//                "           then concat(timestampdiff(month, t.createAt, current_timestamp),'개월 전')\n" +
//                "\n" +
//                "           else concat(timestampdiff(year , t.createAt, current_timestamp), '년 전')\n" +
//                "       end as createAt,\n" +
//                "       t.content as content,\n" +
//                "       timg.image as image,\n" +
//                "       t.sympathyCount as sympathyCount,\n" +
//                "       t.commentCount as commentCount\n" +
//                "from TownActivity t left join (select townActivityIdx, image from TownActivityImage where firstImage = 1 and status= 1 ) timg\n" +
//                "    on t.townActivityIdx = timg.townActivityIdx\n" +
//                "join User u\n" +
//                "    on t.userIdx = u.userIdx\n" +
//                "join Region r\n" +
//                "    on u.userIdx = r.userIdx\n" +
//                "\n" +
//                "\n" +
//                "where t.userIdx = ?\n" +
//                "and t.townActivityIdx = ?\n" +
//                "and r.mainStatus = 1\n" +
//                "and r.status = 1\n" +
//                "and u.status = 1\n" +
//                "and t.status = 1";
//
//        //쿼리 파라미터 생성
//        Object[] getTownActivityMeDetailParams = new Object[]{userIdx, townActivityIdx};
//
//
//        //쿼리문 실행
//        return this.jdbcTemplate.queryForObject(getTownActivityMeDetailQuery,          //하나의 행을 불러오기 때문에 jdbcTemplate.queryForObject 실행
//                (rs, rowNum) -> new GetTownActivityMeDetailRes(
//                        rs.getInt("townActivityIdx"),             //각 칼럼은 DB와 매칭이 되어야 한다.
//                        rs.getString("topicName"),
//                        rs.getString("nickName"),
//                        rs.getString("regionName"),
//                        rs.getInt("authCount"),
//                        rs.getString("createAt"),
//                        rs.getString("content"),
//                        rs.getString("image"),
//                        rs.getInt("sympathyCount"),
//                        rs.getInt("commentCount")),
//                getTownActivityMeDetailParams);
//    }



//////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /* 동네생활 게시글 주제 변경 - modifyTopicName()  */
    public int modifyTopicName(PatchTownActivityReq patchTownActivityReq){
        //쿼리문 생성
        String modifyTopicNameQuery = "update TownActivity set topicName = ? where userIdx = ? and townActivityIdx = ? and status = 1";

        //쿼리 파라미터 생성
        Object[] modifyTopicNameParams = new Object[]{patchTownActivityReq.getTopicName(), patchTownActivityReq.getUserIdx(), patchTownActivityReq.getTownActivityIdx()};

        //게시글 내용 변경 쿼리문 수행 (0,1로 반환됨)
        return this.jdbcTemplate.update(modifyTopicNameQuery,modifyTopicNameParams);
    }

    /* 동네생활 게시글 이미지 변경 - modifyImage()  */
    public int modifyImage(PatchTownActivityReq patchTownActivityReq){
        //쿼리문 생성
        String modifyImageQuery = "update TownActivityImage set image = ?, firstImage = ? where townActivityIdx = ? and status = 1";

        //닉네임과, idx를 새로운 객체에 저장
        Object[] modifyImageParams = new Object[]{patchTownActivityReq.getImage(), patchTownActivityReq.getFirstImage(), patchTownActivityReq.getTownActivityIdx()};

        //닉네임 변경 쿼리문 수행 (0,1로 반환됨)
        return this.jdbcTemplate.update(modifyImageQuery,modifyImageParams);


    }


    /* 동네생활 게시글 내용 변경 - modifyContent()  */
    public int modifyContent(PatchTownActivityReq patchTownActivityReq){
        //쿼리문 생성
        String modifyContentQuery = "update TownActivity set content = ? where userIdx = ? and townActivityIdx = ? and status = 1";

        //쿼리 파라미터 생성
        Object[] modifyContentParams = new Object[]{patchTownActivityReq.getContent(), patchTownActivityReq.getUserIdx(), patchTownActivityReq.getTownActivityIdx()};

        //게시글 내용 변경 쿼리문 수행 (0,1로 반환됨)
        return this.jdbcTemplate.update(modifyContentQuery,modifyContentParams);
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 동네생활 게시글 삭제 - deleteTownActivity()  */
    public int deleteTownActivity(PatchTownActivityReq patchTownActivityReq){
        //쿼리문 생성
        String deleteTownActivityQuery = "update TownActivity set status = 0 where userIdx = ? and townActivityIdx = ?";

        //쿼리 파라미터 생성
        Object[] deleteTownActivityParams = new Object[]{patchTownActivityReq.getUserIdx(), patchTownActivityReq.getTownActivityIdx()};

        //게시글 내용 변경 쿼리문 수행 (0,1로 반환됨)
        return this.jdbcTemplate.update(deleteTownActivityQuery,deleteTownActivityParams);
    }


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 게시글 중복 검사 - checkTownPost()  */
    public int checkTownPost(PostTownActivityReq postTownActivityReq){
        String checkTownPostQuery = "select exists(select topicName from TownActivity where topicName = ? and content = ? and userIdx = ? and status = 1)";

        //쿼리 파라미터 생성
        Object[] checkTownPostParams = new Object[]{postTownActivityReq.getTopicName(), postTownActivityReq.getContent(), postTownActivityReq.getUserIdx()};

        return this.jdbcTemplate.queryForObject(checkTownPostQuery,
                int.class,
                checkTownPostParams); //int형으로 쿼리 결과를 넘겨줌 (0,1)
    }



}
