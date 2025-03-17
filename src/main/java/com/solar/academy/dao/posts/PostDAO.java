package com.solar.academy.dao.posts;

import java.util.HashMap;
import java.util.Optional;
import java.util.Set;

import com.solar.academy.dao.AbstractDAO;
import com.solar.academy.cache.Cache;
import com.solar.academy.cache.IQuerySide;
import com.solar.academy.models.posts.BasePost;
import com.solar.academy.models.posts.MarketPost;
import com.solar.academy.models.posts.UserPost;

@SuppressWarnings ("unchecked")
abstract public class PostDAO {

    abstract Cache db();
    IQuerySide.Predicate containStr = new IQuerySide.Predicate() {
        @Override public boolean filter(Object rec, Object  arg){
            return ((String) rec).contains((String)arg);
        }
    };

    final class UserPostDAO extends FullPostDAO<UserPost>
    implements IPostDAO<UserPost>{
        public void     edit    (UserPost post, String key) throws Exception {
            synchronized (mtx){
                write( key, post, db() );
            }
        }
        public String   create  (UserPost post)             throws Exception{
            return create( post, db() );
        }
        public Optional<UserPost> get    (String key) {
            return Optional.ofNullable(read( key, db() ));
        }
    }
    final class MarketPostDAO extends  FullPostDAO<MarketPost>
    implements  IPostDAO<MarketPost>{
        public void     edit    (MarketPost post, String key) throws Exception{
            synchronized (mtx){
                write( key, post, db());
            }
        }
        public String   create (MarketPost post)              throws Exception{
            return create( post, db());
        }
        public Optional<MarketPost> get  (String key) {
            return Optional.ofNullable(read( key, db() ));
        }
    }

    abstract class FullPostDAO<T> implements AbstractDAO<T> {
        public HashMap<Integer, BasePost>  getByTag(String tag, Set<Integer> skip)      throws Exception{
            return (HashMap<Integer, BasePost>) db().api().filter(
                    dataclass(), "category",
                    tag, containStr, skip
            );}
        public HashMap<Integer, BasePost>  getByHeader(String tag, Set<Integer> skip)   throws Exception{
            return (HashMap<Integer, BasePost>) db().api().filter(
                    dataclass(), "header",
                    tag, containStr, skip
            );}
        public HashMap<Integer, BasePost>  getByText(String tag, Set<Integer> skip)     throws Exception{
            return (HashMap<Integer, BasePost>) db().api().filter(
                    dataclass(), "description",
                    tag, containStr, skip
            );}
        Object mtx = new Object();
    }
}
