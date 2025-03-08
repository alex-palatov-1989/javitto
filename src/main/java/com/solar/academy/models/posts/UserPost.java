package com.solar.academy.models.posts;

import java.util.List;
import com.solar.academy.dao.IRelative;
import com.solar.academy.models.messages.Answer;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
public class UserPost extends FullPost{    

    @IRelative.NoVal
    List<String>     related;

    @IRelative.ToList 
    List<Answer>     answers;
}
