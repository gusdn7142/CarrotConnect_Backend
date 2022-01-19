package com.example.demo.src.townActivity.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetTownActivitytoIdxRes {



    private int townActivityIdx;
    private String topicName;
    private String content;
    private String nickName;
    private String regionName;
    private int authCount;
    private String createAt;
    private int commentCount;
    private int sympathyCount;
    private List<GetTownActivitytoImageRes> getTownActivitytoImageRes;

//    @Getter
//    @Setter
//    @AllArgsConstructor
//    public class GetTownActivitytoImageRes {
//        private List<String> imageList;
//        private List<Integer> townActivityImageIdx;
//    }


}
