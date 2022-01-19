package com.example.demo.src.comment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetComment {
    private int userIdx;
    private String profileImage;
    private String nickName;
    private String regionName;
    private String uploadTime;
    private String comment;
    private String image;
    private String placeName;
    private String placeAddress;
}
