package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

// 거래완료 상품 조회 model
@Getter
@Setter
@AllArgsConstructor
public class GetProductComplete {
    private int userIdx;
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
