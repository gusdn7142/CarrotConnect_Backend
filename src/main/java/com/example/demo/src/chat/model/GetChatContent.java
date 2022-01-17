package com.example.demo.src.chat.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetChatContent {
    private int chatRoomIdx;
    private int opponentIdx;
    private String nickName;
    private double mannerTemp;
    private int productIdx;
    private String profileImage;
    private String title;
    private String price;
    private String productImage;
    private String productStatus;
    private String startDate;
    private List<Contents> contents;
}
