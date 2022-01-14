package com.example.demo.src.alertKeyword.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class GetAlertPrductRes {
    private String image;
    private String keyword;
    private String regionName;
    private String title;
    private String createAt;
}
