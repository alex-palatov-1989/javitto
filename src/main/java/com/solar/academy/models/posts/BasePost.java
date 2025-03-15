package com.solar.academy.models.posts;

import com.solar.academy.models.BaseID;
import lombok.Data;

@Data
public class BasePost extends BaseID{    
    
    public String mainImg;
    public String authorID;

    public String   header;
    public String    price;    
    public String category;
}
