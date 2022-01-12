package com.example.demo.src.product;

import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.product.model.*;
import com.example.demo.src.product.ProductDao;
import com.example.demo.utils.AES128;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

//Provider : Read의 비즈니스 로직 처리
@Service
public class ProductProvider {

    private final ProductDao productDao;
    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ProductProvider(ProductDao productDao, JwtService jwtService) {
        this.productDao = productDao;
        this.jwtService = jwtService;
    }

    public List<GetProductList> getProductList(int userIdx) throws BaseException{
        try{
            List<GetProductList> getProductList = productDao.getProductList(userIdx);
            return getProductList;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetProduct> getProduct(int productIdx) throws BaseException{
        try{
            List<GetProduct> getProduct = productDao.getProduct(productIdx);
            return getProduct;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetProductSale> getProductSale(int userIdx) throws BaseException{
        try{
            List<GetProductSale> getProductSale = productDao.getProductSale(userIdx);
            return getProductSale;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetProductComplete> getProductComplete(int userIdx) throws BaseException{
        try{
            List<GetProductComplete> getProductComplete = productDao.getProductComplete(userIdx);
            return getProductComplete;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
