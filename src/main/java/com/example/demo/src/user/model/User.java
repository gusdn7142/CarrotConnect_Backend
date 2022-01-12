package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class User {
    private int userIdx;
    private String nickName;
    private String phoneNumber;
    private String image;
    private String socialStatus;
    private double mannerTemp;
    private double tradeRate;
    private double responseRate;
    private int authCode;
    private Timestamp createAt;
    private Timestamp updateAt;
    private int status;

}
