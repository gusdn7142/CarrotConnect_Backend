package com.example.demo.src.chat;

import com.example.demo.src.chat.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ChatDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int createChatRoom(int buyerIdx, int sellerIdx, int productIdx, PostChatRoom postChatRoom){

        // 채팅방 생성
        String createChatRoomQuery = "insert into ChatRoom (buyer, seller, productIdx)values (?, ?, ?) ";
        Object[] createChatRoomParams = new Object[]{buyerIdx, sellerIdx, productIdx};

        // 쿼리 실행
        this.jdbcTemplate.update(createChatRoomQuery, createChatRoomParams);

        // chatRoomIdx 값 반환
        String lastInsertIdQuery = "select last_insert_id()";

        // String을 int로 변환
        int chatRoomIdx = this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);

        // 채팅 메세지 보내기
        String createChatContentQuery = "insert into ChatContent(content, chatRoomIdx, productIdx, senderIdx, receiverIdx)values (?, ?, ?, ?, ?)";
        Object[] createChatContentParams = new Object[]{postChatRoom.getContent(), chatRoomIdx, productIdx, buyerIdx, sellerIdx};

        // 쿼리문 실행
        this.jdbcTemplate.update(createChatContentQuery, createChatContentParams);

        // contentIdx 값 반환
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    public int createChatContent(int chatRoomIdx, PostChatContent postChatContent){
        String createCatchContentQuery = "insert into ChatContent(content, chatRoomIdx, productIdx, senderIdx, receiverIdx)values (?, ?, ?, ?, ?) ";
        Object[] createCatchContentParams = new Object[]{postChatContent.getContent(), chatRoomIdx, postChatContent.getProductIdx(), postChatContent.getSenderIdx(), postChatContent.getReceiverIdx()};
        this.jdbcTemplate.update(createCatchContentQuery, createCatchContentParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

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
}
