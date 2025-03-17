package com.solar.academy.dao;


import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.solar.academy.cache.Cache;
import com.solar.academy.models.BaseID;
import lombok.AllArgsConstructor;

public interface AbstractDAO<T>{

    default Class<?> dataclass() throws ClassNotFoundException {
        return Class.forName( AbstractDAO.class.getTypeParameters()[0].getTypeName());
    };

    default void putNewKey(String host, String key, Cache db) throws Exception{

        final var  ids  =  db.api().getBytes(host);
        db.api().putBytes(host, ids+"::"+key);
    }

    default void removePrivateKey( String hshKey, List<String> keys, Cache db ) throws Exception {
        final var hsh = db.api().getBytes(hshKey);
        String    upd = "";

        if( hsh!=null ){
            upd = Arrays.asList( hsh.split("::") ).parallelStream()
                    .filter( id->keys.contains(id) ).collect(Collectors.joining("::"));
        }
        if( hsh == null )
            throw new Exception("not found hash table");
        db.api().putId( hshKey, upd );
    }

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
                                        db.executor().put( key+rec.getKey(), item );
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
            db.executor().put(key,value);
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
            var added = fibers.flatMap(fib->fib ).filter(Objects::nonNull)
                .map( (fib)-> {
                    try {
                      return((CompletableFuture<?>) fib).get();
                    } catch ( Exception e ) { e.printStackTrace(); return null; } 
                    }).collect(Collectors.joining("::"));

            var having = db.api().getBytes(key);
            if( having==null ) having = "";
            db.api().putBytes( key, having+added );
            db.api().putId( key, value );
            db.api().commit();

            if( err[0]!=null )err[0].printStackTrace();
            return  key;
        } catch (Exception e) { System.err.println(e.getMessage()); throw e;
        }   
    }   

    default <T> T read(String key, Cache db) {

        Exception[] err = {null};
        var colls = createMap();

        try {            
            var data  = (T)db.api().get(key, dataclass());
            if( data != null )
                colls.stream().forEach(
                    (g)->{
                        try {      
                            if(g.field.isAnnotationPresent(IRelative.ToList.class))    
                            {
                                final var list = (List<?>)db.api().getPrivate(key, g.getParam());
                                if ((list == null)) {
                                    g.field.set(data, new ArrayList<>());
                                } else {
                                    g.field.set(data, list);
                                }
                            }
                        } catch (Exception e) {                             
                            err[0] = e;
                            System.err.println(e.getMessage());
                        }                    
                    }
                );  
                else {
                System.err.println( "\n>_ readed null on key:"+key);
                return null;
            }

            if(err[0]!=null)throw err[0];
            else return data;
        } catch (Exception e) { 
            e.printStackTrace();
            return null;
        }   
    }    

    default List<Generic> createMap(){
        try {
            HashMap<String, Field> recursive = new HashMap<>();
            var clazz = dataclass();
            while( !clazz.equals(Object.class) )
            {                
                recursive.putAll(
                    Stream.of( clazz.getDeclaredFields() )
                    .filter(
                        f->f.getAnnotations().length != 0
                    ).collect( Collectors.toMap
                        (Field::getName, e->e )
                    )                                       
                );                
                clazz=clazz.getSuperclass();
            }
            return recursive.values().stream().filter(
                f ->    f.isAnnotationPresent(IRelative.ToList.class)
                        ||   f.isAnnotationPresent(IRelative.NoVal.class)
            ).peek(
                f ->    f.setAccessible(true) 
            ).map( f-> new Generic(f, f.getType(), f.getGenericType()) )
            .toList();
            
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }   return null;
    }

    @AllArgsConstructor
    class Generic{
        public Field             field;
        public Class<?>          clazz;
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

