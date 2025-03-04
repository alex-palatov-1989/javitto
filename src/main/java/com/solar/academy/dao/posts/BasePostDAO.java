package com.solar.academy.dao.posts;

import java.util.HashMap;
import java.util.Set;

import com.solar.academy.dao.BaseDAO;
import com.solar.academy.database.Cache;
import com.solar.academy.database.IQuerySide;
import com.solar.academy.models.posts.BasePost;
import com.solar.academy.models.posts.FullPost;
import com.solar.academy.models.posts.MarketPost;
import com.solar.academy.models.posts.UserPost;

public class BasePostDAO {
    final FilterString     containStr = new FilterString();
    static Cache  db;

    @SuppressWarnings("unchecked")
    class FullPostDAO implements BaseDAO {                 
        @Override
        public Class<?> dataclass() {
            return FullPost.class;
        }
        public HashMap<Integer, ? extends BasePost>  getByTag(String tag, Class clazz, Set<Integer> skip) throws Exception{
            return db.api().filter(   clazz, 
                "category",     tag,    containStr, skip
            );
        }        
        public HashMap<Integer, ? extends BasePost>  getByHeader(String tag, Class clazz, Set<Integer> skip) throws Exception{
            return db.api().filter(   clazz, 
                "header",       tag,    containStr, skip    
            );
        }    
        public HashMap<Integer, ? extends  BasePost>  getByText(String tag, Class clazz, Set<Integer> skip) throws Exception{
            return db.api().filter(   clazz, 
                "description",  tag,    containStr, skip
            );
        }                
    }            

    class FilterString implements IQuerySide.Predicate{
        @Override public
        boolean filter(Object rec, Object  arg){                    
        String val = (String) rec;                    
        return val.contains((String)arg);
        }        
    }

    class UserPostDAO extends FullPostDAO { 
        public void     edit (UserPost post, String key) throws Exception{
            write( key, post, db);    
        }
        public String   create  (UserPost post)          throws Exception{
            return create( post, db );    
        }
        public UserPost  get (String key)   throws Exception{
            return read( key, db );    
        }
        public Class<UserPost> dataclass(){ return UserPost.class; }
    }    
    class MarketPostDAO extends FullPostDAO { 
        public void     edit (MarketPost post, String key) throws Exception{ 
            write( key, post, db);    
        }
        public String    create (MarketPost post)          throws Exception{
            return create( post, db);    
        }
        public UserPost  get (String key)   throws Exception{
            return read( key, db );    
        }
        public Class<MarketPost> dataclass(){ return MarketPost.class; }
    }

}
