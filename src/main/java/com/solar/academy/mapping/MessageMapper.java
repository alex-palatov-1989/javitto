package com.solar.academy.mapping;


import com.solar.academy.dto.messages.AnswerDTO;
import com.solar.academy.dto.messages.LetterDTO;
import com.solar.academy.dto.messages.ReviewDTO;
import com.solar.academy.models.messages.Answer;
import com.solar.academy.models.messages.Message;
import com.solar.academy.models.messages.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component  @Scope("prototype")
@Mapper( componentModel = "spring" )
public interface MessageMapper {

    /*=======================================================*/
    @Mapping(target = "datetime",   ignore = true)
    @Mapping(target = "message",    source = "message")
    @Mapping(target = "postID",     source = "postID")
    @Mapping(target = "id",         source = "id"  )
    /*=======================================================*/

    Answer toAnswer(AnswerDTO req);
    List<AnswerDTO> toListAnswer(List<Answer>   req);

    /*=======================================================*/

    @Mapping(target = "photos",     source = "photos")
    Review toReview(ReviewDTO req);
    List<ReviewDTO> toListReview(List<Review>   req);

    /*=======================================================*/

    @Mapping(target = "seller",     source = "seller")
    @Mapping(target = "image",      source = "image")
    Message toMessage(LetterDTO req);
    List<LetterDTO> toListLetter(List<Message>   req);

    /*=======================================================*/
}
