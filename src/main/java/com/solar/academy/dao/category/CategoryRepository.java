package com.solar.academy.dao.category;

import com.solar.academy.cache.Cache;
import com.solar.academy.handlers.Category;
import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;

import java.io.IOException;

@Repository
@AllArgsConstructor
public class CategoryRepository {

    final String CATEGORY_TREE_KEY = "category_tree_key";
    final Cache db;

    public Category load(){
        try {
            var json = db.api().getBytes(CATEGORY_TREE_KEY);
            if( json==null ) return null;
            var tree = Category.build();
            tree.fromJSON( new JSONObject(json), Category.class );
            return tree;
        } catch ( Exception e ) {
            e.printStackTrace();
            return null;
        }
    }
    public void save(Category value) throws IOException{
        try{
            var json = value.toJSON().toString();
            db.api().putBytes( CATEGORY_TREE_KEY, json );
        } catch (Exception e) {
            e.printStackTrace();
            if( e instanceof IOException )
                throw (IOException)e;
        }
    }


    // unit test handle
    public static CategoryRepository build(){
        return new CategoryRepository( new Cache() );
    }
}
