package com.solar.academy.models.posts;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.solar.academy.handlers.Comment;
import com.solar.academy.handlers.CommentSerializer;
import lombok.Data;

import java.util.List;
import com.solar.academy.dao.IRelative;


@Data
public class FullPost extends BasePost{

    @IRelative.NoVal
    List<BasePost>      related;

    @IRelative.ToList
    public List<String> photos;

    public String       description;

    @JsonSerialize  (using = CommentSerializer.Serializer.class)
    @JsonDeserialize(using = CommentSerializer.Deserializer.class)
    public Comment      rootComment;
}
