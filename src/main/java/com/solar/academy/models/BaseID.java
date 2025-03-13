package com.solar.academy.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jdk.jfr.Unsigned;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
public class BaseID implements Serializable {
    public Integer id  =0;
    public String  host="";
    public String  getKey()
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
