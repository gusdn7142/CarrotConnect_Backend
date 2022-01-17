package com.example.demo.src.chat.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetChatRoomList {
    private int chatRoomIdx;
    private int buyerIdx;
    private int sellerIdx;
    private int opponentIdx;
    private String profileImage;
    private String nickName;
    private String regionName;
    private String uploadTime;
    private String lastContent;
    private  String productImage;
}
