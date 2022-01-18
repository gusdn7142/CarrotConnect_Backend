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




public class PatchTownActivityReq {

    private String topicName;
    private String content;
    private int firstImage;  //첫번째 이미지 여부
    private String image;

    private int userIdx;
    private int townActivityIdx;




}
