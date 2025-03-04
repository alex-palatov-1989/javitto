package com.solar.academy.dao;


import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.solar.academy.database.Cache;
import com.solar.academy.models.BaseID;
import lombok.AllArgsConstructor;

public interface BaseDAO{

    public Class<?> dataclass();

    @SuppressWarnings("unchecked")
    default <T> void write(String key, T value, Cache db) throws Exception{
        
        Exception[] err = {null};
        var colls = createMap( );
        try {
            colls.forEach(
                (g)->{
                    try {                
                        if( g.field.get(value)==null )       return;
                        if( g.field.isAnnotationPresent(IRelative.ToList.class))
                        {
                            final var val = (List)g.field.get(value);
                            if( val!=null ) val.forEach(
                                (item)->{
                                    try {
                                        final var rec = (BaseID)item;
                                        db.api().putId( key+rec.getKey(), item );
                                    } catch (Exception e) { e.printStackTrace();
                                    }
                                });                            
                        }
                        g.field.set(value, null);

                    } catch (Exception e) {
                        System.err.println(e.getMessage());
                        err[0] = e;
                    }
                }
            );          
            db.api().putId(key,value);
            if(err[0]!=null)throw err[0];            
        } catch (Exception e) { System.err.println(e.getMessage()); throw e;
        }
    }   

    @SuppressWarnings("unchecked")
    default <T> String create( T value, Cache db) throws Exception{

        Exception[] err = {null};
        var colls =  createMap();
        
        try {                   
            final var key = db.api().getNewKey(value);
            ((BaseID) value).setKey(key);            
            var fibers = colls.parallelStream().map(
                (g)->{
                    try {                        
                        if(g.field.isAnnotationPresent(IRelative.ToList.class))
                        {
                            final var val = (List)g.field.get(value);
                            if(val==null)return null; 

                            var stream = val.parallelStream()
                                .map( item->db.executor().addPrivate(key, item, g.getParam()) );
                        
                            g.field.set(value, null);
                            return stream;
                        }                        
                        g.field.set(value, null);
                        return null;

                    } catch (Exception e) {
                        err[0] =  e;
                        return null;                   
                    }
                }
            );                        
            var added = fibers.filter( e->e!=null ).flatMap( fib->fib )
                .map( (fib)-> {  try { 
                      return((CompletableFuture<?>) fib).get();
                    } catch ( Exception e ) { e.printStackTrace(); return null; } 
                    }).filter( e->e!=null ) .collect(Collectors.joining("::"));
            
            var having = db.api().get(key);
            if( having==null ) having = new String();
            db.executor().put( key, having+added );
            db.executor().put( key, value );
                                
            if( err[0]!=null )  err[0].printStackTrace();

            return  key;
        } catch (Exception e) { System.err.println(e.getMessage()); throw e;
        }   
    }   

    default <T> T read(String key, Cache db) throws Exception{

        Exception[] err = {null};
        var colls = createMap();

        try {            
            var data  = (T)db.api().get(key, dataclass());
            if( data != null && data instanceof T)
                colls.parallelStream().forEach(
                    (g)->{
                        try {      
                            if(g.field.isAnnotationPresent(IRelative.ToList.class))    
                            {
                                final var list = (List<?>)db.api().getPrivate(key, g.getParam());    
                                if( list==null ) 
                                g.field.set(data, new ArrayList<>());
                                else
                                g.field.set(data, list);
                            }                                                                                                                                                                                             
                        } catch (Exception e) {                             
                            err[0] = e;
                            System.err.println(e.getMessage());
                        }                    
                    }
                );  
                else return null;

            if(err[0]!=null)throw err[0];
            else return data;
        } catch (Exception e) { 
            e.printStackTrace();
            return null;
        }   
    }    

    default List<Generic> createMap(){
        List<Generic> colls = new ArrayList<>();
        try {         
            HashMap<String, Field> recursive = new HashMap<>();
            var clazz = dataclass();
            while( !clazz.equals(Object.class) )
            {                
                recursive.putAll(
                    Stream.of( clazz.getDeclaredFields() )
                    .filter(
                        f->f.getAnnotations().length!=0
                    ).collect( Collectors.toMap
                        ( e->e.getName(), e->(Field)e )
                    )                                       
                );                
                clazz=clazz.getSuperclass();
            }     
            var all = recursive.values().stream();

            colls = all.filter(
                f ->    f.isAnnotationPresent(IRelative.ToList.class) 
                    ||  f.isAnnotationPresent(IRelative.NoVal.class)
            ).peek(
                f ->    f.setAccessible(true) 
            ).map( f-> new Generic(f, f.getType(), f.getGenericType()) )
            .toList();
            
            return colls;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }   return null;
    }

    @AllArgsConstructor
    static class Generic{
        public Field             field;
        public Class             clazz;
        private Type              type;
        public Class<?> getParam(){ 
            try {
                return Class.forName(((ParameterizedType)type).getActualTypeArguments()[0].getTypeName());     
            } catch (Exception e) { e.printStackTrace();
            }
            return null;
        }
    }
}

