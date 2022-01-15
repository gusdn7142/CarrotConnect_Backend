package com.example.demo.src.gather.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor


public class GetGatherProductRes {
    private String image;
    private String title;
    private String nickName;
    private String regionName;
    private String price;
    private int saleStatus;

}
