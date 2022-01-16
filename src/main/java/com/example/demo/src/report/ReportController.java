package com.example.demo.src.report;

import com.example.demo.src.report.model.*;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import static com.example.demo.config.BaseResponseStatus.*;

import com.example.demo.src.user.UserProvider;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import static com.example.demo.utils.ValidationRegex.*;


@RestController
@RequestMapping("reports")
public class ReportController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final ReportProvider reportProvider;
    @Autowired
    private final ReportService reportService;
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final UserProvider userProvider;



    public ReportController(ReportProvider reportProvider, ReportService reportService, JwtService jwtService, UserProvider userProvider) {
        this.reportProvider = reportProvider;
        this.reportService = reportService;
        this.jwtService = jwtService;
        this.userProvider = userProvider;
    }















}