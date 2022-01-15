package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

// 상품 조회 model
@Getter
@Setter
@AllArgsConstructor
public class GetProduct {
    private int userIdx;
    private String nickName;
    private String userImage;
    private double mannerTemp;
    private int productIdx;
    private String productRegion;
    private String title;
    private String categoryName;
    private String uploadTime;
    private String content;
    private String price;
    private int priceOfferStatus;
    private String chatCount;
    private String interestCount;
    private String lookupCount;
    private String productStatus;
    private List<String> images;
}
