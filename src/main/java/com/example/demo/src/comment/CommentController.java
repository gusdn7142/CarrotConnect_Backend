package com.example.demo.src.comment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.comment.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexPhoneNumber;

@RestController
@RequestMapping("/town-activities")
public class CommentController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final CommentProvider commentProvider;
    @Autowired
    private final CommentService commentService;
    @Autowired
    private final JwtService jwtService;

    public CommentController(CommentProvider commentProvider, CommentService commentService, JwtService jwtService){
        this.commentProvider = commentProvider;
        this.commentService = commentService;
        this.jwtService = jwtService;
    }

    /**
     * 게시글 댓글 작성 API
     * [POST] /town-activities/postIdx/:userIdx/comment
     * @return BaseResponse<Integer>
     */
    // Path-variable
    @ResponseBody
    @PostMapping("/{postIdx}/{userIdx}/comment")
    public BaseResponse<Integer> createInterestProduct(@PathVariable("postIdx") int postIdx, @PathVariable("userIdx") int userIdx, @RequestBody PostComment postComment) {
        try {
            /**
             * validation 처리해야될것
             * 1. 존재하는 사용자인지
             * 2. 존재하는 게시글인지
             */

            // 헤더 (인증코드)에서 userIdx 추출.
            int userIdxByJwt = jwtService.getUserIdx();

            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            int result = commentService.createComment(postIdx, userIdx, postComment);
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 게시글 댓글 조회 API
     * [GET] /town-activities/:postIdx/:userIdx/comment
     * @return BaseResponse<GetComment>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/{postIdx}/{userIdx}/comment") // (GET) 127.0.0.1:9000/products/:userIdx/interest-list
    public BaseResponse<List<GetComment>> getComment(@PathVariable("postIdx") int postIdx, @PathVariable("userIdx") int userIdx) {
        try{
            // 헤더 (인증코드)에서 userIdx 추출.
            int userIdxByJwt = jwtService.getUserIdx();

            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            List<GetComment> getComment = commentProvider.getComment(postIdx);
            return new BaseResponse<>(getComment);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 게시글 댓글 삭제 API
     * [PATCH] /town-activities/:postIdx/:userIdx/:commentIdx
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{postIdx}/{userIdx}/{commentIdx}")
    public BaseResponse<String> patchProductInterest(@PathVariable("postIdx") int postIdx, @PathVariable("userIdx") int userIdx, @PathVariable("commentIdx") int commentIdx){
        try {
            // 헤더 (인증코드)에서 userIdx 추출.
            int userIdxByJwt = jwtService.getUserIdx();

            // userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            String result = commentService.patchCommentStatus(postIdx, userIdx, commentIdx);
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
