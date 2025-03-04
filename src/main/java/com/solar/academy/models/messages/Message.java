package com.solar.academy.models.messages;

import com.solar.academy.models.BaseID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public class Message extends StringMsg{

    public BaseID photo;
    
}   // msg is private to user
    // its relative to,
    // sender Id inside BaseMsg

