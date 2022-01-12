package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class PatchJoinAuthReq {

    private String phoneNumber;

    private int userIdx; //임시로 생성 (이거 안쓰면 오류 발생)
}
