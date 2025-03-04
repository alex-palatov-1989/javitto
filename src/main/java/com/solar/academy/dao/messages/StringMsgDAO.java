package com.solar.academy.dao.messages;

import java.util.List;

import com.solar.academy.dao.BaseDAO;
import com.solar.academy.database.Cache;
import com.solar.academy.models.messages.Message;
import com.solar.academy.models.messages.StringMsg;

abstract public class StringMsgDAO implements BaseDAO, IMessagingDAO {
    protected Cache db;

    public List<?> getByPost(String postID) throws Exception
    {
        return db.api().getPrivate( postID, dataclass() );
    }   

    public void edit(StringMsg post, String id) throws Exception
    {
        db.api().putId( id, post );
    }

    public void create(StringMsg post, String host) throws Exception
    {
        db.executor().addPrivate( host, post, dataclass() );
    }

    public void delete(String host, String key) throws Exception
    {
        db.api().deletePrivate( host, key, dataclass() );
    }

    public void delAllByPost(String postID) throws Exception 
    {
        Exception[] err = {null};
        if( dataclass() == Message.class ) throw new Exception("\nDELETING PRIVATE MESSAGE ?by post?\n");

        db.api().getPrivate( postID, dataclass() ).stream()
            .map( 
                msg-> ((StringMsg) msg).getKey()
            ).forEach(
                (key)-> {
                    try {
                        db.api().deletePrivate( postID, key, dataclass() );
                    } catch (Exception e) {
                        System.err.println(e.getMessage());
                        err[0]=e;
                    }
                }
            );
        if( err[0]!=null ) throw err[0];
    }

    public void delAllFromSender( String userID, String sender ) throws Exception
    {
        Exception[] err = {null};
        db.api().getPrivate( userID, dataclass() ).stream()
            .filter(
                msg-> ((StringMsg) msg).sender == sender
            ).map( 
                msg-> ((StringMsg) msg).getKey()
            ).forEach(
                (key)-> {
                    try {
                        db.api().deletePrivate( userID, key, dataclass() );
                    } catch (Exception e) {
                        System.err.println(e.getMessage());
                        err[0]=e;
                    }
                }
            );
        if( err[0]!=null ) throw err[0];
    }
}
