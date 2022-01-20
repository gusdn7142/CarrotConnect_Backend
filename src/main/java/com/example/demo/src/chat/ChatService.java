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

    public int createChatRoom(int buyerIdx, int sellerIdx, int productIdx, PostChatRoom postChatRoom) throws BaseException {
        try{
            int check = chatDao.checkProduct(productIdx);
            if(check == 0){throw new BaseException(DATABASE_ERROR_NOT_EXIST_PRODUCT);}

            int user = chatDao.checkUser(sellerIdx);
            if(user == 0){throw new BaseException(DATABASE_ERRORS_NOT_EXITS_USER);}

            int seller = chatDao.checkSeller(sellerIdx, productIdx);
            if(seller == 0){throw new BaseException(DATABASE_ERROR_NOT_ACCESS_PRODUCT);}

            int room = chatDao.checkRoom(buyerIdx, productIdx);
            if(room == 1){throw new BaseException(POST_CHATS_EXITS);}

            int result = chatDao.createChatRoom(buyerIdx, sellerIdx, productIdx, postChatRoom);
            if(result == 0){throw new BaseException(POST_CHATS_FAIL);}
            return result;
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int createChatContent(int chatRoomIdx, PostChatContent postChatContent) throws BaseException {
        try{
            int checkRoom = chatDao.checkChatRoom(chatRoomIdx);
            if(checkRoom == 0){throw new BaseException(DATABASE_ERRORS_NOT_EXITS_CHAT);}

            int same = chatDao.checkParticipation(chatRoomIdx, postChatContent.getSenderIdx(), postChatContent.getReceiverIdx());
            if(same == 0){throw new BaseException(DATABASE_ERROR_NOT_SAME);}

            int result = chatDao.createChatContent(chatRoomIdx, postChatContent);
            if(result == 0){throw new BaseException(POST_CHATS_FAIL_MESSAGE);}
            return result;
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
