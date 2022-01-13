package com.example.demo.src.category;

import com.example.demo.config.BaseException;
import com.example.demo.src.category.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Service
public class CategoryService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CategoryDao categoryDao;
    private final CategoryProvider CategoryProvider;
    private final JwtService jwtService;

    @Autowired
    public CategoryService(CategoryDao categoryDao, CategoryProvider CategoryProvider, JwtService jwtService) {
        this.categoryDao = categoryDao;
        this.CategoryProvider = CategoryProvider;
        this.jwtService = jwtService;

    }

    public void createInterestCategory(int userIdx, int categoryIdx) throws BaseException {
        try{
            int result = categoryDao.createInterestCategory(userIdx, categoryIdx);
            if(result == 0){
                //throw new BaseException(/*MODIFY_FAIL_USERNAME*/);
                System.out.println("실패, 예외는 곧 추가 예정");
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void patchCategoryInterest(int idx) throws BaseException {
        try{
            int result = categoryDao.patchCategoryInterest(idx);
            if(result == 0){
                //throw new BaseException(/*MODIFY_FAIL_USERNAME*/);
                System.out.println("실패, 예외는 곧 추가 예정");
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
