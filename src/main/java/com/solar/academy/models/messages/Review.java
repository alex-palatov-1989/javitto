package com.solar.academy.models.messages;

import com.solar.academy.dao.IRelative;
import lombok.Data;

import java.util.List;

@Data
public class Review extends Answer{
    
    @IRelative.ToList
    public List<String> photos;

    public String   postID;
    public String   header;
}   //
