package com.example.demo.src.region;

import com.example.demo.src.region.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class RegionDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int createRegion(int userIdx, PostRegion postRegion){
        String regionResetQuery = "update Region set nowStatus = 0, authStatus = 0 where nowStatus = 1 and status = 1 and userIdx = ? ";
        Object[] regionResetParams = new Object[]{userIdx};
        this.jdbcTemplate.update(regionResetQuery,regionResetParams);

        String createRegionQuery = "insert into Region (regionName, latitude, longitude, keywordAlertStatus, userIdx) values (?, ?, ?, ?, ?) ";
        Object[] createRegionParams = new Object[]{postRegion.getRegionName(), postRegion.getLatitude(), postRegion.getLongitude(), postRegion.getKeywordAlertStatus(), userIdx};
        this.jdbcTemplate.update(createRegionQuery, createRegionParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }
}
