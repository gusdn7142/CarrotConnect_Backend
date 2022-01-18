package com.example.demo.src.review.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetUserReview {
    private int userIdx;
    private String nickName;
    private String image;
    private String regionName;
    private String uploadTime;
    private String content;
}
