package com.example.demo.src.comment;

import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.comment.model.*;
import com.example.demo.utils.AES128;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

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
            int result = commentDao.createComment(postIdx, userIdx, postComment);
            if(result == 0){
                //throw new BaseException(/*MODIFY_FAIL_USERNAME*/);
                System.out.println("실패, 예외는 곧 추가 예정");
                return result;
            }
            return result;
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public String patchCommentStatus(int postIdx, int userIdx, int commentIdx) throws BaseException {
        try{
            int result = commentDao.patchCommentStatus(postIdx, userIdx, commentIdx);
            String message = "댓글 삭제 성공";

            if(result == 0){
                //throw new BaseException(/*MODIFY_FAIL_USERNAME*/);
                System.out.println("실패, 예외는 곧 추가 예정");
                message = "삭제에 실패했습니다.";
                return message;
            }
            return message;
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
