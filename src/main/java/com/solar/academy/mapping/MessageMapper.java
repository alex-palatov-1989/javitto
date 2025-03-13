package com.solar.academy.mapping;

import com.solar.academy.models.messages.Answer;
import com.solar.academy.models.messages.Message;
import com.solar.academy.models.messages.Review;
import com.solar.academy.request.messages.AnswerRequest;
import com.solar.academy.request.messages.LetterRequest;
import com.solar.academy.request.messages.ReviewRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper( componentModel = "spring" )
public interface MessageMapper {

/*=======================================================*/
    @Mapping(target = "id",             ignore = true)
    @Mapping(target = "host",           ignore = true)
    @Mapping(target = "createDateTime", ignore = true)
    @Mapping(target = "postID",     source = "postID")
    @Mapping(target = "seller",     source = "seller")
    @Mapping(target = "message",    source = "message")
    Answer toAnswer(AnswerRequest advertisementRequest);
/*=======================================================*/
    @Mapping(target = "id",             ignore = true)
    @Mapping(target = "host",           ignore = true)
    @Mapping(target = "createDateTime", ignore = true)
    @Mapping(target = "postID",     source = "postID")
    @Mapping(target = "seller",     source = "seller")
    @Mapping(target = "message",    source = "message")

    @Mapping(target = "image",    source = "image")
    Message toMessage(LetterRequest advertisementRequest);
/*=======================================================*/
    @Mapping(target = "id",             ignore = true)
    @Mapping(target = "host",           ignore = true)
    @Mapping(target = "createDateTime", ignore = true)
    @Mapping(target = "postID",     source = "postID")
    @Mapping(target = "seller",     source = "seller")
    @Mapping(target = "message",    source = "message")

    @Mapping(target = "photos",    source = "photos")
    Review toReview(ReviewRequest advertisementRequest);
/*=======================================================*/

}
