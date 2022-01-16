package com.example.demo.src.hidden;

import com.example.demo.src.hidden.model.*;

import com.example.demo.src.user.model.GetUserBlockRes;
import com.example.demo.src.user.model.PatchUserBlockCancellReq;
import com.example.demo.src.user.model.PostUserBlockReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;





@Repository
public class HiddenDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }




/////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 미노출 사용자 추가 - hideUser()  */
    public int hideUser(PostHideUserReq postHideUserReq){   //UserService.java에서 객체 값(nickName)을 받아와서...
        //쿼리문 생성
        String hideUserQuery = "insert  into Hidden (userIdx, hiddenUserIdx) values (?,(select userIdx from User where nickName = ?))";

        //userIdx와 hiddenUserIdx를 객체에 저장
        Object[] hideUserParams = new Object[]{postHideUserReq.getUserIdx(), postHideUserReq.getHiddenNickName()};

        //사용자 차단 쿼리문 수행 (0,1로 반환됨)
        return this.jdbcTemplate.update(hideUserQuery,hideUserParams);
    }


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 미노출 사용자 추가 여부 확인 - checkHideUser()  */
    public int checkHideUser(PostHideUserReq postHideUserReq){
        String checkHideUserQuery = "select exists(select userIdx from Hidden where userIdx = ? and hiddenUserIdx = (select userIdx from User where nickName = ?) and status = 1)";

        //userIdx와 blockedUserIdx를 객체에 저장
        Object[] checkHideUserParams = new Object[]{postHideUserReq.getUserIdx(), postHideUserReq.getHiddenNickName()};

        return this.jdbcTemplate.queryForObject(checkHideUserQuery,
                int.class,
                checkHideUserParams); //int형으로 쿼리 결과를 넘겨줌 (0,1)
    }



/////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 미노출 사용자 취소 - hideUserCancell()  */
    public int hideUserCancell(PatchHideUserCancellReq patchHideUserCancellReq){
        //쿼리문 생성
        String hideUserCancellQuery = "update Hidden set status = 0 where userIdx = ? and hiddenUserIdx = (select userIdx from User where nickName = ?)";

        //userIdx와 blockedUserIdx를 객체에 저장
        Object[] hideUserCancellParams = new Object[]{patchHideUserCancellReq.getUserIdx(), patchHideUserCancellReq.getHideCancellNickName()};

        //미노출 사용자 취소 쿼리문 수행 (0,1로 반환됨)
        return this.jdbcTemplate.update(hideUserCancellQuery,hideUserCancellParams);
    }





/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* 미노출 사용자 정보 조회 - getHiddenUser() */
    public List<GetHiddenUserRes> getHiddenUser(int userIdx){

        //쿼리문 생성
        String getHiddenUserQuery = "select u.image,\n" +
                "       u.nickName,\n" +
                "       r.regionName\n" +
                "from User u\n" +
                "join (select DISTINCT hiddenUserIdx from Hidden where userIdx = ? and status = 1) h\n" +
                "     on u.userIdx = h.hiddenUserIdx\n" +
                "join Region r\n" +
                "     on u.userIdx = r.userIdx\n" +
                "where r.mainStatus = 1";

        //userIdx값 저장
        int getHiddenUserParams = userIdx;

        //쿼리문 실행
        return this.jdbcTemplate.query(getHiddenUserQuery,
                (rs, rowNum) -> new GetHiddenUserRes(
                        rs.getString("image"),
                        rs.getString("nickName"),             //각 칼럼은 DB와 매칭이 되어야 한다.
                        rs.getString("regionName")),
                getHiddenUserParams);
    }












}
