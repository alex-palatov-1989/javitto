package com.solar.academy.models.messages;

import com.solar.academy.models.BaseID;
import lombok.Data;


@Data
public class StringMsg extends BaseID{

    public String message;
    public String sender;
    
}
