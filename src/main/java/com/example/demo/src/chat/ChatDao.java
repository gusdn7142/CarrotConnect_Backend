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
}
