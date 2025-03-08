package com.solar.academy.models.posts;

import com.solar.academy.models.BaseID;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
public class BasePost extends BaseID{    
    
    public String mainImg;
    public String autorID;

    public String   header;
    public String    price;    
    public String category;
}
