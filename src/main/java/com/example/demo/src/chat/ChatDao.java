package com.example.demo.src.chat;

import com.example.demo.src.chat.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ChatDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Transactional
    public int createChatRoom(int buyerIdx, int sellerIdx, int productIdx, PostChatRoom postChatRoom){

        // 채팅방 생성
        String createChatRoomQuery = "insert into ChatRoom (buyer, seller, productIdx)values (?, ?, ?) ";
        Object[] createChatRoomParams = new Object[]{buyerIdx, sellerIdx, productIdx};
        this.jdbcTemplate.update(createChatRoomQuery, createChatRoomParams);

        // chatRoomIdx 값 반환
        String lastInsertIdQuery = "select last_insert_id()";
        int chatRoomIdx = this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);

        // 채팅 메세지 보내기
        String createChatContentQuery = "insert into ChatContent(content, chatRoomIdx, productIdx, senderIdx, receiverIdx) values (?, ?, ?, ?, ?)";
        Object[] createChatContentParams = new Object[]{postChatRoom.getContent(), chatRoomIdx, productIdx, buyerIdx, sellerIdx};
        this.jdbcTemplate.update(createChatContentQuery, createChatContentParams);

        // contentIdx 값 반환
        return chatRoomIdx;
    }

    @Transactional
    public int createChatContent(int chatRoomIdx, PostChatContent postChatContent){
        String createCatchContentQuery = "insert into ChatContent(content, chatRoomIdx, productIdx, senderIdx, receiverIdx)values (?, ?, ?, ?, ?) ";
        Object[] createCatchContentParams = new Object[]{postChatContent.getContent(), chatRoomIdx, postChatContent.getProductIdx(), postChatContent.getSenderIdx(), postChatContent.getReceiverIdx()};
        this.jdbcTemplate.update(createCatchContentQuery, createCatchContentParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    @Transactional
    public List<GetChatRoomList> getChatRoomList(int userIdx){
        String getChatRoomListQuery = "select cr.chatRoomIdx as chatRoomIdx,\n" +
                "       cr.buyer as buyerIdx,\n" +
                "       cr.seller as sellerIdx,\n" +
                "       u.userIdx as opponentIdx,\n" +
                "       u.image as profileImage,\n" +
                "       u.nickName as nickName,\n" +
                "       r.regionName as regionName,\n" +
                "       case\n" +
                "           when (timestampdiff(minute, x.createAt, now()) < 1) then concat(timestampdiff(second, x.createAt, now()), '초', ' 전')\n" +
                "           when (timestampdiff(hour, x.createAt, now()) < 1) then concat(timestampdiff(minute, x.createAt, now()),'분', ' 전')\n" +
                "           when (timestampdiff(day, x.createAt, now()) < 1) then concat(timestampdiff(hour, x.createAt, now()), '시간', ' 전')\n" +
                "           when (timestampdiff(hour, x.createAt, now()) > 24) then concat(timestampdiff(day, x.createAt, now()), '일', ' 전')\n" +
                "           else concat(timestampdiff(month , cr.createAt, now()),'달', ' 전') end as uploadTime,\n" +
                "       x.content as lastContent,\n" +
                "       pi.image as productImage\n" +
                "from User u, ChatRoom cr\n" +
                "   left join(\n" +
                "       select cc.content as 'content', cc.chatRoomIdx, createAt\n" +
                "       from ChatContent cc\n" +
                "       where(chatRoomIdx, createAt) in (select chatRoomIdx, max(createAt) from ChatContent group by chatRoomIdx)\n" +
                "    ) as x on cr.chatRoomIdx = x.chatRoomIdx, Region r, Product p, ProductImage pi\n" +
                "where p.productIdx = cr.productIdx\n" +
                "  and p.productIdx = pi.productIdx\n" +
                "  and pi.firstImage = 1\n" +
                "  and r.nowStatus = 1\n" +
                "  and cr.status = 1\n" +
                "  and ((cr.buyer = ? and cr.seller = r.userIdx and cr.seller = u.userIdx) or (cr.seller = ? and buyer = r.userIdx and buyer = u.userIdx)) ";
        int getChatRoomListParams = userIdx;
        return this.jdbcTemplate.query(getChatRoomListQuery,
                (rs, rowNum) -> new GetChatRoomList(
                        rs.getInt("chatRoomIdx"),
                        rs.getInt("buyerIdx"),
                        rs.getInt("sellerIdx"),
                        rs.getInt("opponentIdx"),
                        rs.getString("profileImage"),
                        rs.getString("nickName"),
                        rs.getString("regionName"),
                        rs.getString("uploadTime"),
                        rs.getString("lastContent"),
                        rs.getString("productImage")
                ),
                getChatRoomListParams, getChatRoomListParams);
    }

    @Transactional
    public List<GetChatContent> getChatContent(int userIdx, int chatRoomIdx){
        String getChatContentQuery = "select cr.chatRoomIdx as chatRoomIdx,\n" +
                "       u.userIdx as opponentIdx,\n" +
                "       u.nickName as nickName,\n" +
                "       u.mannerTemp as mannerTemp,\n" +
                "       p.productIdx as productIdx,\n" +
                "       u.image as profileImage,\n" +
                "       p.title as title,\n" +
                "       case when(p.saleStatus = 2) then '나눔'\n" +
                "           when(p.saleStatus = 3) then '나눔'\n" +
                "           when(p.saleStatus = 5) then '나눔'\n" +
                "           else concat(format(p.price, 0), '원') end as price,\n" +
                "       p.priceOfferStatus as priceOfferStatus,\n" +
                "       pi.image as productImage,\n" +
                "       case\n" +
                "           when (p.saleStatus = 0) then '거래완료'\n" +
                "           when(p.saleStatus = 1) then '판매중'\n" +
                "           when (p.saleStatus = 2) then '나눔중'\n" +
                "           when(p.saleStatus = 3) then '나눔완료'\n" +
                "           when(p.saleStatus = 4) then '예약중'\n" +
                "           when(p.saleStatus = 5) then '나눔예약중'\n" +
                "           end as productStatus,\n" +
                "       DATE_FORMAT(min(cc.createAt), '%Y년 %m월 %d일') as startDate\n" +
                "from ChatRoom cr, ChatContent cc, User u, Product p, ProductImage pi\n" +
                "where cc.productIdx = p.productIdx\n" +
                "and cc.chatRoomIdx = cr.chatRoomIdx\n" +
                "and p.productIdx = pi.productIdx\n" +
                "and pi.firstImage = 1\n" +
                "and cr.status = 1\n" +
                "and ((cr.buyer = ? and cr.seller = u.userIdx) or (cr.seller = ? and buyer = u.userIdx))\n" +
                "and cr.chatRoomIdx = ? ";
        String getContentsQuery = "select senderIdx, content, receiverIdx, DATE_FORMAT(createAt, '%p %l:%i') as time\n" +
                "from ChatContent\n" +
                "where chatRoomIdx = ? order by createAt ";

        int getChatContentParams = userIdx;
        int getContentsParams = chatRoomIdx;
        return this.jdbcTemplate.query(getChatContentQuery,
                (rs, rowNum) -> new GetChatContent(
                        rs.getInt("chatRoomIdx"),
                        rs.getInt("opponentIdx"),
                        rs.getString("nickName"),
                        rs.getDouble("mannerTemp"),
                        rs.getInt("productIdx"),
                        rs.getString("profileImage"),
                        rs.getString("title"),
                        rs.getString("price"),
                        rs.getInt("priceOfferStatus"),
                        rs.getString("productImage"),
                        rs.getString("productStatus"),
                        rs.getString("startDate"),
                        this.jdbcTemplate.query(getContentsQuery,
                                (rs1, rowNum1) -> new Contents(
                                        rs1.getInt("senderIdx"),
                                        rs1.getString("content"),
                                        rs1.getInt("receiverIdx"),
                                        rs1.getString("time")
                                        ), getContentsParams)
                ), getChatContentParams, getChatContentParams, getContentsParams);
    }

    @Transactional
    public int checkProduct(int productIdx){
        String checkProductQuery = "select exists (select productIdx from Product where productIdx = ? and status = 1) as exits ";
        int checkProductParams = productIdx;
        return this.jdbcTemplate.queryForObject(checkProductQuery, int.class, checkProductParams);
    }

    @Transactional
    public int checkUser(int userIdx){
        String checkProductQuery = "select exists (select userIdx from Product where userIdx = ? and status = 1) as exits ";
        return this.jdbcTemplate.queryForObject(checkProductQuery, int.class, userIdx);
    }

    @Transactional
    public int checkSeller(int userIdx, int productIdx){
        String checkProductQuery = "select exists (select userIdx from Product where userIdx= ? and productIdx = ? and status = 1) as exist";
        return this.jdbcTemplate.queryForObject(checkProductQuery, int.class, userIdx, productIdx);
    }

    @Transactional
    public int checkRoom(int userIdx, int productIdx){
        String checkProductQuery = "select exists(select buyer from ChatRoom where buyer = ? and productIdx = ? and status = 1) as exist";
        return this.jdbcTemplate.queryForObject(checkProductQuery, int.class, userIdx, productIdx);
    }

    @Transactional
    public int checkChatRoom(int chatRoomIdx){
        String checkProductQuery = "select exists(select chatRoomIdx from ChatContent where chatRoomIdx = ? and status = 1) as exist ";
        return this.jdbcTemplate.queryForObject(checkProductQuery, int.class, chatRoomIdx);
    }

    @Transactional
    public int checkParticipation(int chatRoomIdx, int senderIdx, int receiverIdx ){
        String checkProductQuery = "select exists(select chatRoomIdx from ChatRoom\n" +
                "where (buyer = ? and seller = ?) or (buyer = ? and seller = ?)\n" +
                "and chatRoomIdx = ?\n" +
                "and status = 1) as exist";
        return this.jdbcTemplate.queryForObject(checkProductQuery, int.class, senderIdx, receiverIdx, receiverIdx, senderIdx, chatRoomIdx);
    }

    @Transactional
    public int checkChatRoomUser(int chatRoomIdx, int userIdx){
        String checkProductQuery = "select exists(select chatRoomIdx from ChatRoom\n" +
                "where chatRoomIdx = ?\n" +
                "and (buyer = ? or seller = ?)\n" +
                "and status = 1) as existist";
        return this.jdbcTemplate.queryForObject(checkProductQuery, int.class, chatRoomIdx, userIdx, userIdx);
    }
}
