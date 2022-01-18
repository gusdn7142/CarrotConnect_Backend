package com.example.demo.src.chat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.chat.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexPhoneNumber;

@RestController
@RequestMapping("/chats")
public class ChatController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final ChatProvider chatProvider;
    @Autowired
    private final ChatService chatService;
    @Autowired
    private final JwtService jwtService;

    public ChatController(ChatProvider chatProvider, ChatService chatService, JwtService jwtService){
        this.chatProvider = chatProvider;
        this.chatService = chatService;
        this.jwtService = jwtService;
    }

    /**
     * 채팅방 생성 API
     * [POST] /chats/:buyerIdx/:sellerIdx/:productIdx
     * @return BaseResponse<String>
     */
    // Body
    @ResponseBody
    @PostMapping("/{buyerIdx}/{sellerIdx}/{productIdx}")
    public BaseResponse<String> createChatRoom(@PathVariable("buyerIdx") int buyerIdx,
                                              @PathVariable("sellerIdx") int sellerIdx,
                                              @PathVariable("productIdx") int productIdx,
                                              @RequestBody PostChatRoom postChatRoom) {
        try {
            /**
             * validation 처리해야될것
             * 1. 올바른 값들인지
             * 2. 존재하는 사용자인지
             * 3. 존재하는 상품인지
             * 4. 길이 확인
             * 5. 중복 확인
             */

            // 헤더 (인증코드)에서 userIdx 추출.
            int userIdxByJwt = jwtService.getUserIdx();

            //userIdx와 접근한 유저가 같은지 확인
            if(buyerIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            String result = chatService.createChatRoom(buyerIdx, sellerIdx, productIdx, postChatRoom);
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 채팅 메세지 생성 API
     * [POST] /chats/:chatRoomIdx/content
     * @return BaseResponse<String>
     */
    // Body
    @ResponseBody
    @PostMapping("/{chatRoomIdx}/content")
    public BaseResponse<String> createChatContent(@PathVariable("chatRoomIdx") int chatRoomIdx, @RequestBody PostChatContent postChatContent) {
        try {
            /**
             * validation 처리해야될것
             * 1. 올바른 값들인지
             * 2. 존재하는 사용자인지
             * 3. 존재하는 상품인지
             * 4. 길이 확인
             * 5. 중복 확인
             */

            // 헤더 (인증코드)에서 userIdx 추출.
            int userIdxByJwt = jwtService.getUserIdx();

            //userIdx와 접근한 유저가 같은지 확인
            if(postChatContent.getSenderIdx() != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            String result = chatService.createChatContent(chatRoomIdx, postChatContent);
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 채팅방 목록 조회 API
     * [GET] /chats/:userIdx
     * @return BaseResponse<GetChatRoomList>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/{userIdx}") // (GET) 127.0.0.1:9000/chats/:userIdx
    public BaseResponse<List<GetChatRoomList>> getChatRoomList(@PathVariable("userIdx") int userIdx) {
        try{
            // 헤더 (인증코드)에서 userIdx 추출.
            int userIdxByJwt = jwtService.getUserIdx();

            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            // Get ChatRoom List
            List<GetChatRoomList> getChatRoomList = chatProvider.getChatRoomList(userIdx);
            return new BaseResponse<>(getChatRoomList);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 특정 채팅방 내용 조회 API
     * [GET] /chats/:userIdx/:chatRoomIdx/room
     * @return BaseResponse<GetChatContent>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/{userIdx}/{chatRoomIdx}/room") // (GET) 127.0.0.1:9000/chats/:userIdx/:chatRoomIdx/room
    public BaseResponse<List<GetChatContent>> getChatContent(@PathVariable("userIdx") int userIdx, @PathVariable("chatRoomIdx") int chatRoomIdx) {
        try{
            /**
             * validation 처리해야될것
             * 1. 존재하는 채팅방인지
             */

            // 헤더 (인증코드)에서 userIdx 추출.
            int userIdxByJwt = jwtService.getUserIdx();

            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            // Get ChatRoom
            List<GetChatContent> getChatContent = chatProvider.getChatContent(userIdx, chatRoomIdx);
            return new BaseResponse<>(getChatContent);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
