package com.example.demo.src.sympathy.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SympathyList {
    private int userIdx;
    private String profileImage;
    private String nickName;
    private String regionName;
    private int authCount;
    private String sympathyImage;
    private String sympathyName;
}
