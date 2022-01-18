package com.example.demo.src.review.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetReviewAboutUser {
    private List<GetUserManner> userManners;
    private String reviewCount;
    private List<GetUserReview> userReviews;
}
