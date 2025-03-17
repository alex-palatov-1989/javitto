package com.solar.academy.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public abstract class RequestBase
{
    LocalDateTime datetime;

    @NotBlank ( message = "err cause id is empty" )
    Integer id;

    @NotBlank ( message = "err cause host is empty" )
    String host;
}
