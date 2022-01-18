package com.example.demo.src.townActivity.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor



public class PostTownActivityReq {
    private String topicName;   //필수
    private String content;     //필수
    private String plcaeName;   //선택
    private String placeAddress;  //선택
    private int userIdx;

    private String image;   //이미지
    private int firstImage;  //첫번째 이미지 여부

    //private JSONObject imageList;


}
