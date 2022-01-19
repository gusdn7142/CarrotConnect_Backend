package com.example.demo.src.comment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PostComment {
    private String comment;
    private String image;
    private String placeName;
    private String placeAddress;

    public PostComment(){
    }
}
