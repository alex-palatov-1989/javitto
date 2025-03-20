package com.solar.academy.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class BaseID {

    String  id;
    String  host;
    String  datetime;
}
