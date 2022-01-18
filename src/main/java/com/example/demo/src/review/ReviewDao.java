package com.example.demo.src.review;

import com.example.demo.src.review.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ReviewDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Transactional
    public int createReview(int senderIdx, int receiverIdx, int productIdx, PostReview postReview){
        String createReviewQuery = "insert into DealReview (content, productIdx, senderIdx, receiverIdx, preference) values (?, ?, ?, ?, ?) ";
        Object[] createReviewParams = new Object[]{postReview.getContent(), productIdx, senderIdx, receiverIdx, postReview.getPreference()};
        this.jdbcTemplate.update(createReviewQuery, createReviewParams);

        String lastInsertIdQuery = "select last_insert_id()";
        int reviewIdx =  this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);

        for (int i = 0; i < postReview.getTypeIdx().size(); i++ ){
            String createMannerQuery = "insert into MannerEvaliation (reviewIdx, typeIdx, senderIdx, receiverIdx) values (?, ?, ?, ?) ";
            Object[] createMannerParams = new Object[]{reviewIdx, postReview.getTypeIdx().get(i), senderIdx, receiverIdx};
            this.jdbcTemplate.update(createMannerQuery, createMannerParams);
        }

        String mannerIdx = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(mannerIdx,int.class);
    }

    @Transactional
    public List<GetReviewAboutUser> getReviewAboutUser(int receiverIdx){
        String getReviewAboutUserQuery = "select concat('받은 거래 후기 ', count(receiverIdx)) as reviewCount from DealReview where DealReview.status = 1 and receiverIdx = ? ";
        String getUserMannerQuery = "select me.typeIdx as typeIdx,\n" +
                "       count(me.typeIdx) as mannerTypeCount,\n" +
                "       mt.content mannerContent\n" +
                "from MannerEvaliation me, MannerType mt\n" +
                "where me.typeIdx = mt.typeIdx\n" +
                "and me.status = 1\n" +
                "and me.receiverIdx = ?\n" +
                "group by me.typeIdx ";
        String getUserReviewQuery = "select u.userIdx as userIdx,\n" +
                "       u.nickName as nickName,\n" +
                "       u.image as image,\n" +
                "       r.regionName,\n" +
                "       case\n" +
                "           when (timestampdiff(minute, dr.createAt, now()) < 1) then concat(timestampdiff(second, dr.createAt, now()), '초', ' 전')\n" +
                "           when (timestampdiff(hour, dr.createAt, now()) < 1) then concat(timestampdiff(minute, dr.createAt, now()),'분', ' 전')\n" +
                "           when (timestampdiff(day, dr.createAt, now()) <= 1) then concat(timestampdiff(hour, dr.createAt, now()), '시간', ' 전')\n" +
                "           when (timestampdiff(hour, dr.createAt, now()) > 24) then concat(timestampdiff(day, dr.createAt, now()), '일', ' 전')\n" +
                "           else concat(timestampdiff(month , dr.createAt, now()),'달', ' 전') end as uploadTime,\n" +
                "       dr.content as content\n" +
                "from DealReview dr, User u, Region r\n" +
                "where dr.senderIdx = u.userIdx\n" +
                "and dr.status = 1\n" +
                "and u.status = 1\n" +
                "and dr.senderIdx = r.userIdx\n" +
                "and r.nowStatus = 1\n" +
                "and r.status = 1\n" +
                "and dr.receiverIdx = ? ";

        int getReviewAboutParams = receiverIdx;

        return this.jdbcTemplate.query(getReviewAboutUserQuery,
                (rs, rowNum) -> new GetReviewAboutUser(
                        this.jdbcTemplate.query(getUserMannerQuery,
                                (rs1, rowNum1) -> new GetUserManner(
                                        rs1.getInt("typeIdx"),
                                        rs1.getInt("mannerTypeCount"),
                                        rs1.getString("mannerContent")
                                ), getReviewAboutParams),
                        rs.getString("reviewCount"),
                        this.jdbcTemplate.query(getUserReviewQuery,
                                (rs2, rowNum2) -> new GetUserReview(
                                        rs2.getInt("userIdx"),
                                        rs2.getString("nickName"),
                                        rs2.getString("image"),
                                        rs2.getString("regionName"),
                                        rs2.getString("uploadTime"),
                                        rs2.getString("content")
                                ), getReviewAboutParams)
                ), getReviewAboutParams);
    }
}
