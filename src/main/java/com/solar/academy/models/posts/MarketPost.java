package com.solar.academy.models.posts;

import java.util.List;
import com.solar.academy.dao.IRelative;
import com.solar.academy.models.messages.Review;
import lombok.Data;

@Data
public class MarketPost extends FullPost{        

    @IRelative.ToList
    List<Review>    reviews;
}
