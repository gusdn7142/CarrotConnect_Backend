package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

// 숨김 상품 조회 model
@Getter
@Setter
@AllArgsConstructor
public class GetProductHidden {
    private int userIdx;
    private int productIdx;
    private int hideStatus;
    private String title;
    private String regionName;
    private String uploadTime;
    private String price;
    private String image;
    private int chatCount;
    private int interestCount;
    private String productStatus;
}
