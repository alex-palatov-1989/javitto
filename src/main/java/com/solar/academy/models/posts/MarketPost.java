package com.solar.academy.models.posts;

import java.util.List;
import com.solar.academy.dao.IRelative;
import com.solar.academy.models.messages.Review;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
public class MarketPost extends FullPost{        

    @IRelative.NoVal
    List<String>    related;

    @IRelative.ToList
    List<Review>    reviews;
}
