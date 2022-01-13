package com.example.demo.src.category;

import com.example.demo.config.BaseException;
import com.example.demo.src.category.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

//Provider : Read의 비즈니스 로직 처리
@Service
public class CategoryProvider {

    private final CategoryDao CategoryDao;
    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public CategoryProvider(CategoryDao CategoryDao, JwtService jwtService) {
        this.CategoryDao = CategoryDao;
        this.jwtService = jwtService;
    }

    public List<GetCategoryInterest> getCategoryInterest(int userIdx) throws BaseException{
        try{
            List<GetCategoryInterest> getCategoryInterest = CategoryDao.getCategoryInterest(userIdx);
            return getCategoryInterest;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
