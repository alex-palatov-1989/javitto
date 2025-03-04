package com.solar.academy.models.posts;

import com.solar.academy.handlers.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;
import com.solar.academy.dao.IRelative;

@Getter @Setter
@NoArgsConstructor
public class FullPost extends BasePost{
    
    @IRelative.ToList
    public List<String> photos;

    public Comment  rootComment;
    public String   description;
}
