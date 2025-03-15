package com.solar.academy.dto.messages;

import com.solar.academy.dto.RequestBase;
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
public class ReviewDTO extends AnswerDTO {

    @NotBlank( message = "cant assign with header==null" )
    String header;

    List<String> photos;
}
