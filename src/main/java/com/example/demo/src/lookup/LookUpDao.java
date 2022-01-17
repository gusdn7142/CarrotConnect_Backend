package com.example.demo.src.lookup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class LookUpDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int createLookUpProduct(int userIdx, int productIdx){
        String createLookUpProductQuery = "insert into ProductLookup (userIdx, productIdx) values (?, ?) ";
        Object[] createLookUpProductParams = new Object[]{userIdx, productIdx};
        this.jdbcTemplate.update(createLookUpProductQuery, createLookUpProductParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }
}
