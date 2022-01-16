package com.example.demo.src.report;



import com.example.demo.config.BaseException;
import static com.example.demo.config.BaseResponseStatus.*;
import com.example.demo.src.report.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;





// Service Create, Update, Delete 의 로직 처리
@Service
public class ReportService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ReportDao reportDao;
    private final ReportProvider reportProvider;
    private final JwtService jwtService;


    @Autowired
    public ReportService(ReportDao reportDao, ReportProvider reportProvider, JwtService jwtService) {
        this.reportDao = reportDao;
        this.reportProvider = reportProvider;
        this.jwtService = jwtService;

    }




}
