package com.solar.academy.dao.posts;

import com.solar.academy.models.posts.BasePost;
import com.solar.academy.models.posts.FullPost;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Optional;
import java.util.Set;

public interface IPostDAO<T> {
    void        edit(T post, String id) throws Exception;
    String      create(T post)          throws Exception;
    Optional<T> get(String id);

    HashMap<Integer, BasePost>  getByTag   (String tag, Set<Integer> skip)     throws Exception;
    HashMap<Integer, BasePost>  getByHeader(String tag, Set<Integer> skip)     throws Exception;
    HashMap<Integer, BasePost>  getByText  (String tag, Set<Integer> skip)     throws Exception;
}
