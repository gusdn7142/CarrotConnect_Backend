package com.example.demo.src.user.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor


public class PatchHideUserCancellReq {
    private int userIdx;
    private String hideCancellNickName;



}
