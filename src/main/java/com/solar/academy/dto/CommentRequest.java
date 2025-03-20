package com.solar.academy.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CommentRequest {
    String key;
    String postID;
    String sender;
    String body;
}
