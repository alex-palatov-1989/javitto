package com.solar.academy.models.posts;

import com.solar.academy.models.BaseID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class BasePost extends BaseID{    
    
    public String mainImg;
    public BaseID autorID;

    public String   header;
    public String    price;    
    public String category;
}
