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
}
