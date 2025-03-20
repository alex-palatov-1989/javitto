package com.solar.academy.dto.messages;

import jakarta.validation.constraints.NotBlank;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AnswerDTO extends MessageBase {

    @NotBlank( message = "cant assign with postID==null" )
    private String postID;

    @NotBlank( message = "dont post empty msg" )
    private String message;

    @NotBlank( message = "cant assign with sender==null" )
    private String sender;

}
