package com.example.demo.src.search.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class GetSearchToProductRes {

    private int productIdx;
    private String image;
    private String title;
    private String regionName;
    private String createAt;
    private String price;
    private int chatCount;
    private int interestCount;
}
