package com.solar.academy.dao.posts;

public interface IPostDAO<T> {
    void edit(T post, String id)throws Exception;
    String create(T post) throws Exception;
    T get(String id) throws Exception;
}
