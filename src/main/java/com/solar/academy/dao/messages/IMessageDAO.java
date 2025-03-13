package com.solar.academy.dao.messages;

import java.util.List;

import com.solar.academy.models.messages.StringMsg;

public interface IMessageDAO {

    public List<?> getByHost(String hostID) throws Exception;

    public <T extends StringMsg> void edit  (T post, String id)   throws Exception;
    public <T extends StringMsg> void create(T post, String host) throws Exception;

    public void delete(String host, String key)     throws Exception;
    public void delAllByPost(String postID)         throws Exception;

    public void delAllFromSender( String userID, String sender ) throws Exception;
    
}
