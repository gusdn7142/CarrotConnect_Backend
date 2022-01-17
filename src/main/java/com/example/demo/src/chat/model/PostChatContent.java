package com.example.demo.src.chat.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostChatContent {
    private String content;
    private int productIdx;
    private int senderIdx;
    private int receiverIdx;

    public PostChatContent(){
    }
}
