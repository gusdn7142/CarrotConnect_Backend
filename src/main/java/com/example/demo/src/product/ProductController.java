package com.example.demo.src.product;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.product.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexPhoneNumber;

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

    public ProductController(ProductProvider productProvider, ProductService productService, JwtService jwtService){
        this.productProvider = productProvider;
        this.productService = productService;
        this.jwtService = jwtService;
    }

    /**
     * 전체 상품 조회 API
     * [GET] /products
     * @return BaseResponse<GetProductList>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("") // (GET) 127.0.0.1:9000/products
    public BaseResponse<List<GetProductList>> getProductList(@RequestBody GetProductListReq getProductListReq) {
        try{
            int userIdx = getProductListReq.getUserIdx();
            String regionName = getProductListReq.getRegionName();

            // 헤더 (인증코드)에서 userIdx 추출.
            int userIdxByJwt = jwtService.getUserIdx();

            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            System.out.println("controller userIdx: " + userIdx + "regionName: " + regionName);
            // Get Product List
            List<GetProductList> getProductList = productProvider.getProductList(regionName);
            System.out.println("controller: " + getProductList);
            return new BaseResponse<>(getProductList);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 상품 조회 API
     * [GET] /products/:productIdx
     * @return BaseResponse<GetProduct>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/{productIdx}") // (GET) 127.0.0.1:9000/product/:productIdx
    public BaseResponse<List<GetProduct>> getProduct(@PathVariable("productIdx") int productIdx) {
        try{

            /**
             * validation 처리해야될것
             * 1. 인증코드여부
             * 2. 존재하는 상품인지
             */

            // Get Product
            List<GetProduct> getProduct = productProvider.getProduct(productIdx);
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
            /**
             * validation 처리해야될것
             * 1. 존재하는 상품인지
             */

            // 헤더 (인증코드)에서 userIdx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            int userIdx = patchProductStatus.getUserIdx();

            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            String result = productService.patchProductStatus(productIdx, userIdx);
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
            // 헤더 (인증코드)에서 userIdx 추출.
            int userIdxByJwt = jwtService.getUserIdx();

            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            // Get Product Sale
            List<GetProductSale> getProductSale = productProvider.getProductSale(userIdx);
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
            // 헤더 (인증코드)에서 userIdx 추출.
            int userIdxByJwt = jwtService.getUserIdx();

            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            // Get Product Complete
            List<GetProductComplete> getProductComplete = productProvider.getProductComplete(userIdx);
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
            // 헤더 (인증코드)에서 userIdx 추출.
            int userIdxByJwt = jwtService.getUserIdx();

            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            // Get Product Hidden
            List<GetProductHidden> getProductHidden = productProvider.getProductHidden(userIdx);
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
            // 헤더 (인증코드)에서 userIdx 추출.
            int userIdxByJwt = jwtService.getUserIdx();

            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            // Get Product Purchased
            List<GetProductPurchased> getProductPurchased = productProvider.getProductPurchased(userIdx);
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
    public BaseResponse<String> createProduct(@PathVariable("userIdx") int userIdx, @RequestBody PostProductReq postProductReq) {
        try {
            /**
             * validation 처리해야될것
             * 1. 인증코드여부
             * 2. 올바른 값들인지
             * 3. 존재하는 사용자인지
             * 4. 다른 값이 들어오는지
             */

            // 헤더 (인증코드)에서 userIdx 추출.
            int userIdxByJwt = jwtService.getUserIdx();

            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            //같다면 변경
            productService.createProduct(userIdx, postProductReq);
            String result = "상품 등록 성공";
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
    public BaseResponse<String> createInterestProduct(@PathVariable("userIdx") int userIdx, @PathVariable("productIdx") int productIdx) {
        try {
            /**
             * validation 처리해야될것
             * 1. 존재하는 사용자인지
             * 2. 존재하는 상품인지
             */

            // 헤더 (인증코드)에서 userIdx 추출.
            int userIdxByJwt = jwtService.getUserIdx();

            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            //같다면 변경
            productService.createInterestProduct(userIdx, productIdx);
            String result = "관심 목록 등록 성공";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 관심 목록 조회 API
     * [GET] /products/:userIdx/interest-list
     * @return BaseResponse<GetProductPurchased>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/{userIdx}/interest-list") // (GET) 127.0.0.1:9000/products/:userIdx/interest-list
    public BaseResponse<List<GetProductInterest>> getProductInterest(@PathVariable("userIdx") int userIdx) {
        try{
            // 헤더 (인증코드)에서 userIdx 추출.
            int userIdxByJwt = jwtService.getUserIdx();

            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            // Get Product Purchased
            List<GetProductInterest> getProductInterest = productProvider.getProductInterest(userIdx);
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
            // 헤더 (인증코드)에서 userIdx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            int userIdx = patchProductInterest.getUserIdx();

            // userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            String result = productService.patchProductInterest(interestIdx, userIdx);
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
