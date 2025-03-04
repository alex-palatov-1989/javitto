package com.solar.academy.models.messages;

import com.solar.academy.dao.IRelative;
import com.solar.academy.models.BaseID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public class Review extends StringMsg{
    
    @IRelative.ToList
    public String[]         photos;    
    
    BaseID   post;
}
