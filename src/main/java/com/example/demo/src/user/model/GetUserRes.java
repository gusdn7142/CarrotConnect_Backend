package com.example.demo.src.user.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetUserRes {

    private String image;
    private String nickName;
    private Double mannerTemp;
    private Double tradeRate;
    private Double responseRate;
    private String createAt;
    private String regionName;
    private int authCount;
    private int badgeCount;
    private int productSellCount;
    private int sellReviewCount;


}
