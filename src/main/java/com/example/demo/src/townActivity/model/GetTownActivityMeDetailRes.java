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





public class GetTownActivityMeDetailRes {
    private int townActivityIdx;
    private String topicName;
    private String nickName;
    private String regionName;
    private int authCount;
    private String createAt;

    private String content;
    private String image;
    private int sympathyCount;
    private int commentCount;
}
