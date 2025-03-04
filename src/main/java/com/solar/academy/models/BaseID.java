package com.solar.academy.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BaseID {
    private Integer key;
    private Integer host;

    @JsonIgnore     public  Integer getKeyInt()   { return key; }
    @JsonIgnore     public  String  getKey()      { return Integer.toHexString(key); }
    @JsonIgnore     public  Integer getHostInt()  { return host; }
    @JsonIgnore     public  String  getHost()     { return Integer.toHexString(host);}

    @JsonIgnore
    public BaseID setKey(String str)
    {
        key = Integer.parseUnsignedInt(str, 16);
        return this;
    }
    @JsonIgnore
    public BaseID setKey(Integer i)
    {
        key = i;
        return this;
    }

    @JsonIgnore
    public BaseID setHost(String str)
    {
        host = Integer.parseUnsignedInt(str, 16);
        return this;
    }
    @JsonIgnore
    public BaseID setHost(Integer i)
    {
        host = i;
        return this;
    }
}
