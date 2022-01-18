package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetProductOnSale {
    private int productIdx;
    private String productImage;
    private String title;
    private String price;
}
