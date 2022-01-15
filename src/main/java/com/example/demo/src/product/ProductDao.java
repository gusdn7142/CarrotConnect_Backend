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

    public List<GetProductList> getProductList(String regionName){
        String getProductListQuery = "select p.productIdx as productIdx,\n" +
                "       p.title as title,\n" +
                "       p.regionName as regionName,\n" +
                "       case\n" +
                "           when (timestampdiff(minute, p.createAt, now()) < 1) then concat(timestampdiff(second, p.createAt, now()), '초', ' 전')\n" +
                "           when (timestampdiff(hour, p.createAt, now()) < 1) then concat(timestampdiff(minute, p.createAt, now()),'분', ' 전')\n" +
                "           when (timestampdiff(day, p.createAt, now()) < 1) then concat(timestampdiff(hour, p.createAt, now()), '시간', ' 전')\n" +
                "           when (timestampdiff(hour, p.createAt, now()) > 24) then concat(timestampdiff(day, p.createAt, now()), '일', ' 전')\n" +
                "           else concat(timestampdiff(month , p.createAt, now()),'달', ' 전') end as uploadTime,\n" +
                "       case when(p.saleStatus = 2) then '나눔'\n" +
                "           when(p.saleStatus = 3) then '나눔'\n" +
                "           else concat(format(p.price, 0), '원') end as price,\n" +
                "       pi.image as image,\n" +
                "       chatCount,\n" +
                "       y.interestCount as interestCount,\n" +
                "       case\n" +
                "           when (p.saleStatus = 0) then '거래완료'\n" +
                "           when(p.saleStatus = 1) then '판매중'\n" +
                "           when (p.saleStatus = 2) then '나눔중'\n" +
                "           when(p.saleStatus = 3) then '나눔완료'\n" +
                "           when(p.saleStatus = 4) then '예약중'\n" +
                "           when(p.saleStatus = 5) then '나눔예약중'\n" +
                "           end as productStatus\n" +
                "from Product p\n" +
                "   left join(select productIdx, count(productIdx) as 'chatCount'\n" +
                "       from ChatRoom\n" +
                "       group by productIdx) as x on p.productIdx = x.productIdx\n" +
                "   left join(select productIdx, count(productIdx) as 'interestCount'\n" +
                "       from ProductInterest\n" +
                "       group by productIdx) as y on p.productIdx = y.productIdx, ProductImage pi\n" +
                "where p.status = 1\n" +
                "and pi.firstImage = 1\n" +
                "and p.hideStatus = 0\n" +
                "and p.productIdx = pi.productIdx\n" +
                "and p.regionName = ? ";
        String getProductListParams = regionName;
        System.out.println("dao: " + getProductListParams);
        return this.jdbcTemplate.query(getProductListQuery,
                (rs, rowNum) -> new GetProductList(
                        rs.getInt("productIdx"),
                        rs.getString("title"),
                        rs.getString("regionName"),
                        rs.getString("uploadTime"),
                        rs.getString("price"),
                        rs.getString("image"),
                        rs.getInt("chatCount"),
                        rs.getInt("interestCount"),
                        rs.getString("productStatus")
                        ),
                getProductListParams);
    }

    public List<GetProduct> getProduct(int productIdx){
        String getProductQuery = "select u.userIdx as userIdx,\n" +
                "       u.nickName as nickName,\n" +
                "       u.image as userImage,\n" +
                "       concat(u.mannerTemp, '℃') as mannerTemp,\n" +
                "       p.productIdx as productIdx,\n" +
                "       p.title as title,\n" +
                "       c.categoryName as categoryName,\n" +
                "       case\n" +
                "           when (timestampdiff(minute, p.createAt, now()) < 1) then concat(timestampdiff(second, p.createAt, now()), '초', ' 전')\n" +
                "           when (timestampdiff(minute, p.createAt, now()) >= 60) then concat(timestampdiff(hour, p.createAt, now()), '시간', ' 전')\n" +
                "           when (timestampdiff(hour, p.createAt, now()) >= 24) then concat(timestampdiff(day, p.createAt, now()), '일', ' 전')\n" +
                "           else concat(timestampdiff(minute, p.createAt, now()),'분', ' 전') end as uploadTime,\n" +
                "       p.content as content,\n" +
                "       case when(p.saleStatus = 2) then '나눔'\n" +
                "           when(p.saleStatus = 3) then '나눔'\n" +
                "           else concat(format(p.price, 0), '원') end as price,\n" +
                "       p.priceOfferStatus as priceOfferStatus,\n" +
                "       concat('채팅 ', chatCount) as chatCount ,\n" +
                "       concat('관심 ', y.interestCount) as interestCount,\n" +
                "       concat('조회 ', z.lookupCount) as lookupCount,\n" +
                "       case\n" +
                "           when (p.saleStatus = 0) then '거래완료'\n" +
                "           when(p.saleStatus = 1) then '판매중'\n" +
                "           when (p.saleStatus = 2) then '나눔중'\n" +
                "           when(p.saleStatus = 3) then '나눔완료'\n" +
                "           when(p.saleStatus = 4) then '예약완료'\n" +
                "           end as productStatus\n" +
                "from User u, Category c ,Product p\n" +
                "    left join(select productIdx, count(productIdx) as 'chatCount'\n" +
                "        from ChatRoom\n" +
                "        group by productIdx) as x on p.productIdx = x.productIdx\n" +
                "    left join(select productIdx, count(productIdx) as 'interestCount'\n" +
                "        from ProductInterest\n" +
                "        group by productIdx) as y on p.productIdx = y.productIdx\n" +
                "    left join(select productIdx, count(productIdx) as 'lookupCount'\n" +
                "        from ProductLookup\n" +
                "        group by productIdx) as z on p.productIdx = z.productIdx\n" +
                "where p.userIdx = u.userIdx\n" +
                "and p.categoryIdx = c.categoryIdx\n" +
                "and p.status = 1\n" +
                "and p.hideStatus = 0\n" +
                "and p.productIdx = ? ";
        String getProductImageQuery = "select pi.image as images\n" +
                "from ProductImage pi, Product p\n" +
                "where pi.productIdx = p.productIdx\n" +
                "and p.status = 1\n" +
                "and p.hideStatus = 0\n" +
                "and p.productIdx = ? ";

        int getProductParams = productIdx;
        return this.jdbcTemplate.query(getProductQuery,
                (rs, rowNum) -> new GetProduct(
                        rs.getInt("userIdx"),
                        rs.getString("nickName"),
                        rs.getString("userImage"),
                        rs.getString("mannerTemp"),
                        rs.getInt("productIdx"),
                        rs.getString("title"),
                        rs.getString("categoryName"),
                        rs.getString("uploadTime"),
                        rs.getString("content"),
                        rs.getString("price"),
                        rs.getInt("priceOfferStatus"),
                        rs.getString("chatCount"),
                        rs.getString("interestCount"),
                        rs.getString("lookupCount"),
                        rs.getString("productStatus"),
                        this.jdbcTemplate.query(getProductImageQuery, (rs1, rowNum1) -> new String(rs1.getString("images")), getProductParams)
                ), getProductParams);
    }

    public int patchProductStatus(int productIdx){
        String patchProductStatusQuery = "update Product set status = 0 where productIdx = ? ";
        Object[] patchProductStatusParams = new Object[]{productIdx};
        return this.jdbcTemplate.update(patchProductStatusQuery,patchProductStatusParams);
    }

    public List<GetProductSale> getProductSale(int userIdx){
        String getProductSaleQuery = "select p.userIdx,\n" +
                "       p.productIdx as productIdx,\n" +
                "       p.title as title,\n" +
                "       regionName,\n" +
                "       case\n" +
                "           when (timestampdiff(minute, p.createAt, now()) < 1) then concat(timestampdiff(second, p.createAt, now()), '초', ' 전')\n" +
                "           when (timestampdiff(minute, p.createAt, now()) >= 60) then concat(timestampdiff(hour, p.createAt, now()), '시간', ' 전')\n" +
                "           when (timestampdiff(hour, p.createAt, now()) >= 24) then concat(timestampdiff(day, p.createAt, now()), '일', ' 전')\n" +
                "           else concat(timestampdiff(minute, p.createAt, now()),'분', ' 전') end as uploadTime,\n" +
                "       case when(p.saleStatus = 2) then '나눔'\n" +
                "           when(p.saleStatus = 3) then '나눔'\n" +
                "           else concat(format(p.price, 0), '원') end as price,\n" +
                "       pi.image as image,\n" +
                "       chatCount,\n" +
                "       y.interestCount as interestCount,\n" +
                "       case\n" +
                "           when (p.saleStatus = 0) then '거래완료'\n" +
                "           when(p.saleStatus = 1) then '판매중'\n" +
                "           when (p.saleStatus = 2) then '나눔중'\n" +
                "           when(p.saleStatus = 3) then '나눔완료'\n" +
                "           when(p.saleStatus = 4) then '예약완료'\n" +
                "           end as productStatus\n" +
                "from Product p\n" +
                "   left join(select productIdx, count(productIdx) as 'chatCount'\n" +
                "       from ChatRoom\n" +
                "       group by productIdx) as x on p.productIdx = x.productIdx\n" +
                "   left join(select productIdx, count(productIdx) as 'interestCount'\n" +
                "       from ProductInterest\n" +
                "       group by productIdx) as y on p.productIdx = y.productIdx, Region, User, ProductImage pi\n" +
                "where p.regionidx = Region.regionIdx\n" +
                "and Region.userIdx = User.userIdx\n" +
                "and p.status = 1\n" +
                "and p.productIdx = pi.productIdx\n" +
                "and pi.firstImage = 1\n" +
                "and p.hideStatus = 0\n" +
                "and (p.saleStatus = 1 or p.saleStatus = 2)\n" +
                "and p.userIdx = ? ";
        int getProductSaleParams = userIdx;
        return this.jdbcTemplate.query(getProductSaleQuery,
                (rs, rowNum) -> new GetProductSale(
                        rs.getInt("userIdx"),
                        rs.getInt("productIdx"),
                        rs.getString("title"),
                        rs.getString("regionName"),
                        rs.getString("uploadTime"),
                        rs.getString("price"),
                        rs.getString("image"),
                        rs.getInt("chatCount"),
                        rs.getInt("interestCount"),
                        rs.getString("productStatus")
                ),
                getProductSaleParams);
    }

    public List<GetProductComplete> getProductComplete(int userIdx){
        String getProductCompleteQuery = "select p.userIdx,\n" +
                "       p.productIdx as productIdx,\n" +
                "       p.title as title,\n" +
                "       regionName,\n" +
                "       case\n" +
                "           when (timestampdiff(minute, p.createAt, now()) < 1) then concat(timestampdiff(second, p.createAt, now()), '초', ' 전')\n" +
                "           when (timestampdiff(minute, p.createAt, now()) >= 60) then concat(timestampdiff(hour, p.createAt, now()), '시간', ' 전')\n" +
                "           when (timestampdiff(hour, p.createAt, now()) >= 24) then concat(timestampdiff(day, p.createAt, now()), '일', ' 전')\n" +
                "           else concat(timestampdiff(minute, p.createAt, now()),'분', ' 전') end as uploadTime,\n" +
                "       case when(p.saleStatus = 2) then '나눔'\n" +
                "           when(p.saleStatus = 3) then '나눔'\n" +
                "           else concat(format(p.price, 0), '원') end as price,\n" +
                "       pi.image as image,\n" +
                "       chatCount,\n" +
                "       y.interestCount as interestCount,\n" +
                "       case\n" +
                "           when (p.saleStatus = 0) then '거래완료'\n" +
                "           when(p.saleStatus = 1) then '판매중'\n" +
                "           when (p.saleStatus = 2) then '나눔중'\n" +
                "           when(p.saleStatus = 3) then '나눔완료'\n" +
                "           when(p.saleStatus = 4) then '예약완료'\n" +
                "           end as productStatus\n" +
                "from Product p\n" +
                "   left join(select productIdx, count(productIdx) as 'chatCount'\n" +
                "       from ChatRoom\n" +
                "       group by productIdx) as x on p.productIdx = x.productIdx\n" +
                "   left join(select productIdx, count(productIdx) as 'interestCount'\n" +
                "       from ProductInterest\n" +
                "       group by productIdx) as y on p.productIdx = y.productIdx, Region, User, ProductImage pi\n" +
                "where p.regionidx = Region.regionIdx\n" +
                "and Region.userIdx = User.userIdx\n" +
                "and p.status = 1\n" +
                "and p.productIdx = pi.productIdx\n" +
                "and pi.firstImage = 1\n" +
                "and p.hideStatus = 0\n" +
                "and (p.saleStatus = 0 or p.saleStatus = 3)\n" +
                "and p.userIdx = ? ";
        int getProductCompleteParams = userIdx;
        return this.jdbcTemplate.query(getProductCompleteQuery,
                (rs, rowNum) -> new GetProductComplete(
                        rs.getInt("userIdx"),
                        rs.getInt("productIdx"),
                        rs.getString("title"),
                        rs.getString("regionName"),
                        rs.getString("uploadTime"),
                        rs.getString("price"),
                        rs.getString("image"),
                        rs.getInt("chatCount"),
                        rs.getInt("interestCount"),
                        rs.getString("productStatus")
                ),
                getProductCompleteParams);
    }

    public List<GetProductHidden> getProductHidden(int userIdx){
        String getProductHiddenQuery = "select p.userIdx as userIdx,\n" +
                "       p.productIdx as productIdx,\n" +
                "       p.hideStatus as hideStatus,\n" +
                "       p.title as title,\n" +
                "       regionName,\n" +
                "       case\n" +
                "           when (timestampdiff(minute, p.createAt, now()) < 1) then concat(timestampdiff(second, p.createAt, now()), '초', ' 전')\n" +
                "           when (timestampdiff(minute, p.createAt, now()) >= 60) then concat(timestampdiff(hour, p.createAt, now()), '시간', ' 전')\n" +
                "           when (timestampdiff(hour, p.createAt, now()) >= 24) then concat(timestampdiff(day, p.createAt, now()), '일', ' 전')\n" +
                "           else concat(timestampdiff(minute, p.createAt, now()),'분', ' 전') end as uploadTime,\n" +
                "       case when(p.saleStatus = 2) then '나눔'\n" +
                "           when(p.saleStatus = 3) then '나눔'\n" +
                "           else concat(format(p.price, 0), '원') end as price,\n" +
                "       pi.image as image,\n" +
                "       chatCount,\n" +
                "       y.interestCount as interestCount,\n" +
                "       case\n" +
                "           when (p.saleStatus = 0) then '거래완료'\n" +
                "           when(p.saleStatus = 1) then '판매중'\n" +
                "           when (p.saleStatus = 2) then '나눔중'\n" +
                "           when(p.saleStatus = 3) then '나눔완료'\n" +
                "           when(p.saleStatus = 4) then '예약완료'\n" +
                "           end as productStatus\n" +
                "from Product p\n" +
                "   left join(select productIdx, count(productIdx) as 'chatCount'\n" +
                "       from ChatRoom\n" +
                "       group by productIdx) as x on p.productIdx = x.productIdx\n" +
                "   left join(select productIdx, count(productIdx) as 'interestCount'\n" +
                "       from ProductInterest\n" +
                "       group by productIdx) as y on p.productIdx = y.productIdx, Region, User, ProductImage pi\n" +
                "where p.regionidx = Region.regionIdx\n" +
                "and Region.userIdx = User.userIdx\n" +
                "and p.status = 1\n" +
                "and p.productIdx = pi.productIdx\n" +
                "and pi.firstImage = 1\n" +
                "and p.hideStatus = 1\n" +
                "and p.userIdx = ? ";
        int getProductHiddenParams = userIdx;
        return this.jdbcTemplate.query(getProductHiddenQuery,
                (rs, rowNum) -> new GetProductHidden(
                        rs.getInt("userIdx"),
                        rs.getInt("productIdx"),
                        rs.getInt("hideStatus"),
                        rs.getString("title"),
                        rs.getString("regionName"),
                        rs.getString("uploadTime"),
                        rs.getString("price"),
                        rs.getString("image"),
                        rs.getInt("chatCount"),
                        rs.getInt("interestCount"),
                        rs.getString("productStatus")
                ),
                getProductHiddenParams);
    }

    public List<GetProductPurchased> getProductPurchased(int userIdx){
        String getProductPurchasedQuery = "select p.productIdx as productIdx,\n" +
                "       p.title as title,\n" +
                "       regionName,\n" +
                "       case\n" +
                "           when (timestampdiff(minute, p.createAt, now()) < 1) then concat(timestampdiff(second, p.createAt, now()), '초', ' 전')\n" +
                "           when (timestampdiff(minute, p.createAt, now()) >= 60) then concat(timestampdiff(hour, p.createAt, now()), '시간', ' 전')\n" +
                "           when (timestampdiff(hour, p.createAt, now()) >= 24) then concat(timestampdiff(day, p.createAt, now()), '일', ' 전')\n" +
                "           else concat(timestampdiff(minute, p.createAt, now()),'분', ' 전') end as uploadTime,\n" +
                "       case when(p.saleStatus = 2) then '나눔'\n" +
                "           when(p.saleStatus = 3) then '나눔'\n" +
                "           else concat(format(p.price, 0), '원') end as price,\n" +
                "       pi.image as image,\n" +
                "       chatCount,\n" +
                "       y.interestCount as interestCount,\n" +
                "       case when (p.saleStatus = 0) then '거래완료' end as productStatus\n" +
                "from Product p\n" +
                "   left join(select productIdx, count(productIdx) as 'chatCount'\n" +
                "       from ChatRoom\n" +
                "       group by productIdx) as x on p.productIdx = x.productIdx\n" +
                "   left join(select productIdx, count(productIdx) as 'interestCount'\n" +
                "       from ProductInterest\n" +
                "       group by productIdx) as y on p.productIdx = y.productIdx, Region, User, ProductImage pi, PurchaseHistory ph\n" +
                "where p.regionidx = Region.regionIdx\n" +
                "and Region.userIdx = User.userIdx\n" +
                "and p.productIdx = pi.productIdx\n" +
                "and p.productIdx = ph.productIdx\n" +
                "and ph.status = 1\n" +
                "and p.status = 1\n" +
                "and pi.firstImage = 1\n" +
                "and p.saleStatus = 0\n" +
                "and ph.userIdx = ? ";
        int getProductPurchasedParams = userIdx;
        return this.jdbcTemplate.query(getProductPurchasedQuery,
                (rs, rowNum) -> new GetProductPurchased(
                        rs.getInt("productIdx"),
                        rs.getString("title"),
                        rs.getString("regionName"),
                        rs.getString("uploadTime"),
                        rs.getString("price"),
                        rs.getString("image"),
                        rs.getInt("chatCount"),
                        rs.getInt("interestCount"),
                        rs.getString("productStatus")
                ),
                getProductPurchasedParams);
    }

    public int createProduct(int userIdx, PostProductReq postProductReq){

        // Post 테이블에 데이터 삽입
        String createProductQuery = "insert into Product (title, price, priceOfferStatus, content, saleStatus, categoryIdx, regionIdx, userIdx) values (?, ?, ?, ?, ?, ?, ?, ?) ";
        Object[] createProductParams = new Object[]{postProductReq.getTitle(), postProductReq.getPrice(),postProductReq.getPriceOfferStatus() ,postProductReq.getContent(), postProductReq.getSaleStatus(), postProductReq.getCategoryIdx(), postProductReq.getRegionIdx(), userIdx};

        // 쿼리 실행
        this.jdbcTemplate.update(createProductQuery, createProductParams);

        // productIdx 값 반환
        String lastInsertIdQuery = "select last_insert_id()";

        // String을 int로 변환
        int productIdx = this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);

        // ProductImage table에 이미지 삽입
        String createProductImageQuery = "insert into ProductImage(productIdx, image, firstImage) values (?, ?, ?)";
        Object[] createProductImageParams = new Object[]{productIdx, postProductReq.getImage(), postProductReq.getFirstImageCheck()};

        // 쿼리문 실행
        this.jdbcTemplate.update(createProductImageQuery, createProductImageParams);

        // productIdx 값 반환
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    public int createInterestProduct(int userIdx, int productIdx){
        String createInterestProductQuery = "insert into ProductInterest (userIdx, productIdx) values (?, ?) ";
        Object[] createInterestParams = new Object[]{userIdx, productIdx};
        this.jdbcTemplate.update(createInterestProductQuery, createInterestParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    public List<GetProductInterest> getProductInterest(int userIdx){
        String getProductInterestQuery = "select ProductInterest.interestIdx as interestIdx,\n" +
                "       p.productIdx as productIdx,\n" +
                "       p.title as title,\n" +
                "       regionName,\n" +
                "       case\n" +
                "           when (timestampdiff(minute, p.createAt, now()) < 1) then concat(timestampdiff(second, p.createAt, now()), '초', ' 전')\n" +
                "           when (timestampdiff(hour, p.createAt, now()) < 1) then concat(timestampdiff(minute, p.createAt, now()),'분', ' 전')\n" +
                "           when (timestampdiff(day, p.createAt, now()) <= 1) then concat(timestampdiff(hour, p.createAt, now()), '시간', ' 전')\n" +
                "           when (timestampdiff(hour, p.createAt, now()) > 24) then concat(timestampdiff(day, p.createAt, now()), '일', ' 전')\n" +
                "           else concat(timestampdiff(month , p.createAt, now()),'달', ' 전') end as uploadTime,\n" +
                "       case when(p.saleStatus = 2) then '나눔'\n" +
                "           when(p.saleStatus = 3) then '나눔'\n" +
                "           else concat(format(p.price, 0), '원') end as price,\n" +
                "       pi.image as image,\n" +
                "       chatCount,\n" +
                "       y.interestCount as interestCount,\n" +
                "       case\n" +
                "           when (p.saleStatus = 0) then '거래완료'\n" +
                "           when(p.saleStatus = 1) then '판매중'\n" +
                "           when (p.saleStatus = 2) then '나눔중'\n" +
                "           when(p.saleStatus = 3) then '나눔완료'\n" +
                "           when(p.saleStatus = 4) then '예약완료'\n" +
                "           end as productStatus\n" +
                "from Product p\n" +
                "   left join(select productIdx, count(productIdx) as 'chatCount'\n" +
                "       from ChatRoom\n" +
                "       group by productIdx) as x on p.productIdx = x.productIdx\n" +
                "   left join(select productIdx, count(productIdx) as 'interestCount'\n" +
                "       from ProductInterest\n" +
                "       group by productIdx) as y on p.productIdx = y.productIdx, Region, ProductImage pi, ProductInterest\n" +
                "where p.productIdx = ProductInterest.productIdx\n" +
                "and p.regionidx = Region.regionIdx\n" +
                "and p.productIdx = pi.productIdx\n" +
                "and p.status = 1\n" +
                "and pi.firstImage = 1\n" +
                "and p.hideStatus = 0\n" +
                "and ProductInterest.userIdx = ? ";
        int getProductInterestParams = userIdx;
        return this.jdbcTemplate.query(getProductInterestQuery,
                (rs, rowNum) -> new GetProductInterest(
                        rs.getInt("interestIdx"),
                        rs.getInt("productIdx"),
                        rs.getString("title"),
                        rs.getString("regionName"),
                        rs.getString("uploadTime"),
                        rs.getString("price"),
                        rs.getString("image"),
                        rs.getInt("chatCount"),
                        rs.getInt("interestCount"),
                        rs.getString("productStatus")
                ),
                getProductInterestParams);
    }

    public int patchProductInterest(int interestIdx){
        String patchProductInterestQuery = "update ProductInterest set status = 0 where interestIdx = ? ";
        Object[] patchProductInterestParams = new Object[]{interestIdx};
        return this.jdbcTemplate.update(patchProductInterestQuery,patchProductInterestParams);
    }
}

