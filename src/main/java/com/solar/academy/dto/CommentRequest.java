package com.solar.academy.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CommentRequest {
    @NotBlank ( message = "err cause key is empty" )    String key;
    @NotBlank ( message = "err cause positID null" )    String postID;
    @NotBlank ( message = "err cause body is empty" )   String sender;
    @NotBlank ( message = "err cause body is empty" )   String body;
}
