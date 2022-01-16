package com.example.demo.src.report;

import com.example.demo.config.BaseException;
import static com.example.demo.config.BaseResponseStatus.*;
import com.example.demo.src.report.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;



//Provider : Read의 비즈니스 로직 처리
@Service
public class ReportProvider {

    private final ReportDao reportDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ReportProvider(ReportDao reportDao, JwtService jwtService) {
        this.reportDao = reportDao;
        this.jwtService = jwtService;
    }














}
