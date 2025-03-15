package com.solar.academy.dto.messages;

import com.solar.academy.dto.RequestBase;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor @NoArgsConstructor
public class AnswerDTO extends RequestBase {

    @NotBlank( message = "cant assign with postID==null" )
    private String postID;

    @NotBlank( message = "dont post empty msg" )
    private String message;

    @NotBlank( message = "cant assign with sender==null" )
    private String sender;

}
