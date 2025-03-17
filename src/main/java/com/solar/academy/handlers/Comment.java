package com.solar.academy.handlers;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

public class Comment extends StringTree {

    @Getter @Setter
    @Leaf private String sender;

    @Getter @Setter
    @Leaf private String text;

    final Set<Long> idxs = new HashSet<>();
    static long getRnd(){ return UUID.randomUUID().getLeastSignificantBits(); }

    public
    Comment(){ this(0, null, null); }
    Comment(long id, String sender, String text) {
        super( String.valueOf(id) );
        this.sender = sender;
        this.text = text;
    }
    public Comment addChild(String sender, String text) {
        long id;
        synchronized( idxs ){            
            do id = getRnd();
            while( idxs.contains(id) );            
            idxs.add(id);
        }
        Comment child = new Comment(id, sender, text);
        child.setParent(this);
        children.put(String.valueOf(id), child);
        return child;
    }
    public Comment findById(String id) throws NoSuchElementException{
        if( this.id.equals(id) ){
            return this;
        } else {
            return children.values().stream()
                   .map(child -> ((Comment)child).findById(id))
                   .filter(Objects::nonNull).toList().getFirst();
        }
    }
    public void deleteById(String id) {
        var del = findById(id);
        if( del!=null ){
            del.getParent().children.remove(del.id);
        }
    }
}