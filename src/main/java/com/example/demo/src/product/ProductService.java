package com.example.demo.src.product;

import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.product.model.*;
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
public class ProductService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ProductDao productDao;
    private final ProductProvider productProvider;
    private final JwtService jwtService;

    @Autowired
    public ProductService(ProductDao productDao, ProductProvider productProvider, JwtService jwtService) {
        this.productDao = productDao;
        this.productProvider = productProvider;
        this.jwtService = jwtService;

    }

    public String patchProductStatus(int productIdx, int userIdx) throws BaseException {
        try{
            int result = productDao.patchProductStatus(productIdx, userIdx);
            String message = "상품 삭제 성공";
            if(result == 0){
                //throw new BaseException(/*MODIFY_FAIL_USERNAME*/);
                System.out.println("실패");
                message = "삭제에 실패했습니다.";
                return message;
            }
            return message;
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void createProduct(int userIdx, PostProductReq postProductReq) throws BaseException {
        try{
            int result = productDao.createProduct(userIdx, postProductReq);
            if(result == 0){
                //throw new BaseException(/*MODIFY_FAIL_USERNAME*/);
                System.out.println("실패, 예외는 곧 추가 예정");
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void createInterestProduct(int userIdx, int productIdx) throws BaseException {
        try{
            int result = productDao.createInterestProduct(userIdx, productIdx);
            if(result == 0){
                //throw new BaseException(/*MODIFY_FAIL_USERNAME*/);
                System.out.println("실패, 예외는 곧 추가 예정");
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public String patchProductInterest(int interestIdx, int userIdx) throws BaseException {
        try{
            int result = productDao.patchProductInterest(interestIdx, userIdx);
            String message = "관심 목록 삭제 성공";

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

    public String patchProductSaleStatus(int userIdx, int productIdx, int saleStatus) throws BaseException {
        try{
            int result = productDao.patchProductSaleStatus(userIdx, productIdx, saleStatus);
            String message = "상품 판매상태 변경 성공";

            if(result == 0){
                //throw new BaseException(/*MODIFY_FAIL_USERNAME*/);
                System.out.println("실패, 예외는 곧 추가 예정");
                message = "변경에 실패했습니다.";
                return message;
            }
            return message;
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
