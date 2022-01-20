package com.example.demo.src.comment;

import com.example.demo.config.BaseException;
import com.example.demo.src.comment.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Service
public class CommentService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CommentDao commentDao;
    private final CommentProvider commentProvider;
    private final JwtService jwtService;

    @Autowired
    public CommentService(CommentDao commentDao, CommentProvider commentProvider, JwtService jwtService) {
        this.commentDao = commentDao;
        this.commentProvider = commentProvider;
        this.jwtService = jwtService;
    }

    public int createComment(int postIdx, int userIdx, PostComment postComment) throws BaseException {
        try{
            int checkPost = commentDao.checkPost(postIdx);
            if(checkPost == 0){throw new BaseException(DATABASE_ERROR_NOT_EXITS_POST);}

            int result = commentDao.createComment(postIdx, userIdx, postComment);
            if(result == 0){throw new BaseException(POST_COMMENTS_FAIL);}
            return result;
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void patchCommentStatus(int postIdx, int userIdx, int commentIdx) throws BaseException {
        try{
            int checkPost = commentDao.checkPost(postIdx);
            if(checkPost == 0){throw new BaseException(DATABASE_ERROR_NOT_EXITS_POST);}

            int checkComment = commentDao.checkComment(commentIdx);
            if(checkComment == 0){throw new BaseException(DATABASE_ERROR_NOT_EXITS_COMMENT);}

            int checkAccess = commentDao.checkAccess(postIdx, userIdx, commentIdx);
            if(checkAccess == 0){throw new BaseException(DATABASE_ERROR_NOT_ACCESS_COMMENT);}

            int result = commentDao.patchCommentStatus(postIdx, userIdx, commentIdx);
            if(result == 0){throw new BaseException(PATCH_COMMENTS_FAIL);}
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
