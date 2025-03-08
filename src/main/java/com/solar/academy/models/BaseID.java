package com.solar.academy.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;


@Data
public class BaseID {
    public Integer key = 0;
    public Integer host= 0;
    public Integer getID()    { return key; }
    public Integer getOwner() { return host; }

    @JsonIgnore     public  String  getKey()      { return Integer.toHexString(key); }
    @JsonIgnore     public  String  getHost()     { return Integer.toHexString(host);}

    @JsonIgnore     public BaseID setKey(Integer i) {
        key = i;    return this;
    }
    @JsonIgnore     public BaseID setHost(Integer i) {
        host = i;   return this;
    }
    @JsonIgnore     public BaseID setHost(String str)
    {
        host = Integer.parseUnsignedInt(str, 16);
        return this;
    }
    @JsonIgnore     public BaseID setKey(String str) {
        key = Integer.parseUnsignedInt(str, 16);
        return this;
    }
}
