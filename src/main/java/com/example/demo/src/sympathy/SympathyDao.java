package com.example.demo.src.sympathy;

import com.example.demo.src.sympathy.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class SympathyDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Transactional
    public int createSympathy(int postIdx, int userIdx, int sympathyIdx){
        String createSympathyQuery = "insert into TownActivitySympathy (sympathyIdx, postIdx, userIdx) values (?, ?, ?) ";
        Object[] createSympathyParams = new Object[]{sympathyIdx, postIdx, userIdx};
        this.jdbcTemplate.update(createSympathyQuery, createSympathyParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    @Transactional
    public List<GetSympathy> getSympathy(int postIdx){
        String getSympathyQuery = "select postIdx, count(postIdx) as sympathyCount from TownActivitySympathy ts, TownActivity t\n" +
                "where ts.status = 1\n" +
                "and t.status = 1\n" +
                "and ts.postIdx = t.townActivityIdx\n" +
                "and ts.postIdx = ?\n" +
                "group by ts.postIdx ";
        String sympathyQuery = "select u.userIdx as userIdx,\n" +
                "       u.image as profileImage,\n" +
                "       u.nickName as nickName,\n" +
                "       r.regionName as regionName,\n" +
                "       r.authCount as authCount,\n" +
                "       s.image as sympathyImage,\n" +
                "       s.name as sympathyName\n" +
                "from TownActivitySympathy ts, User u, Region r, Sympathy s, TownActivity t\n" +
                "where ts.postIdx = t.townActivityIdx\n" +
                "and ts.userIdx = u.userIdx\n" +
                "and ts.sympathyIdx = s.sympathyIdx\n" +
                "and u.userIdx = r.userIdx\n" +
                "and ts.status = 1\n" +
                "and u.status = 1\n" +
                "and r.status = 1\n" +
                "and r.nowStatus =1\n" +
                "and s.status = 1\n" +
                "and t.status = 1\n" +
                "and postIdx = ? ";
        int getSympathyParams = postIdx;
        return this.jdbcTemplate.query(getSympathyQuery,
                (rs, rowNum) -> new GetSympathy(
                        rs.getInt("postIdx"),
                        rs.getInt("sympathyCount"),
                        this.jdbcTemplate.query(sympathyQuery,
                                (rs1, rowNum1) -> new SympathyList(
                                        rs1.getInt("userIdx"),
                                        rs1.getString("profileImage"),
                                        rs1.getString("nickName"),
                                        rs1.getString("regionName"),
                                        rs1.getInt("authCount"),
                                        rs1.getString("sympathyImage"),
                                        rs1.getString("sympathyName")
                                ), getSympathyParams)
                        ),
                getSympathyParams);
    }

    @Transactional
    public int patchSympathyStatus(int postIdx, int userIdx){
        String patchSympathyStatus = "update TownActivitySympathy set status = 0 where userIdx = ? and postIdx = ? ";
        Object[] patchSympathyParams = new Object[]{userIdx, postIdx};
        return this.jdbcTemplate.update(patchSympathyStatus,patchSympathyParams);
    }

    @Transactional
    public int checkPost(int postIdx){
        String checkPostQuery = "select exists(select townActivityIdx from TownActivity where townActivityIdx = ? and status = 1) as exist ";
        return this.jdbcTemplate.queryForObject(checkPostQuery, int.class, postIdx);
    }

    @Transactional
    public int checkSympathy(int postIdx, int userIdx){
        String checkSympathyQuery = "select exists(select sympathyIdx from TownActivitySympathy where postIdx = ? and userIdx = ? and status = 1) as exist;\n ";
        return this.jdbcTemplate.queryForObject(checkSympathyQuery, int.class, postIdx, userIdx);
    }
}

