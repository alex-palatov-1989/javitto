package com.solar.academy.dto.messages;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ReviewDTO extends MessageBase {

    @NotBlank( message = "cant assign with postID==null" )
    private String postID;

    @NotBlank( message = "dont post empty msg" )
    private String message;

    @NotBlank( message = "cant assign with sender==null" )
    private String sender;

    @NotBlank( message = "cant assign with header==null" )
    private String header;

    List<String> photos;
}
