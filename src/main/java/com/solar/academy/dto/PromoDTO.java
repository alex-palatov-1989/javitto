package com.solar.academy.dto;


import com.solar.academy.dto.messages.AnswerDTO;
import com.solar.academy.dto.messages.ReviewDTO;
import com.solar.academy.handlers.Comment;
import com.solar.academy.models.BaseID;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data

public class PromoDTO extends RequestBase{

    @NotBlank ( message = "dont add posters without image" )
    String mainImg;

    @NotBlank ( message = "cant assign with autorID==null" )
    String authorID;

    @NotBlank ( message = "dont add posters without header" )
    String header;

    @Positive( message = "must have positive price" )
    Float price;

    @NotBlank ( message = "must have category tag or default root" )
    String category;

    @NotBlank ( message = "add description to post" )
    String description;


    List<BaseID>        photos;
    List<ReviewDTO>     reviews;
    List<AnswerDTO>     answers;
    List<PromoDTO>      related;
    Comment         rootComment;
}
