package com.example.demo.src.chat;

import com.example.demo.config.BaseException;
import com.example.demo.src.chat.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Service
public class ChatService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ChatDao chatDao;
    private final ChatProvider ChatProvider;
    private final JwtService jwtService;

    @Autowired
    public ChatService(ChatDao chatDao, ChatProvider ChatProvider, JwtService jwtService) {
        this.chatDao = chatDao;
        this.ChatProvider = ChatProvider;
        this.jwtService = jwtService;
    }

    public void createChatRoom(int buyerIdx, int sellerIdx, int productIdx, PostChatRoom postChatRoom) throws BaseException {
        try{
            int result = chatDao.createChatRoom(buyerIdx, sellerIdx, productIdx, postChatRoom);
            if(result == 0){
                //throw new BaseException(/*MODIFY_FAIL_USERNAME*/);
                System.out.println("실패, 예외는 곧 추가 예정");
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
