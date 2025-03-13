package com.solar.academy.services;

import com.solar.academy.dao.posts.IPostRepository;
import org.springframework.stereotype.Service;

@Service
public class PostService {
    IPostRepository repository;
    CategoryService tagService;
}


