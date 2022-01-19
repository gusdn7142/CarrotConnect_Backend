package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

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
    private String regionName;
    private String image;
    private int firstImageCheck;
    private List<String> images;

    public PostProductReq(){
    }
}
