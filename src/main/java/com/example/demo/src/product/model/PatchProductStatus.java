package com.example.demo.src.product.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

// 상품 삭제 model
@Getter
@Setter
@AllArgsConstructor
@JsonAutoDetect
public class PatchProductStatus {
    private int userIdx;
    private int status;
}
