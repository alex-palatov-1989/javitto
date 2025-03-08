package com.solar.academy.models.messages;

import com.solar.academy.dao.IRelative;
import com.solar.academy.models.BaseID;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
public class Review extends StringMsg{
    
    @IRelative.ToList
    public String[]         photos;    
    
    BaseID   post;
}
