package com.solar.academy.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public interface IQuerySide {

    public String getBytes(String key)                                      throws Exception ;
    public <T>  T get(String id, Class<T> clazz )                           throws Exception ;
    public <T> List<T> getPrivate(String hostId, Class<?> clazz)            throws Exception ;

    public void delete (String key, Class<?> clazz)                         throws Exception;
    public void deleteBytes  ( String hshKey )                              throws Exception;

    public <T> HashMap<Integer , T> getAll( Class<T> clazz )                throws Exception;
    public <T> HashMap<Integer , T> equal ( Class<T> clazz, 
        String field,   Object value, 
        Set<Integer>    skipKeys)       throws Exception ;   


    public <T> HashMap<Integer , T> filter( Class<T> clazz, 
        String field, Object value, 
        Predicate       pred, 
        Set<Integer>    skipKeys)       throws Exception ;


    @FunctionalInterface 
    public interface Predicate { boolean filter(Object lhs, Object rhs); }     
}
