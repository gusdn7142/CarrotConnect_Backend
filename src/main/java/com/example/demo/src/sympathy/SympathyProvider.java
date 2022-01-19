package com.example.demo.src.sympathy;

import com.example.demo.config.BaseException;
import com.example.demo.src.sympathy.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

//Provider : Read의 비즈니스 로직 처리
@Service
public class SympathyProvider {

    private final SympathyDao sympathyDao;
    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public SympathyProvider(SympathyDao sympathyDao, JwtService jwtService) {
        this.sympathyDao = sympathyDao;
        this.jwtService = jwtService;
    }

    public List<GetSympathy> getSympathy(int postIdx) throws BaseException{
        try{
            List<GetSympathy> getSympathy = sympathyDao.getSympathy(postIdx);
            return getSympathy;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}