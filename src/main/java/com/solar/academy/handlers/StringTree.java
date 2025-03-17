package com.solar.academy.handlers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.JSONArray;
import org.json.JSONObject;


public class StringTree {
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Leaf{}    

    public String id = null;

    @Getter @Setter
    protected StringTree parent;
    @Getter protected HashMap<String, StringTree> children;

    protected StringTree(String id) {
        this.id         = id;
        this.children   = new HashMap<>();
        this.parent     = null;
    }

    private
    Stream<Field> getLeafs(){
        return 
            Stream.of(getClass().getDeclaredFields())
            .filter(
                field-> field.isAnnotationPresent(Leaf.class)
            )
            .peek(
                field-> field.setAccessible(true)
            );            
    }
    private
    JSONObject leafToJSON(){
        JSONObject template =  new JSONObject();
        getLeafs().forEach( (field)->{
            try {
                template.put(field.getName(), field.get(this));
            } catch (Exception e) { e.printStackTrace();
            }
        } );
        return template;
    }
    public 
    JSONObject toJSON() {
        JSONObject template = leafToJSON();                
        JSONArray arr = new JSONArray();

        for( var child:children.values() ) 
        arr.put(child.toJSON());            

        if( !children.isEmpty() )
        template.put("inner",  arr);
        template.put("id", this.id);        
        return template;
    }
    
    @SuppressWarnings("unchecked")
    public <T extends StringTree> void fromJSON( JSONObject json, Class<T> clazz ) {
        getLeafs().forEach(field -> {
            try {
                if (json.has(field.getName())) 
                    field.set(this, json.get(field.getName()));                
            } catch (Exception e) { e.printStackTrace();
            }
        });
        if (json.has("inner")) {
            JSONArray arr = json.getJSONArray("inner");
            arr.toList().stream().forEach(
                (childJson)->{
                    try {
                        T child = (T)clazz.getDeclaredConstructors()[0].newInstance((Object)null);
                        if( childJson instanceof JSONObject )
                            child.fromJSON( (JSONObject) childJson , clazz );
                        else
                            child.fromJSON(  new JSONObject((Map<?, ?>) childJson), clazz );

                        this.children.put(child.id, child);                            
                    } catch (Exception e) { e.printStackTrace();
                    }                        
                }
            );
        }
        if( json.has("id") )this.id = json.getString("id");        
    }
}
