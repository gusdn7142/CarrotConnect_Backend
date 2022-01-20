package com.example.demo.src.category;

import com.example.demo.src.category.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class CategoryDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Transactional
    public List<GetCategoryInterest> getCategoryInterest(int userIdx){
        String getCategoryInterestQuery = "select interestCategoryIdx as interestIdx,\n" +
                "       c.categoryIdx as categoryIdx,\n" +
                "       c.categoryName as categoryName,\n" +
                "       InterestCategory.status as status\n" +
                "from InterestCategory, Category c\n" +
                "where c.categoryIdx = InterestCategory.categoryIdx\n" +
                "and InterestCategory.status = 1\n" +
                "and userIdx = ? ";
        int getCategoryInterestParams = userIdx;
        return this.jdbcTemplate.query(getCategoryInterestQuery,
                (rs, rowNum) -> new GetCategoryInterest(
                        rs.getInt("interestIdx"),
                        rs.getInt("categoryIdx"),
                        rs.getString("categoryName"),
                        rs.getInt("status")
                ),
                getCategoryInterestParams);
    }

    @Transactional
    public int createInterestCategory(int userIdx, int categoryIdx){
        String createInterestCategoryQuery = "insert into InterestCategory (userIdx, categoryIdx) values (?, ?) ";
        Object[] createInterestCategoryParams = new Object[]{userIdx, categoryIdx};
        this.jdbcTemplate.update(createInterestCategoryQuery, createInterestCategoryParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    @Transactional
    public int patchCategoryInterest(int idx, int userIdx){
        String patchCategoryInterestQuery = "update InterestCategory set status = 0 where interestCategoryIdx = ? and userIdx = ?";
        Object[] patchCategoryInterestParams = new Object[]{idx, userIdx};
        return this.jdbcTemplate.update(patchCategoryInterestQuery,patchCategoryInterestParams);
    }

    @Transactional
    public int checkCategoryExist(int userIdx, int categoryIdx){
        String checkProductQuery = "select exists (select interestCategoryIdx from InterestCategory where userIdx= ? and categoryIdx = ? ) as exits";
        return this.jdbcTemplate.queryForObject(checkProductQuery, int.class, userIdx, categoryIdx);
    }

    @Transactional
    public int checkInterestExist(int idx){
        String checkProductQuery = "select exists (select interestCategoryIdx from InterestCategory where interestCategoryIdx = ? ) as exits;\n";
        return this.jdbcTemplate.queryForObject(checkProductQuery, int.class, idx);
    }

    @Transactional
    public int checkCategoryAccessUser(int idx, int userIdx){
        String checkProductQuery = "select exists (select interestCategoryIdx from InterestCategory where userIdx= ? and interestCategoryIdx = ? ) as exits;\n";
        return this.jdbcTemplate.queryForObject(checkProductQuery, int.class, userIdx, idx);
    }
}
