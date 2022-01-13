package com.example.demo.src.product.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

// 관심 목록 삭제 model
@Getter
@Setter
@AllArgsConstructor
@JsonAutoDetect
public class PatchProductInterest {
    private int userIdx;
    private int status;
}
