package com.example.demo.src.region.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

// 내 동네 조회 model
@Getter
@Setter
@AllArgsConstructor
public class GetRegion {
    private int regionIdx;
    private String regionName;
    private int nowStatus;
}
