package com.solar.academy.dao;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface IRelative{

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ToList{}    

    @Target(ElementType.FIELD)    
    @Retention(RetentionPolicy.RUNTIME)
    public @interface NoVal{}
}