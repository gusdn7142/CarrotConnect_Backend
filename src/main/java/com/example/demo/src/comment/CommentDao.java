package com.example.demo.src.comment;

import com.example.demo.src.comment.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class CommentDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Transactional
    public int createComment(int postIdx, int userIdx, PostComment postComment){
        String createCommentQuery = "insert into TownActivityComment (comment, image, placeName, placeAddress, userIdx, postIdx) values (?, ?, ?, ?, ?, ?) ";
        Object[] createCommentParams = new Object[]{postComment.getComment(), postComment.getImage(), postComment.getPlaceName(), postComment.getPlaceAddress(), userIdx, postIdx};
        this.jdbcTemplate.update(createCommentQuery, createCommentParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    @Transactional
    public List<GetComment> getComment(int postIdx){
        String getCommentQuery = "select u.userIdx as userIdx,\n" +
                "       u.image as profileImage,\n" +
                "       u.nickName as nickName,\n" +
                "       r.regionName as regionName,\n" +
                "       case\n" +
                "           when (timestampdiff(minute, c.createAt, now()) < 1) then concat(timestampdiff(second, c.createAt, now()), '초', ' 전')\n" +
                "           when (timestampdiff(hour, c.createAt, now()) < 1) then concat(timestampdiff(minute, c.createAt, now()),'분', ' 전')\n" +
                "           when (timestampdiff(day, c.createAt, now()) <= 1) then concat(timestampdiff(hour, c.createAt, now()), '시간', ' 전')\n" +
                "           when (timestampdiff(hour, c.createAt, now()) > 24) then concat(timestampdiff(day, c.createAt, now()), '일', ' 전')\n" +
                "           else concat(timestampdiff(month , c.createAt, now()),'달', ' 전') end as uploadTime,\n" +
                "       c.comment as comment,\n" +
                "       c.image as image,\n" +
                "       c.placeName as placeName,\n" +
                "       c.placeAddress as placeAddress\n" +
                "from User u, Region r, TownActivityComment c\n" +
                "where c.userIdx = u.userIdx\n" +
                "and u.userIdx = r.userIdx\n" +
                "and u.status = 1\n" +
                "and c.status = 1\n" +
                "and r.status = 1\n" +
                "and r.nowStatus = 1\n" +
                "and c.postIdx = ? ";
        int getCommentParams = postIdx;
        return this.jdbcTemplate.query(getCommentQuery,
                (rs, rowNum) -> new GetComment(
                        rs.getInt("userIdx"),
                        rs.getString("profileImage"),
                        rs.getString("nickName"),
                        rs.getString("regionName"),
                        rs.getString("uploadTime"),
                        rs.getString("comment"),
                        rs.getString("image"),
                        rs.getString("placeName"),
                        rs.getString("placeAddress")
                ),
                getCommentParams);
    }

    @Transactional
    public int patchCommentStatus(int postIdx, int userIdx, int commentIdx){
        String patchCommentStatusQuery = "update TownActivityComment set status = 0 where userIdx = ? and postIdx = ? and commentIdx = ? ";
        Object[] patchCommentStatusParams = new Object[]{userIdx, postIdx, commentIdx};
        return this.jdbcTemplate.update(patchCommentStatusQuery,patchCommentStatusParams);
    }
}
