package com.solar.academy.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data

public class PromoRequest {

    @NotBlank ( message = "dont add posters without image" )
    String mainImg;

    @NotBlank ( message = "cant assign with autorID==null" )
    String autorID;

    @NotBlank ( message = "dont add posters without header" )
    String header;

    @Positive( message = "must have positive price" )
    Float price;

    @NotBlank ( message = "must have category tag or default root" )
    String category;

    @NotBlank ( message = "add description to post" )
    String description;
}
