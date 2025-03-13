package com.solar.academy.mapping;

import com.solar.academy.models.messages.Answer;
import com.solar.academy.models.messages.Message;
import com.solar.academy.models.messages.Review;
import com.solar.academy.dto.messages.AnswerDTO;
import com.solar.academy.dto.messages.LetterDTO;
import com.solar.academy.dto.messages.ReviewDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper( componentModel = "spring" )
public interface MessageMapper {

/*=======================================================*/

    @Mapping(target = "createDateTime", ignore = true)
    @Mapping(target = "message",    source = "message")
    @Mapping(target = "postID",     source = "postID")
    @Mapping(target = "id",         source = "id"  )
    @Mapping(target = "host",       source = "host")

    Answer toAnswer(AnswerDTO req);
    List<AnswerDTO> toListAnswer(List<Answer>   req);

/*=======================================================*/

    @Mapping(target = "createDateTime",  ignore = true)
    @Mapping(target = "message",    source = "message")
    @Mapping(target = "postID",     source = "postID")
    @Mapping(target = "id",         source = "id"  )
    @Mapping(target = "host",       source = "host")

    @Mapping(target = "photos",     source = "photos")

    Review toReview(ReviewDTO req);
    List<ReviewDTO> toListReview(List<Review>   req);

/*=======================================================*/

    @Mapping(target = "createDateTime",  ignore = true)
    @Mapping(target = "message",    source = "message")
    @Mapping(target = "seller",     source = "seller")
    @Mapping(target = "id",         source = "id"  )
    @Mapping(target = "host",       source = "host")

    @Mapping(target = "image",      source = "image")

    Message toMessage(LetterDTO req);
    List<LetterDTO> toListLetter(List<Message>   req);

/*=======================================================*/

}
