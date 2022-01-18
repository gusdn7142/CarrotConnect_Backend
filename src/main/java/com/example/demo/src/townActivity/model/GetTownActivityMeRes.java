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


public class GetTownActivityMeRes {

    private int townActivityIdx;
    private String topicName;
    private String content;
    private int commentCount;
}
