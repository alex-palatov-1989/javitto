package com.solar.academy.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class BaseID {

    String  id   ;
    String  host ;

    @JsonIgnore public  String  getKey()
    {
        return id;
    }
    @JsonIgnore public  BaseID  setKey(Integer i)
    {
        id = Integer.toString(i,16);
        return this;
    }
    @JsonIgnore     public BaseID setKey(String str) {
        id = str;
        return this;
    }

    public LocalDateTime datetime;
}
