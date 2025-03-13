package com.solar.academy.services;

import com.solar.academy.dao.posts.IPostRepository;
import com.solar.academy.mapping.MessageMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PostService {

    IPostRepository repository;
    CategoryService tagService;
    MessageMapper   msgMapper;

}


