package com.solar.academy.dao.messages;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Future;

import com.solar.academy.dao.AbstractDAO;
import com.solar.academy.cache.Cache;
import com.solar.academy.models.messages.Message;
import com.solar.academy.models.messages.StringMsg;

abstract public class MessageDAO implements AbstractDAO {
    abstract Cache db();

    public List<?> getByHost(String hostID) throws Exception
    {
        return db().api().getPrivate( hostID, dataclass() );
    }
    public <T extends StringMsg> String create(T post, String host) throws Exception
    {
        final var key = db().executor().addPrivate( host, post, dataclass() );
        putNewKey(host, String.valueOf(key.get()) , db());
        return (String)key.get();
    }

    public <T extends StringMsg> void edit(T post, String id) throws Exception
    {
        if ( db().api().getBytes(id) ==null )
            throw new Exception("not found key: "+id);

        System.out.println( db().api().getBytes(id) );
        db().executor().put( id, post );
    }

    public void delete(String host, String key) throws Exception
    {
        if ( db().api().getBytes(key) ==null )
            throw new Exception("not found key: "+key);
        else db().api().deleteBytes(key, true);

        removePrivateKey( host, List.of(key), db() );
        db().api().delete( key, dataclass() );
    }

    public void delAllByPost(String postID) throws Exception 
    {
        Exception[] err = {null};
        if( dataclass() == Message.class ) {
        throw new Exception("\nDELETING PRIVATE MESSAGE ?by post?\n");
        }
        var remove = db().api().getPrivate( postID, dataclass() ).stream()
            .map( 
                msg-> ((StringMsg) msg).getId()
            ).peek(
                (key)-> {
                    try {
                        db().api().delete( key, dataclass() );
                    } catch (Exception e) {
                        System.err.println(e.getMessage());
                        err[0]=e;
                    }
                }
            ).toList();

        removePrivateKey( postID, remove, db() );
        if( err[0]!=null ) throw err[0];
    }

    public void delAllFromSender( String userID, String sender ) throws Exception
    {
        Exception[] err = {null};
        var remove = db().api().getPrivate( userID, dataclass() ).stream()
            .filter(
                msg-> ((StringMsg) msg).sender.equals(sender)
            ).map( 
                msg-> ((StringMsg) msg).getId()
            ).peek(
                (key)-> {
                    try {

                        db().api().delete( key, dataclass() );

                    } catch (Exception e) {
                        System.err.println(e.getMessage());
                        err[0]=e;
                    }
                }
            ).toList();

        removePrivateKey( userID, remove, db() );
        if( err[0]!=null ) throw err[0];
    }
}
