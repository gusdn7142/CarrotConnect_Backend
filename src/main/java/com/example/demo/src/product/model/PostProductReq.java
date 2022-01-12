package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

// 상품 등록 model
@Getter
@Setter
@AllArgsConstructor
public class PostProductReq {
    private String title;
    private int price;
    private int priceOfferStatus;
    private String content;
    private int saleStatus;
    private int categoryIdx;
    private int regionIdx;
    private String image;
    private int firstImageCheck;
}
