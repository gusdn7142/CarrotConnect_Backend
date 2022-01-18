package com.example.demo.src.review.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetUserManner {
    private int typeIdx;
    private int mannerTypeCount;
    private String mannerContent;
}
