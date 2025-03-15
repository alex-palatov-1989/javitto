package com.solar.academy.dao.posts;

import java.util.HashMap;
import java.util.Set;

import com.solar.academy.dao.AbstractDAO;
import com.solar.academy.cache.Cache;
import com.solar.academy.cache.IQuerySide;
import com.solar.academy.models.posts.BasePost;
import com.solar.academy.models.posts.MarketPost;
import com.solar.academy.models.posts.UserPost;

abstract public class PostDAO {
    abstract Cache db();

    @SuppressWarnings("unchecked")
    abstract class FullPostDAO implements AbstractDAO {
        public HashMap<Integer, ? extends BasePost>  getByTag(String tag, Class clazz, Set<Integer> skip) throws Exception{
            return db().api().filter(   clazz, 
                "category",     tag,    containStr, skip
            );
        }        
        public HashMap<Integer, ? extends BasePost>  getByHeader(String tag, Class clazz, Set<Integer> skip) throws Exception{
            return db().api().filter(   clazz, 
                "header",       tag,    containStr, skip    
            );
        }    
        public HashMap<Integer, ? extends  BasePost>  getByText(String tag, Class clazz, Set<Integer> skip) throws Exception{
            return db().api().filter(   clazz, 
                "description",  tag,    containStr, skip
            );
        }                
    }            

    final class UserPostDAO extends FullPostDAO implements IPostDAO<UserPost>{
        public void     edit (UserPost post, String key)    throws Exception {
            write( key, post, db() );
        }
        public String   create  (UserPost post)             throws Exception{
            return create( post, db() );    
        }
        public UserPost  get (String key)                   throws Exception{
            return read( key, db() );    
        }
        @Override
        public Class<UserPost> dataclass(){ return UserPost.class; }
    }
    final class MarketPostDAO extends FullPostDAO implements  IPostDAO<MarketPost>{
        public void     edit (MarketPost post, String key) throws Exception{ 
            write( key, post, db());    
        }
        public String    create (MarketPost post)          throws Exception{
            return create( post, db());    
        }
        public MarketPost  get (String key)                throws Exception{
            return read( key, db() );    
        }
        @Override
        public Class<MarketPost> dataclass(){ return MarketPost.class; }
    }

    final class FilterString implements IQuerySide.Predicate{
        @Override public
        boolean filter(Object rec, Object  arg){
            String val = (String) rec;
            return val.contains((String)arg);
        }
    }
    final FilterString     containStr = new FilterString();
}
