package com.example.demo.src.alertKeyword.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor


public class PostAlertKeywordReq {
    private int userIdx;         //사용자 인덱스
    private String Keyword;

}
