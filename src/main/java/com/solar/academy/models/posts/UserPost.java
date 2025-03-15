package com.solar.academy.models.posts;

import java.util.List;
import com.solar.academy.dao.IRelative;
import com.solar.academy.models.messages.Answer;
import lombok.Data;

@Data
public class UserPost extends FullPost{    

    @IRelative.ToList 
    List<Answer>     answers;
}
