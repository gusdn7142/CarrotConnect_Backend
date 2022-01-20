package com.example.demo.src.product;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.product.model.*;
import com.example.demo.src.user.UserProvider;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final ProductProvider productProvider;
    @Autowired
    private final ProductService productService;
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final UserProvider userProvider;

    public ProductController(ProductProvider productProvider, ProductService productService, JwtService jwtService, UserProvider userProvider){
        this.productProvider = productProvider;
        this.productService = productService;
        this.jwtService = jwtService;
        this.userProvider = userProvider;
    }

    /**
     * 전체 상품 조회 API
     * [GET] /products?userIdx=?regionName=
     * @return BaseResponse<GetProductList>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("") // (GET) 127.0.0.1:9000/products?userIdx=?regionName=
    public BaseResponse<List<GetProductList>> getProductList(@RequestParam int userIdx, @RequestParam String regionName) {
        try{
            if(regionName == null){return new BaseResponse<>(POST_PRODUCTS_EMPTY_REGION);}
            if(regionName.length() < 2 || regionName.length() > 15){return new BaseResponse<>(POST_PRODUCTS_INVALID_REGION);}
            if(productService.checkRegionName(regionName) == 0){return new BaseResponse<>(DATABASE_ERROR_NOT_EXIST_REGION);}

            // 헤더 (인증코드)에서 userIdx 추출.
            int userIdxByJwt = jwtService.getUserIdx();

            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){return new BaseResponse<>(INVALID_USER_JWT);}

            //로그아웃된 유저 인지 확인
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            userProvider.checkByUser(request.getHeader("X-ACCESS-TOKEN"));

            List<GetProductList> getProductList = productProvider.getProductList(regionName);
            return new BaseResponse<>(getProductList);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 상품 조회 API
     * [GET] /products/:productIdx/:userIdx
     * @return BaseResponse<GetProduct>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/{productIdx}/{userIdx}") // (GET) 127.0.0.1:9000/product/:productIdx/:userIdx
    public BaseResponse<List<GetProduct>> getProduct(@PathVariable("productIdx") int productIdx, @PathVariable("userIdx") int userIdx) {
        try{
            if(productService.checkProduct(productIdx) == 0){return new BaseResponse<>(DATABASE_ERROR_NOT_EXIST_PRODUCT);}

            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt){return new BaseResponse<>(INVALID_USER_JWT);}
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            userProvider.checkByUser(request.getHeader("X-ACCESS-TOKEN"));

            List<GetProduct> getProduct = productProvider.getProduct(productIdx, userIdx);
            return new BaseResponse<>(getProduct);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 상품 삭제 (상품 상태 변경) API
     * [PATCH] /products/:productIdx/status
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{productIdx}/status")
    public BaseResponse<String> patchProductStatus(@PathVariable("productIdx") int productIdx, @RequestBody PatchProductStatus patchProductStatus){
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            int userIdx = patchProductStatus.getUserIdx();
            if(userIdx != userIdxByJwt){return new BaseResponse<>(INVALID_USER_JWT);}
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            userProvider.checkByUser(request.getHeader("X-ACCESS-TOKEN"));

            productService.patchProductStatus(productIdx, userIdx);
            String result = "성공";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 판매중인 상품 조회 API
     * [GET] /products/:userIdx/sale
     * @return BaseResponse<GetProductSale>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/{userIdx}/sale") // (GET) 127.0.0.1:9000/product/:userIdx/sale
    public BaseResponse<List<GetProductSale>> getProductSale(@PathVariable("userIdx") int userIdx) {
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt){return new BaseResponse<>(INVALID_USER_JWT);}
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            userProvider.checkByUser(request.getHeader("X-ACCESS-TOKEN"));

            List<GetProductSale> getProductSale = productProvider.getProductSale(userIdx);
            if(getProductSale.size() == 0) {return new BaseResponse<>(GET_PRODUCTS_FAIL_SALE);}
            return new BaseResponse<>(getProductSale);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 거래완료 상품 조회 API
     * [GET] /products/:userIdx/complete
     * @return BaseResponse<GetProductComplete>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/{userIdx}/complete") // (GET) 127.0.0.1:9000/product/:userIdx/complete
    public BaseResponse<List<GetProductComplete>> getProductComplete(@PathVariable("userIdx") int userIdx) {
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt){return new BaseResponse<>(INVALID_USER_JWT);}
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            userProvider.checkByUser(request.getHeader("X-ACCESS-TOKEN"));

            List<GetProductComplete> getProductComplete = productProvider.getProductComplete(userIdx);
            if(getProductComplete.size() == 0){return new BaseResponse<>(GET_PRODUCTS_FAIL_COMPLETE);}
            return new BaseResponse<>(getProductComplete);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 숨김 상품 조회 API
     * [GET] /products/:userIdx/hidden
     * @return BaseResponse<GetProductHidden>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/{userIdx}/hidden") // (GET) 127.0.0.1:9000/product/:userIdx/hidden
    public BaseResponse<List<GetProductHidden>> getProductHidden(@PathVariable("userIdx") int userIdx) {
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt){return new BaseResponse<>(INVALID_USER_JWT);}
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            userProvider.checkByUser(request.getHeader("X-ACCESS-TOKEN"));

            List<GetProductHidden> getProductHidden = productProvider.getProductHidden(userIdx);
            if(getProductHidden.size() == 0){return new BaseResponse<>(GET_PRODUCTS_FAIL_HIDED);}
            return new BaseResponse<>(getProductHidden);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 구매 내역 조회 API
     * [GET] /products/:userIdx/purchased
     * @return BaseResponse<GetProductPurchased>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/{userIdx}/purchased") // (GET) 127.0.0.1:9000/products/:userIdx/purchased
    public BaseResponse<List<GetProductPurchased>> getProductPurchased(@PathVariable("userIdx") int userIdx) {
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt){return new BaseResponse<>(INVALID_USER_JWT);}
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            userProvider.checkByUser(request.getHeader("X-ACCESS-TOKEN"));

            List<GetProductPurchased> getProductPurchased = productProvider.getProductPurchased(userIdx);
            if(getProductPurchased.size() == 0){return new BaseResponse<>(GET_PRODUCTS_FAIL_PURCHASED);}
            return new BaseResponse<>(getProductPurchased);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 상품 등록 API
     * [POST] /products/:userIdx
     * @return BaseResponse<String>
     */
    // Body
    @ResponseBody
    @PostMapping("/{userIdx}")
    public BaseResponse<Integer> createProduct(@PathVariable("userIdx") int userIdx, @RequestBody PostProductReq postProductReq) {
        try {
            if(postProductReq.getTitle() == null){return new BaseResponse<>(POST_PRODUCTS_EMPTY_TITLE);}
            if(postProductReq.getTitle().length() < 2 || postProductReq.getTitle().length() > 30){return new BaseResponse<>(POST_PRODUCTS_INVALID_TITLE);}
            if(postProductReq.getPrice() > 1000000000){return new BaseResponse<>(POST_PRODUCTS_INVALID_PRICE);}
            if(postProductReq.getPriceOfferStatus() > 1 || postProductReq.getPriceOfferStatus() < 0){return new BaseResponse<>(POST_PRODUCTS_INVALID_OFFER);}
            if(postProductReq.getCategoryIdx() == 0){return new BaseResponse<>(POST_PRODUCTS_EMPTY_CATEGORY);}
            if(postProductReq.getCategoryIdx() < 1 || postProductReq.getCategoryIdx() > 17){return new BaseResponse<>(POST_PRODUCTS_INVALID_CATEGORY);}
            if(postProductReq.getRegionName() == null){return new BaseResponse<>(POST_PRODUCTS_EMPTY_REGION);}
            if(postProductReq.getRegionName().length() < 2 || postProductReq.getRegionName().length() > 15){return new BaseResponse<>(POST_PRODUCTS_INVALID_REGION);}
            if(postProductReq.getImage() == null){return new BaseResponse<>(POST_PRODUCTS_EMPTY_IMAGE);}
            if(postProductReq.getImage().length() < 5 || postProductReq.getImage().length() > 200){return new BaseResponse<>(POST_PRODUCTS_INVALID_IMAGE);}
            if(postProductReq.getFirstImageCheck() == 0){return new BaseResponse<>(POST_PRODUCTS_EMPTY_FIRST);}
            if(postProductReq.getFirstImageCheck() != 1){return new BaseResponse<>(POST_PRODUCTS_INVALID_FIRST);}

            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt){return new BaseResponse<>(INVALID_USER_JWT);}
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            userProvider.checkByUser(request.getHeader("X-ACCESS-TOKEN"));

            int result = productService.createProduct(userIdx, postProductReq);
            if(result == 0) {return new BaseResponse<>(POST_PRODUCTS_FAIL);}
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 관심 상품 등록 API
     * [POST] /products/:userIdx/:productIdx
     * @return BaseResponse<String>
     */
    // Path-variable
    @ResponseBody
    @PostMapping("/{userIdx}/{productIdx}/interest")
    public BaseResponse<Integer> createInterestProduct(@PathVariable("userIdx") int userIdx, @PathVariable("productIdx") int productIdx) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt){return new BaseResponse<>(INVALID_USER_JWT);}
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            userProvider.checkByUser(request.getHeader("X-ACCESS-TOKEN"));

            int result = productService.createInterestProduct(userIdx, productIdx);
            if(result == 0){return new BaseResponse<>(POST_PRODUCTS_FAIL_INTEREST);}
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 관심 목록 조회 API
     * [GET] /products/:userIdx/interest-list
     * @return BaseResponse<GetProductInterest>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/{userIdx}/interest-list") // (GET) 127.0.0.1:9000/products/:userIdx/interest-list
    public BaseResponse<List<GetProductInterest>> getProductInterest(@PathVariable("userIdx") int userIdx) {
        try{
            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt){return new BaseResponse<>(INVALID_USER_JWT);}
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            userProvider.checkByUser(request.getHeader("X-ACCESS-TOKEN"));

            List<GetProductInterest> getProductInterest = productProvider.getProductInterest(userIdx);
            if(getProductInterest.size() == 0){return new BaseResponse<>(GET_PRODUCTS_FAIL_INTEREST);}
            return new BaseResponse<>(getProductInterest);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 관심 목록 삭제 (관심 목록 상태 변경) API
     * [PATCH] /products/:interestIdx/interest-status
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{interestIdx}/interest-status")
    public BaseResponse<String> patchProductInterest(@PathVariable("interestIdx") int interestIdx, @RequestBody PatchProductInterest patchProductInterest){
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            int userIdx = patchProductInterest.getUserIdx();
            if(userIdx != userIdxByJwt){return new BaseResponse<>(INVALID_USER_JWT);}
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            userProvider.checkByUser(request.getHeader("X-ACCESS-TOKEN"));

            productService.patchProductInterest(interestIdx, userIdx);
            String result = "성공";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 상품 판매 상태 변경 API
     * [PATCH] /products/:userIdx/:productIdx/:saleStatus
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{userIdx}/{productIdx}/{saleStatus}")
    public BaseResponse<String> patchProductSaleStatus(@PathVariable("userIdx") int userIdx, @PathVariable("productIdx") int productIdx, @PathVariable("saleStatus") int saleStatus){
        try {
            if(saleStatus < 0 || saleStatus > 5){return new BaseResponse<>(PATCH_PRODUCTS_FAIL_SALE_STATUS);}

            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt){return new BaseResponse<>(INVALID_USER_JWT);}
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            userProvider.checkByUser(request.getHeader("X-ACCESS-TOKEN"));

            productService.patchProductSaleStatus(userIdx, productIdx, saleStatus);
            String result = "성공";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
