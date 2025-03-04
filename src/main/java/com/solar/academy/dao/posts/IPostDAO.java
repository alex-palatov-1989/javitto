package com.solar.academy.dao.posts;

import java.util.List;

import com.solar.academy.dao.posts.PostDAO.SearchFlags;
import com.solar.academy.models.posts.BasePost;

public interface IPostDAO {


    public SearchFlags     getFlags();

    public List<BasePost>  defaultSearch    (String tag);
    public List<BasePost>  searchCategory   (String tag);
    public List<BasePost>  searchInUsers    (String tag);
    public List<BasePost>  searchOnMarket   (String tag);

    public List<BasePost>  findText(String tag, SearchFlags flags);
}
