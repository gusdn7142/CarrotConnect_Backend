package com.example.demo.src.product;

import com.example.demo.src.product.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ProductDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetProductList> getProductList(int userIdx){
        String getProductListQuery = "select p.productIdx as productIdx,\n" +
                "       p.title as title,\n" +
                "       regionName,\n" +
                "       case\n" +
                "           when (timestampdiff(minute, p.createAt, now()) < 1) then concat(timestampdiff(second, p.createAt, now()), '초', ' 전')\n" +
                "           when (timestampdiff(minute, p.createAt, now()) >= 60) then concat(timestampdiff(hour, p.createAt, now()), '시간', ' 전')\n" +
                "           when (timestampdiff(hour, p.createAt, now()) >= 24) then concat(timestampdiff(day, p.createAt, now()), '일', ' 전')\n" +
                "           else concat(timestampdiff(minute, p.createAt, now()),'분', ' 전') end as uploadTime,\n" +
                "       concat(format(price, 0), '원') as price,\n" +
                "       p.image as image,\n" +
                "       chatCount,\n" +
                "       y.interestCount as interestCount\n" +
                "from Product p\n" +
                "   left join(select productIdx, count(productIdx) as 'chatCount'\n" +
                "       from ChatRoom\n" +
                "       group by productIdx) as x on p.productIdx = x.productIdx\n" +
                "   left join(select productIdx, count(productIdx) as 'interestCount'\n" +
                "       from ProductInterest\n" +
                "       group by productIdx) as y on p.productIdx = y.productIdx, Region, User\n" +
                "where p.regionidx = Region.regionIdx\n" +
                "and Region.userIdx = User.userIdx and User.userIdx = ?";
        int getProductListParams = userIdx;
        return this.jdbcTemplate.query(getProductListQuery,
                (rs, rowNum) -> new GetProductList(
                        rs.getInt("productIdx"),
                        rs.getString("title"),
                        rs.getString("regionName"),
                        rs.getString("uploadTime"),
                        rs.getString("price"),
                        rs.getString("image"),
                        rs.getInt("chatCount"),
                        rs.getInt("interestCount")
                        ),
                getProductListParams);
    }
}
