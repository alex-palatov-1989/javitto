package com.solar.academy.models.messages;

import com.solar.academy.models.BaseID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter
@NoArgsConstructor
public class StringMsg extends BaseID{

    public String message;
    public String sender;
    
}
