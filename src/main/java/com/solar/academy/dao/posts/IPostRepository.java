package com.solar.academy.dao.posts;

import java.util.List;

import com.solar.academy.models.posts.BasePost;
import com.solar.academy.models.posts.MarketPost;
import com.solar.academy.models.posts.UserPost;

public interface IPostRepository {

    public List<BasePost>  defaultSearch    (String tag);
    public List<BasePost>  searchCategory   (String tag);
    public List<BasePost>  searchInUsers    (String tag);
    public List<BasePost>  searchOnMarket   (String tag);

    public List<BasePost>  findText(String tag, SearchFlags flags);
    public SearchFlags     getFlags();
    final static class SearchFlags{
        public boolean  byCat, byText, byHead = true;
        public boolean  inUsers   = true;
        public boolean  onMarket  = true;
    }

    IPostDAO<UserPost>      getUsers();
    IPostDAO<MarketPost>    getMarkets();
}
