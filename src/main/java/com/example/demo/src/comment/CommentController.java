package com.example.demo.src.comment;

import com.example.demo.src.user.UserProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.comment.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

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
    @Autowired
    private final UserProvider userProvider;

    public CommentController(CommentProvider commentProvider, CommentService commentService, JwtService jwtService, UserProvider userProvider){
        this.commentProvider = commentProvider;
        this.commentService = commentService;
        this.jwtService = jwtService;
        this.userProvider = userProvider;
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
            if(postComment.getComment() == null){return new BaseResponse<>(POST_COMMENTS_EMPTY);}
            if(postComment.getComment().length() < 1 || postComment.getComment().length() > 100){return new BaseResponse<>(POST_COMMENTS_INVALID);}
            if(postComment.getImage().length() < 1 || postComment.getImage().length() > 100){return new BaseResponse<>(POST_COMMENTS_INVALID_IMAGE);}
            if(postComment.getPlaceName().length() < 1 || postComment.getPlaceName().length() > 50){return new BaseResponse<>(POST_COMMENTS_INVALID_REGION_NAME);}
            if(postComment.getPlaceAddress().length() < 1 || postComment.getPlaceAddress().length() > 50){return new BaseResponse<>(POST_COMMENTS_INVALID_ADDRESS);}

            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt){return new BaseResponse<>(INVALID_USER_JWT);}
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            userProvider.checkByUser(request.getHeader("X-ACCESS-TOKEN"));

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
            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt){return new BaseResponse<>(INVALID_USER_JWT);}
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            userProvider.checkByUser(request.getHeader("X-ACCESS-TOKEN"));

            List<GetComment> getComment = commentProvider.getComment(postIdx);
            if(getComment.size() == 0){return new BaseResponse<>(GET_COMMENTS_FAIL);}
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
            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt){return new BaseResponse<>(INVALID_USER_JWT);}
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            userProvider.checkByUser(request.getHeader("X-ACCESS-TOKEN"));

            commentService.patchCommentStatus(postIdx, userIdx, commentIdx);
            String result = "성공";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
