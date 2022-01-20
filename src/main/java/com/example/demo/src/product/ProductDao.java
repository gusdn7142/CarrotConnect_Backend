package com.example.demo.src.product;

import com.example.demo.src.product.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ProductDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Transactional
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
                "           when(p.saleStatus = 5) then '나눔'\n" +
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
                "   left join(\n" +
                "       select productIdx, count(productIdx) as 'interestCount'\n" +
                "       from ProductInterest\n" +
                "       where status = 1\n" +
                "       group by productIdx) as y on p.productIdx = y.productIdx, ProductImage pi\n" +
                "where p.status = 1\n" +
                "and pi.firstImage = 1\n" +
                "and p.hideStatus = 0\n" +
                "and p.productIdx = pi.productIdx\n" +
                "and p.regionName = ? ";
        String getProductListParams = regionName;
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

    @Transactional
    public int checkRegion(String regionName){
        String checkRegionQuery = "select exists (select regionName from Region where regionName = ? and status = 1) as exist ";
        String checkRegionParams = regionName;
        return this.jdbcTemplate.queryForObject(checkRegionQuery, int.class, checkRegionParams);
    }

    @Transactional
    public int checkProduct(int productIdx){
        String checkProductQuery = "select exists (select productIdx from Product where productIdx = ? and status = 1) as exist ";
        int checkProductParams = productIdx;
        return this.jdbcTemplate.queryForObject(checkProductQuery, int.class, checkProductParams);
    }

    @Transactional
    public int checkProductAccessUser(int productIdx, int userIdx){
        String checkProductQuery = "select exists (select productIdx from Product where userIdx = ? and productIdx = ? and status = 1 ) as exist ";
        return this.jdbcTemplate.queryForObject(checkProductQuery, int.class, userIdx, productIdx);
    }

    @Transactional
    public int checkProductInterest(int productIdx, int userIdx){
        String checkProductInterestQuery = "select exists (select userIdx from ProductInterest where userIdx = ? and productIdx = ? and status = 1 ) as exist";
        return this.jdbcTemplate.queryForObject(checkProductInterestQuery, int.class, userIdx, productIdx);
    }

    @Transactional
    public int checkProductInterestIdx(int interestIdx){
        String checkProductInterestIdxQuery = "select exists (select interestIdx from ProductInterest where interestIdx = ? and status = 1 ) as exist ";
        return this.jdbcTemplate.queryForObject(checkProductInterestIdxQuery, int.class, interestIdx);
    }

    @Transactional
    public int checkProductInterestAccess(int interestIdx, int userIdx){
        String checkProductInterestQuery = "select exists (select userIdx from ProductInterest where userIdx = ? and interestIdx = ? and status = 1) as exist";
        return this.jdbcTemplate.queryForObject(checkProductInterestQuery, int.class, userIdx, interestIdx);
    }

    @Transactional
    public List<GetProduct> getProduct(int productIdx, int userIdx){
        String getProductQuery = "select u.userIdx as userIdx,\n" +
                "       u.nickName as nickName,\n" +
                "       u.image as userImage,\n" +
                "       u.mannerTemp as mannerTemp,\n" +
                "       p.productIdx as productIdx,\n" +
                "       p.regionName as productRegion,\n" +
                "       p.title as title,\n" +
                "       c.categoryName as categoryName,\n" +
                "       case\n" +
                "           when (timestampdiff(minute, p.createAt, now()) < 1) then concat(timestampdiff(second, p.createAt, now()), '초', ' 전')\n" +
                "           when (timestampdiff(hour, p.createAt, now()) < 1) then concat(timestampdiff(minute, p.createAt, now()),'분', ' 전')\n" +
                "           when (timestampdiff(day, p.createAt, now()) < 1) then concat(timestampdiff(hour, p.createAt, now()), '시간', ' 전')\n" +
                "           when (timestampdiff(hour, p.createAt, now()) > 24) then concat(timestampdiff(day, p.createAt, now()), '일', ' 전')\n" +
                "           else concat(timestampdiff(month , p.createAt, now()),'달', ' 전') end as uploadTime,\n" +
                "       p.content as content,\n" +
                "       case when(p.saleStatus = 2) then '나눔'\n" +
                "           when(p.saleStatus = 3) then '나눔'\n" +
                "           when(p.saleStatus = 5) then '나눔'\n" +
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
                "           when(p.saleStatus = 4) then '예약중'\n" +
                "           when(p.saleStatus = 5) then '나눔예약중'\n" +
                "           end as productStatus,\n" +
                "       exists(\n" +
                "           select pi.userIdx\n" +
                "           from ProductInterest pi, Product p\n" +
                "           where pi.productIdx = p.productIdx\n" +
                "             and pi.status = 1\n" +
                "             and pi.userIdx = ?\n" +
                "             and p.productIdx = ?) as interestStatus\n" +
                "from User u, Category c ,Product p\n" +
                "    left join(select productIdx, count(productIdx) as 'chatCount'\n" +
                "        from ChatRoom\n" +
                "        group by productIdx) as x on p.productIdx = x.productIdx\n" +
                "    left join(select productIdx, count(productIdx) as 'interestCount'\n" +
                "        from ProductInterest\n" +
                "       where status = 1\n" +
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
        String getProductOnSaleQuery = "select p.productIdx as productIdx,\n" +
                "       pi.image as productImage,\n" +
                "       p.title as title,\n" +
                "       case\n" +
                "           when(p.saleStatus = 2) then '나눔'\n" +
                "           when(p.saleStatus = 3) then '나눔'\n" +
                "           when(p.saleStatus = 5) then '나눔'\n" +
                "           else concat(format(p.price, 0), '원') end as price\n" +
                "from Product p, ProductImage pi\n" +
                "where p.productIdx = pi.productIdx\n" +
                "and firstImage = 1\n" +
                "and p.status = 1\n" +
                "and p.hideStatus = 0\n" +
                "and p.userIdx = ? ";

        String getProductUserIdxQuery = "select userIdx from Product where productIdx = ?";
        int getProductUserIdxParams = productIdx;
        int productUserIdx = this.jdbcTemplate.queryForObject(getProductUserIdxQuery, (rs3, rowNum3) -> new Integer(rs3.getString("userIdx")), getProductUserIdxParams);

        int getProductParams = productIdx;
        return this.jdbcTemplate.query(getProductQuery,
                (rs, rowNum) -> new GetProduct(
                        rs.getInt("userIdx"),
                        rs.getString("nickName"),
                        rs.getString("userImage"),
                        rs.getDouble("mannerTemp"),
                        rs.getInt("productIdx"),
                        rs.getString("productRegion"),
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
                        this.jdbcTemplate.query(getProductImageQuery, (rs1, rowNum1) -> new String(rs1.getString("images")), getProductParams),
                        rs.getInt("interestStatus"),
                        this.jdbcTemplate.query(getProductOnSaleQuery, (rs2, rowNum2) -> new GetProductOnSale(
                                rs2.getInt("productIdx"),
                                rs2.getString("productImage"),
                                rs2.getString("title"),
                                rs2.getString("price")
                                ), productUserIdx)
                ), userIdx, getProductParams, getProductParams);
    }

    @Transactional
    public int patchProductStatus(int productIdx, int userIdx){
        String patchProductStatusQuery = "update Product set status = 0 where productIdx = ? and userIdx = ? ";
        Object[] patchProductStatusParams = new Object[]{productIdx, userIdx};
        return this.jdbcTemplate.update(patchProductStatusQuery,patchProductStatusParams);
    }

    @Transactional
    public List<GetProductSale> getProductSale(int userIdx){
        String getProductSaleQuery = "select p.userIdx as userIdx,\n" +
                "       p.productIdx as productIdx,\n" +
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
                "           when(p.saleStatus = 5) then '나눔'\n" +
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
                "       where status = 1\n" +
                "       group by productIdx) as y on p.productIdx = y.productIdx, ProductImage pi\n" +
                "where p.productIdx = pi.productIdx\n" +
                "and p.status = 1\n" +
                "and pi.firstImage = 1\n" +
                "and p.hideStatus = 0\n" +
                "and (p.saleStatus = 1 or p.saleStatus = 2 or p.saleStatus = 4 or p.saleStatus = 5)\n" +
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

    @Transactional
    public List<GetProductComplete> getProductComplete(int userIdx){
        String getProductCompleteQuery = "select p.userIdx as userIdx,\n" +
                "       p.productIdx as productIdx,\n" +
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
                "           when(p.saleStatus = 5) then '나눔'\n" +
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
                "       where status = 1\n" +
                "       group by productIdx) as y on p.productIdx = y.productIdx, ProductImage pi\n" +
                "where p.productIdx = pi.productIdx\n" +
                "and p.status = 1\n" +
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

    @Transactional
    public List<GetProductHidden> getProductHidden(int userIdx){
        String getProductHiddenQuery = "select p.userIdx as userIdx,\n" +
                "       p.productIdx as productIdx,\n" +
                "       p.hideStatus as hideStatus,\n" +
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
                "           when(p.saleStatus = 5) then '나눔'\n" +
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
                "       where status = 1\n" +
                "       group by productIdx) as y on p.productIdx = y.productIdx, ProductImage pi\n" +
                "where p.status = 1\n" +
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

    @Transactional
    public List<GetProductPurchased> getProductPurchased(int userIdx){
        String getProductPurchasedQuery = "select p.productIdx as productIdx,\n" +
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
                "           when(p.saleStatus = 5) then '나눔'\n" +
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
                "       where status = 1\n" +
                "       group by productIdx) as y on p.productIdx = y.productIdx, ProductImage pi, PurchaseHistory ph\n" +
                "where p.productIdx = pi.productIdx\n" +
                "and p.productIdx = ph.productIdx\n" +
                "and ph.status = 1\n" +
                "and p.status = 1\n" +
                "and pi.firstImage = 1\n" +
                "and (p.saleStatus = 0 or p.saleStatus = 3)\n" +
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

    @Transactional
    public int createProduct(int userIdx, PostProductReq postProductReq){

        // Post 테이블에 데이터 삽입
        String createProductQuery = "insert into Product (title, price, priceOfferStatus, content, saleStatus, categoryIdx, regionName, userIdx) values (?, ?, ?, ?, ?, ?, ?, ?) ";
        Object[] createProductParams = new Object[]{postProductReq.getTitle(), postProductReq.getPrice(),postProductReq.getPriceOfferStatus() ,postProductReq.getContent(), postProductReq.getSaleStatus(), postProductReq.getCategoryIdx(), postProductReq.getRegionName(), userIdx};
        this.jdbcTemplate.update(createProductQuery, createProductParams);

        // productIdx 값 반환
        String lastInsertIdQuery = "select last_insert_id()";
        int productIdx = this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);

        // ProductImage table에 이미지 삽입
        String createProductImageQuery = "insert into ProductImage(productIdx, image, firstImage) values (?, ?, ?)";
        Object[] createProductImageParams = new Object[]{productIdx, postProductReq.getImage(), postProductReq.getFirstImageCheck()};
        this.jdbcTemplate.update(createProductImageQuery, createProductImageParams);

        if(postProductReq.getImages() == null){
            return productIdx;
        }

        for (int i = 0; i < postProductReq.getImages().size(); i++ ){
            String createImagesQuery = "insert into ProductImage(productIdx, image) values (?, ?) ";
            Object[] createImagesParams = new Object[]{productIdx, postProductReq.getImages().get(i)};
            this.jdbcTemplate.update(createImagesQuery, createImagesParams);
        }
        return productIdx;
    }

    @Transactional
    public int createInterestProduct(int userIdx, int productIdx){
        String createInterestProductQuery = "insert into ProductInterest (userIdx, productIdx) values (?, ?) ";
        Object[] createInterestParams = new Object[]{userIdx, productIdx};
        this.jdbcTemplate.update(createInterestProductQuery, createInterestParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    @Transactional
    public List<GetProductInterest> getProductInterest(int userIdx){
        String getProductInterestQuery = "select ProductInterest.interestIdx as interestIdx,\n" +
                "       p.productIdx as productIdx,\n" +
                "       p.title as title,\n" +
                "       p.regionName,\n" +
                "       case\n" +
                "           when (timestampdiff(minute, p.createAt, now()) < 1) then concat(timestampdiff(second, p.createAt, now()), '초', ' 전')\n" +
                "           when (timestampdiff(hour, p.createAt, now()) < 1) then concat(timestampdiff(minute, p.createAt, now()),'분', ' 전')\n" +
                "           when (timestampdiff(day, p.createAt, now()) <= 1) then concat(timestampdiff(hour, p.createAt, now()), '시간', ' 전')\n" +
                "           when (timestampdiff(hour, p.createAt, now()) > 24) then concat(timestampdiff(day, p.createAt, now()), '일', ' 전')\n" +
                "           else concat(timestampdiff(month , p.createAt, now()),'달', ' 전') end as uploadTime,\n" +
                "       case when(p.saleStatus = 2) then '나눔'\n" +
                "           when(p.saleStatus = 3) then '나눔'\n" +
                "           when(p.saleStatus = 5) then '나눔'\n" +
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
                "       where status = 1\n" +
                "       group by productIdx) as y on p.productIdx = y.productIdx, ProductImage pi, ProductInterest\n" +
                "where p.productIdx = ProductInterest.productIdx\n" +
                "and p.productIdx = pi.productIdx\n" +
                "and p.status = 1\n" +
                "and pi.firstImage = 1\n" +
                "and p.hideStatus = 0\n" +
                "and ProductInterest.status = 1\n" +
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

    @Transactional
    public int patchProductInterest(int interestIdx, int userIdx){
        String patchProductInterestQuery = "update ProductInterest set status = 0 where interestIdx = ? and userIdx =? ";
        Object[] patchProductInterestParams = new Object[]{interestIdx, userIdx};
        return this.jdbcTemplate.update(patchProductInterestQuery,patchProductInterestParams);
    }

    @Transactional
    public int patchProductSaleStatus(int userIdx, int productIdx, int saleStatus){
        String patchProductSaleStatusQuery = "update Product set saleStatus = ? where userIdx = ? and productIdx = ? and status = 1";
        Object[] patchProductSaleStatusParams = new Object[]{saleStatus, userIdx, productIdx};
        return this.jdbcTemplate.update(patchProductSaleStatusQuery,patchProductSaleStatusParams);
    }
}

