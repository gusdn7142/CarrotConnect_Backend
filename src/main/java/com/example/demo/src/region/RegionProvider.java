package com.example.demo.src.region;

import com.example.demo.config.BaseException;
import com.example.demo.src.region.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

//Provider : Read의 비즈니스 로직 처리
@Service
public class RegionProvider {

    private final RegionDao regionDao;
    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public RegionProvider(RegionDao regionDao, JwtService jwtService) {
        this.regionDao = regionDao;
        this.jwtService = jwtService;
    }
}
