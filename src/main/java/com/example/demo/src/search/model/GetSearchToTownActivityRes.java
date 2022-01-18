package com.example.demo.src.search.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor




public class GetSearchToTownActivityRes {
    private int townActivityIdx;
    private String image;
    private String topicName;
    private String regionName;
    private String createAt;
    private String content;
    private int sympathyCount;
    private int commentCount;

}

