package com.solar.academy.dto.messages;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class LetterDTO extends MessageBase {

    @NotBlank( message = "dont post empty msg" )
    private String message;

    @NotBlank( message = "cant assign with sender==null" )
    private String sender;

    @NotBlank( message = "cant assign with seller==null" )
    private String seller;

    String image  ;
}
