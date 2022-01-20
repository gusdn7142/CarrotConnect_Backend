package com.example.demo.src.product;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.product.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void patchProductStatus(int productIdx, int userIdx) throws BaseException {
        try{
            int check = productDao.checkProduct(productIdx);
            if(check == 0){throw new BaseException(DATABASE_ERROR_NOT_EXIST_PRODUCT);}

            int access = productDao.checkProductAccessUser(productIdx, userIdx);
            if(access == 0) {throw new BaseException(DATABASE_ERROR_NOT_ACCESS_PRODUCT);}

            int result = productDao.patchProductStatus(productIdx, userIdx);
            if(result == 0) {throw new BaseException(PATCH_PRODUCTS_FAIL);}
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int createProduct(int userIdx, PostProductReq postProductReq) throws BaseException {
        try{
            int result = productDao.createProduct(userIdx, postProductReq);
            return result;
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int createInterestProduct(int userIdx, int productIdx) throws BaseException {
        try{
            int check = productDao.checkProduct(productIdx);
            if(check == 0){throw new BaseException(DATABASE_ERROR_NOT_EXIST_PRODUCT);}

            int exits = productDao.checkProductInterest(productIdx, userIdx);
            if(exits == 1) {throw new BaseException(POST_PRODUCTS_EXISTS_INTEREST);}

            int result = productDao.createInterestProduct(userIdx, productIdx);
            return result;
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void patchProductInterest(int interestIdx, int userIdx) throws BaseException {
        try{
            int check = productDao.checkProductInterestIdx(interestIdx);
            if(check == 0){throw new BaseException(DATABASE_ERROR_NOT_EXIST_INTEREST);}

            int access = productDao.checkProductInterestAccess(interestIdx, userIdx);
            if(access == 0) {throw new BaseException(DATABASE_ERROR_NOT_ACCESS_INTEREST);}

            int result = productDao.patchProductInterest(interestIdx, userIdx);
            if(result == 0){throw new BaseException(PATCH_PRODUCTS_FAIL_INTEREST);}
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void patchProductSaleStatus(int userIdx, int productIdx, int saleStatus) throws BaseException {
        try{
            int check = productDao.checkProduct(productIdx);
            if(check == 0){throw new BaseException(DATABASE_ERROR_NOT_EXIST_PRODUCT);}

            int access = productDao.checkProductAccessUser(productIdx, userIdx);
            if(access == 0) {throw new BaseException(DATABASE_ERROR_NOT_ACCESS_PRODUCT);}

            int result = productDao.patchProductSaleStatus(userIdx, productIdx, saleStatus);
            if(result == 0){throw new BaseException(PATCH_PRODUCTS_FAIL_SALE_STATUS);}
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkRegionName(String regionName) throws BaseException{
        return productDao.checkRegion(regionName);
    }

    public int checkProduct(int productIdx) throws BaseException{
        return productDao.checkProduct(productIdx);
    }
}
