package com.solar.academy.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public abstract class RequestBase
{
    LocalDateTime datetime;
    @NotBlank Integer id;
    @NotBlank String host;
}
