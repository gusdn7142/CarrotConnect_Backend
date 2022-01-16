package com.example.demo.src.region.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

// 내 동네 추가 model
@Getter
@Setter
@AllArgsConstructor
public class PostRegion {
    private String regionName;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private int keywordAlertStatus;

    public PostRegion(){
    }
}
