package com.example.demo.src.review.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PostReview {
    private String content;
    private int preference;
    private List<Integer> typeIdx;

    public PostReview(){
    }
}

