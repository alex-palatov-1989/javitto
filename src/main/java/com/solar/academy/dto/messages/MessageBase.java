package com.solar.academy.dto.messages;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public abstract class MessageBase
{
    LocalDateTime   datetime;
    String          id;
}
