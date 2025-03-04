package com.solar.academy.handlers;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Comment extends StringTree {
    
    @Leaf private String sender; 
    @Leaf private String text;
    static Set<Integer> idxs = new HashSet<>();

    public Comment(int id, String sender, String text) {
        super( String.valueOf(id) );
        this.sender = sender;
        this.text = text;
    }
    public Comment addChild(String sender, String text) {
        Integer id;
        synchronized( idxs ){            
            do id = Math.abs(UUID.randomUUID().hashCode());
            while( idxs.contains(id) );            
            idxs.add(id);
        }
        Comment child = new Comment(id, sender, text);
        child.setParent(this);
        children.put(String.valueOf(id), child);
        return child;
    }
    public Comment findById(String id) {
        if( this.id.equals(id) ){
            return this;
        } else
        return children.values().stream()
            .map(child -> ((Comment)child).findById(id))
            .filter( find->find!=null )
            .findFirst().orElse( null );
    }
    public void deleteByid(String id) {

        Comment del = findById(id);
        if( del!=null ){
            Comment upper = (Comment)del.getParent();
            if( upper!=null ){
                del.getChildren().values().forEach(child -> {
                    child.setParent(upper);
                    upper.children.put(child.id, child);
                });
                upper.children.remove(id);
            }
        }
    }
}