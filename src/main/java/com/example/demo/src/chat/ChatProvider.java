package com.example.demo.src.chat;

import com.example.demo.config.BaseException;
import com.example.demo.src.chat.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

//Provider : Read의 비즈니스 로직 처리
@Service
public class ChatProvider {

    private final ChatDao chatDao;
    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ChatProvider(ChatDao chatDao, JwtService jwtService) {
        this.chatDao = chatDao;
        this.jwtService = jwtService;
    }

    public List<GetChatRoomList> getChatRoomList(int userIdx) throws BaseException{
        try{
            List<GetChatRoomList> getChatRoomList = chatDao.getChatRoomList(userIdx);
            return getChatRoomList;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetChatContent> getChatContent(int userIdx, int chatRoomIdx) throws BaseException{
        try{
            int checkRoom = chatDao.checkChatRoom(chatRoomIdx);
            if(checkRoom == 0){throw new BaseException(DATABASE_ERRORS_NOT_EXITS_CHAT);}

            int checkUser = chatDao.checkChatRoomUser(chatRoomIdx, userIdx);
            if(checkUser == 0){throw new BaseException(DATABASE_ERROR_NOT_SAME);}

            List<GetChatContent> getChatContent = chatDao.getChatContent(userIdx, chatRoomIdx);
            return getChatContent;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
