package com.example.demo.src.alertKeyword.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor

public class GetALLAlertProductRes {
    private ArrayList<List<GetAlertPrductRes>> getAlertPrductRes;

}

