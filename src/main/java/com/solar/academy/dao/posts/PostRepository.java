package com.solar.academy.dao.posts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.solar.academy.database.Cache;
import com.solar.academy.models.posts.BasePost;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
final public class PostRepository extends PostDAO implements IPostRepository{
        
    @Getter final UserPostDAO      users   = new UserPostDAO();
    @Getter final MarketPostDAO    markets = new MarketPostDAO();

    public SearchFlags     getFlags() { return new SearchFlags(); }
    
    public List<BasePost>  defaultSearch(String tag)
    {
        return findText(tag, getFlags());
    }
    public List<BasePost>  searchCategory(String tag)
    {
        var cat = getFlags(); cat.byCat=true; cat.byHead = false;
        return findText( tag, cat );
    }
    public List<BasePost>  searchInUsers(String tag)
    {
        var cat = getFlags(); cat.byCat=true; cat.onMarket = false;
        return findText( tag, cat );
    }
    public List<BasePost>  searchOnMarket(String tag)
    {
        var cat = getFlags(); cat.byCat=true; cat.inUsers = false;
        return findText( tag, cat );
    }

    public List<BasePost>  findText(String tag, SearchFlags flags){
        var search = new HashMap<Integer, BasePost>();            
        var ret = new ArrayList<BasePost>();      

        try {
            var skip = new HashSet<Integer>();
            if(flags.inUsers){      
                if(flags.byHead)    search.putAll(users.getByHeader (tag, users.dataclass(), null));
                skip.addAll(search.keySet());

                if(flags.byCat)     search.putAll(users.getByTag     (tag, users.dataclass(), skip));
                skip.addAll(search.keySet());

                if(flags.byText)    search.putAll(users.getByText    (tag, users.dataclass(), skip));
                skip.clear();

            ret.addAll(search.values());
            }
            if(flags.onMarket){     
                if(flags.byHead)    search.putAll(markets.getByHeader  (tag, markets.dataclass(), null));
                skip.addAll(search.keySet());

                if(flags.byCat)    search.putAll(markets.getByTag      (tag, markets.dataclass(), skip));
                skip.addAll(search.keySet());
                
                if(flags.byText)   search.putAll(markets.getByText     (tag, markets.dataclass(), skip));
                skip.clear();

            ret.addAll(search.values());
            }            
        } catch   ( Exception e  ) {     e.printStackTrace();
        } finally { System.gc(); }           
        return ret;
    }

    @Autowired Cache _db;
    @Override synchronized Cache db() {
        return  _db;
    }
}
