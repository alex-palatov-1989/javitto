package com.solar.academy.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
public class BaseID implements Serializable {
    private Integer id  =0;
    private String  host="";


    public  String  getKey()
    {
        return Integer.toHexString(id);
    }
    @JsonIgnore public  BaseID  setKey(Integer i)
    {
        id = i;
        return this;
    }
    @JsonIgnore     public BaseID setHost(String str)
    {
        host = str;
        return this;
    }
    @JsonIgnore     public BaseID setKey(String str) {
        id = Integer.parseUnsignedInt(str, 16);
        return this;
    }

    public
    LocalDateTime datetime;
}
