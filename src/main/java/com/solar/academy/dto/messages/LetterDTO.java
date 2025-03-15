package com.solar.academy.dto.messages;

import com.solar.academy.dto.RequestBase;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class LetterDTO extends AnswerDTO {

    @NotBlank( message = "cant assign with seller==null" )
    String seller;

    String image;
}
