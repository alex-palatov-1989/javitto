package com.solar.academy.handlers;

public class WrappedModel<T>{
    private T        _value = null;
    private String   _key   = null;
    private Class<T> _class = null;

    public T        value  (){return _value;}
    public String   key    (){return _key;  }
    public Class<T> clazz  (){return _class;}

    public WrappedModel<T>   value  (T val)   {_value=val; return this;}
    public WrappedModel<T>   key    (String k){_key=k;     return this;}
    
    private WrappedModel(String k, Class<T> c) {_key=k;     _class=c; }
    private WrappedModel(T val,    Class<T> c) {_value=val; _class=c; }

    public static <T> WrappedModel<T> fromKey(String k, Class<T> clazz){
        return new WrappedModel<T>(k, clazz);
    }
    public static <T> WrappedModel<T> fromVal(T val, Class<T> clazz){
        return new WrappedModel<T>(val, clazz);
    }
}
