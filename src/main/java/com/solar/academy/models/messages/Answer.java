package com.solar.academy.models.messages;

import com.solar.academy.models.BaseID;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Data
public class Answer extends StringMsg{

    public String postID;
}   // any post can has answers
    // answer is independent table
