package com.solar.academy.dto.messages;

import lombok.Data;
import lombok.Setter;

import java.time.LocalDateTime;

public abstract class IMessageDTO
{
    @Setter
    LocalDateTime datetime;
}
