package com.solar.academy.dao.posts;

import java.util.*;

import com.solar.academy.cache.Cache;
import com.solar.academy.models.posts.BasePost;
import com.solar.academy.models.posts.MarketPost;
import com.solar.academy.models.posts.UserPost;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository @AllArgsConstructor
final public class PostRepository implements IPostRepository{
    @PostConstruct void print(){
        System.err.println( this+String.format(
        "\n>_repository\t= %s\n>_repository\t= %s\n", users, markets)
    );}
    @Getter PostDAO.UserPostDAO     users;
    @Getter PostDAO.MarketPostDAO   markets;


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
                    search.putAll(getUsers().getByHeader (tag, null));
                    skip.addAll(search.keySet());
                }
                if(flags.byCat)
                {
                    search.putAll(getUsers().getByTag    (tag,
                            skip.isEmpty() ? null : skip
                        ));
                    skip.addAll(search.keySet());
                }
                if(flags.byText)
                    search.putAll(getUsers().getByText   (tag,
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
                    search.putAll(getMarkets().getByHeader  (tag, null));
                    skip.addAll(search.keySet());
                }
                if(flags.byCat)
                {
                    search.putAll(getMarkets().getByTag  (tag,
                            skip.isEmpty() ? null : skip
                        ));
                    skip.addAll(search.keySet());
                }
                if(flags.byText)
                    search.putAll(getMarkets().getByText (tag,
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
