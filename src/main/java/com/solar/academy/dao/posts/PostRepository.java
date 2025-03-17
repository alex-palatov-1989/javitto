package com.solar.academy.dao.posts;

import java.util.*;

import com.solar.academy.cache.Cache;
import com.solar.academy.models.posts.BasePost;
import lombok.Getter;
import org.springframework.stereotype.Repository;

@Repository
final public class PostRepository extends PostDAO implements IPostRepository{
        
    @Getter UserPostDAO      users;
    @Getter MarketPostDAO    markets;
            Cache            _db;

    @Override synchronized Cache db() {
        return  _db;
    }

    public SearchFlags     getFlags()                   { return new SearchFlags(); }
    public List<BasePost>  defaultSearch(String tag)    { return findText(tag, getFlags()); }

    public List<BasePost>  searchCategory(String tag) {
        var cat = getFlags(); cat.byCat=true; cat.byHead = false;
        return findText( tag, cat );
    }
    public List<BasePost>  searchInUsers(String tag) {
        var cat = getFlags(); cat.byCat=true; cat.onMarket = false;
        return findText( tag, cat );
    }
    public List<BasePost>  searchOnMarket(String tag) {
        var cat = getFlags(); cat.byCat=true; cat.inUsers = false;
        return findText( tag, cat );
    }

    public List<BasePost>  findText(String tag, SearchFlags flags){
        var search  = new HashMap<Integer, Object>();
        var ret     = new ArrayList<BasePost>();

        try {
            var skip = new HashSet<Integer>();
            if(flags.inUsers){      
                if(flags.byHead)
                {
                    search.putAll(users.getByHeader (tag, null));
                    skip.addAll(search.keySet());
                }
                if(flags.byCat)
                {
                    search.putAll(users.getByTag    (tag,
                            skip.isEmpty() ? null : skip
                        ));
                    skip.addAll(search.keySet());
                }
                if(flags.byText)
                    search.putAll(users.getByText   (tag,
                            skip.isEmpty() ? null : skip
                        ));

                search.values().stream()
                        .map(e->(BasePost)e)
                        .forEach(e->ret.add(e));
            }

            skip.clear();   search.clear();

            if(flags.onMarket){     
                if(flags.byHead)
                {
                    search.putAll(markets.getByHeader  (tag, null));
                    skip.addAll(search.keySet());
                }
                if(flags.byCat)
                {
                    search.putAll(markets.getByTag  (tag,
                            skip.isEmpty() ? null : skip
                        ));
                    skip.addAll(search.keySet());
                }
                if(flags.byText)
                    search.putAll(markets.getByText (tag,
                            skip.isEmpty() ? null : skip
                        ));

                search.values().stream()
                        .map(e->(BasePost)e)
                        .forEach(e->ret.add(e));
            }            
        } catch   ( Exception e  ) {     e.printStackTrace();
        } finally { System.gc(); }           
        return ret;
    }
}
