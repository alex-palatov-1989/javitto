package com.solar.academy.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;


@Data
public class BaseID {
    public Integer id  =0;
    public Integer host=0;

    public  String  getKey()    { return Integer.toHexString(id); }
    public  String  getOwner()  { return Integer.toHexString(host);}

    public BaseID setKey(Integer i) {
        id = i;    return this;
    }
    public BaseID setOwner(Integer i) {
        host = i;   return this;
    }

    @JsonIgnore     public BaseID setHost(String str)
    {
        host = Integer.parseUnsignedInt(str, 16);
        return this;
    }
    @JsonIgnore     public BaseID setKey(String str) {
        id = Integer.parseUnsignedInt(str, 16);
        return this;
    }
}
