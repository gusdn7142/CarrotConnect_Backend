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

    public int createInterestCategory(int userIdx, int categoryIdx) throws BaseException {
        try{
            int check = categoryDao.checkCategoryExist(userIdx, categoryIdx);
            if(check == 1) {throw new BaseException(POST_CATEGORIES_FAIL);}

            int result = categoryDao.createInterestCategory(userIdx, categoryIdx);
            return result;
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void patchCategoryInterest(int idx, int userIdx) throws BaseException {
        try{
            int exits = categoryDao.checkInterestExist(idx);
            if(exits == 0){throw new BaseException(DATABASE_ERROR_NOT_EXIST_CATEGORY);}

            int access = categoryDao.checkCategoryAccessUser(idx, userIdx);
            if(access == 0){throw new BaseException(DATABASE_ERROR_NOT_ACCESS_CATEGORY);}

            int result = categoryDao.patchCategoryInterest(idx, userIdx);
            if(result == 0){throw new BaseException(PATCH_CATEGORIES_FAIL);}
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
