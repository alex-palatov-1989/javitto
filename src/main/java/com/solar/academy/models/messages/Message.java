package com.solar.academy.models.messages;

import lombok.Data;

@Data
public class Message extends Answer{

    public String seller;
    public String image;
}   // msg is private to user
    // its relative to sender

