package com.example.demo.src.category.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetCategoryInterest {
    private int interestIdx;
    private int categoryIdx;
    private String categoryName;
    private int status;
}
