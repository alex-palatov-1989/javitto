package com.solar.academy.database;

import lombok.Getter;

public interface ICommandSide {
    public <T> void   putId(String key, T val);
    public <T> Object put( T val );
    public <T> Object addPrivate(String hostId, T value, Class<?> clazz);
    enum COMMAND{        
        PUT ("putId"), 
        POST("put"), 
        ADD ("addPrivate");
        @Getter final private String mtdName;
        COMMAND(String s){ mtdName = s; };
    }
    public <T> String getNewKey(T val);
    public void commit();
}
