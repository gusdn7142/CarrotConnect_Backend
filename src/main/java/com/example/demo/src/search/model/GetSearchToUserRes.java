package com.example.demo.src.search.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class GetSearchToUserRes {
    private int userIdx;
    private String image;
    private String nickName;
    private String regionName;
}
