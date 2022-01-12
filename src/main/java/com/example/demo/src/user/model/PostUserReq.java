package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class PostUserReq {


//  private String nickName;  필요 없음
  private String phoneNumber;


  private int userIdx;         //사용자 인덱스
  private String regionName;    //지역 명칭
  private double latitude;  //위도
  private double longitude;  //경도



//  private BigDecimal latitude;  //위도
//  private BigDecimal longitude;  //경도


//    private String UserName;
//    private String id;
//    private String email;
//    private String password;








}
