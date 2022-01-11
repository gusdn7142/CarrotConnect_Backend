package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

// 상품 삭제 model
@Getter
@Setter
@AllArgsConstructor
public class PatchProductStatus {
    private int productIdx;
    private int status;
}
