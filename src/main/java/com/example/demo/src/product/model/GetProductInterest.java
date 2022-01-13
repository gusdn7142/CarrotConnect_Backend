package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

// 전체 상품 조회 model
@Getter
@Setter
@AllArgsConstructor
public class GetProductInterest {
    private int interestIdx;
    private int productIdx;
    private String title;
    private String regionName;
    private String uploadTime;
    private String price;
    private String image;
    private int chatCount;
    private int interestCount;
    private String productStatus;
}
