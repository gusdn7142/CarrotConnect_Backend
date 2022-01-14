package com.example.demo.src.alertKeyword.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor


public class PatchAlertKeywordReq {

    //알림 키워드 삭제에 사용
    private int userIdx;         //사용자 인덱스
    private String keyword;


    //알림 (키워드) 동네 변경에 사용
    private int regionAlertStatus;
    private String regionName;

}
