package com.example.demo.src.sympathy.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetSympathy {
    private int postIdx;
    private int sympathyCount;
    private List<SympathyList> sympathies;
}
