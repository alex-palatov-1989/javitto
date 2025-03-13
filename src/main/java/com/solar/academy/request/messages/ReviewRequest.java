package com.solar.academy.request.messages;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ReviewRequest implements IMessageRequest{

    @NotBlank( message = "cant assign with postID==null" )
    String postID;

    @NotBlank( message = "dont post empty msg" )
    String message;

    @NotBlank( message = "cant assign with sender==null" )
    String sender;

    @NotBlank( message = "cant assign with header==null" )
    String header;

    public
    LocalDateTime createDateTime;
    public List<String> photos;
}
