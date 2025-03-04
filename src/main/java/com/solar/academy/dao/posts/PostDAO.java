package com.solar.academy.dao.posts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import com.solar.academy.models.posts.BasePost;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PostDAO extends BasePostDAO implements IPostDAO{
        
    @Getter final UserPostDAO      userDAO    = new UserPostDAO();
    @Getter final MarketPostDAO    goodDAO    = new MarketPostDAO();    

    
    @NoArgsConstructor
    class SearchFlags{
        public boolean  byCat, byText, byHead = true;
        public boolean  inUsers   = true;
        public boolean  onMarket  = true; 
    }
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
                if(flags.byHead)    search.putAll(userDAO.getByHeader (tag, userDAO.dataclass(), null));    
                skip.addAll(search.keySet());

                if(flags.byCat)     search.putAll(userDAO.getByTag     (tag, userDAO.dataclass(), skip));
                skip.addAll(search.keySet());

                if(flags.byText)    search.putAll(userDAO.getByText    (tag, userDAO.dataclass(), skip));
                skip.clear();

            ret.addAll(search.values());
            }
            if(flags.onMarket){     
                if(flags.byHead)    search.putAll(goodDAO.getByHeader  (tag, goodDAO.dataclass(), null));
                skip.addAll(search.keySet());

                if(flags.byCat)    search.putAll(goodDAO.getByTag      (tag, goodDAO.dataclass(), skip));
                skip.addAll(search.keySet());
                
                if(flags.byText)   search.putAll(goodDAO.getByText     (tag, goodDAO.dataclass(), skip));
                skip.clear();

            ret.addAll(search.values());
            }            
        } catch   ( Exception e  ) {     e.printStackTrace();
        } finally { System.gc(); }           
        return ret;
    }      

}
