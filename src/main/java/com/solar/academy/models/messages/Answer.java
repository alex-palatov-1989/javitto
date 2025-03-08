package com.solar.academy.models.messages;

import com.solar.academy.models.BaseID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter @Setter
public class Answer extends StringMsg{

    public BaseID post;
}   // any post can has answers
    // answer is independent table
