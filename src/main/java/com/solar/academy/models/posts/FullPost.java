package com.solar.academy.models.posts;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.solar.academy.handlers.Comment;
import com.solar.academy.handlers.CommentSerializer;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;
import com.solar.academy.dao.IRelative;
import netscape.javascript.JSObject;
import org.json.JSONObject;


@Data
public class FullPost extends BasePost{
    
    @IRelative.ToList
    public List<String> photos;

    public String       description;

    @JsonSerialize  (using = CommentSerializer.Serializer.class)
    @JsonDeserialize(using = CommentSerializer.Deserializer.class)
    public Comment      rootComment;
}
