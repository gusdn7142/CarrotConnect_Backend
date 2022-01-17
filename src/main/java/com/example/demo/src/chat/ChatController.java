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
    public BaseResponse<String> createProduct(@PathVariable("buyerIdx") int buyerIdx,
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

            //같다면 생성
            chatService.createChatRoom(buyerIdx, sellerIdx, productIdx, postChatRoom);
            String result = "채팅방 생성 및 메세지 전송 성공";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
