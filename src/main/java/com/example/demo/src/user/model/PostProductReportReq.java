package com.example.demo.src.user.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor


public class PostProductReportReq {

    private String reportType;   //신고 유형
    private String content;      //내용 (null 가능)
    private String product;      //피해 상품 (null 가능)
    private String price;        //피해 금액 (null 가능)

    private int userIdx;         //신고자
    private String reportPostTitle; //신고할 게시글 제목
    private String reportNickName; //신고할 닉네임

    private int hiddenStatus;  //1이면 숨김 상태 처리 예정


}
