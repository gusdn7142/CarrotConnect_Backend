package com.example.demo.src.user.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor


public class PostUserReportReq {


    private String reportType;   //신고 유형
    private String content;      //내용 (null 가능)
    private String product;      //피해 상품 (null 가능)
    private String price;        //피해 금액 (null 가능)
    private int informalStatus; //반말 여부 (null 가능)
    private int unkindStatus;  //불친절 여부 (null 가능)

    private int userIdx;         //신고자
    private String reportNickName; //신고할 닉네임


    private int hiddenStatus;  //1이면 숨김 상태 처리 예정



}
