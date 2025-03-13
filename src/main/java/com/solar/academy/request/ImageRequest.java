package com.solar.academy.request;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data

public class ImageRequest {
    @NotBlank ( message = "must have ID to assign to entity" )
    String hostID;

    @NotBlank ( message = "must have URL of uploaded image" )
    String imageURL;
}
