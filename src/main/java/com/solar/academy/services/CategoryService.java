package com.solar.academy.services;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.solar.academy.dao.category.CategoryRepository;
import com.solar.academy.handlers.Category;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
//@NoArgsConstructor
public class CategoryService {

    Category             category;
    CategoryRepository   repository;
    public synchronized Category getTag()
    {
        return  category;
    }
    public CategoryService(CategoryRepository db){
        try{
            this.repository = db;
            category = repository.load();
            if( category == null )
            {
                category = Category.build();
                category.addChild("GOODS")
                        .addChild("JOBS")
                        .addChild("ROOMS");

                category.findById("GOODS")
                        .addChild("BUY")
                        .addChild("SELL");

                category.findById("JOBS")
                        .addChild("EMPLOY")
                        .addChild("VACANCY");

                category.findById("ROOMS")
                        .addChild("PROPOSAL")
                        .addChild("DEMAND");

                System.err.println("\n>_ error loading category tree");
                repository.save(category);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.err.println(
                    category.toJSON().toString(5)
            );
        }
    }
    public  String find( String tag )
    {
        synchronized (category){
            return  getTag().findById(tag).id;
        }
    }
    public  String path( String tag )
    {
        synchronized (category){
            return  getTag().getPath(tag);
        }
    }

    /* callable from AdminCategoryService */
    protected  void append( String host, String child )
    {
        try{
            repository.save(
                    getTag().findById(host).addChild(child)
            );
        } catch (NullPointerException e) {
            System.err.println("\n>_ cant find category tag: " +host+'\n'+e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    protected  void remove( String tag )
    {
        synchronized (category){
            getTag().deleteByid(tag);
        }
        try{
            repository.save(
                    getTag()
            );
        } catch( IOException e ){
            e.printStackTrace();
        }
    }

    // unit test handle
    public  CategoryService build(){
        repository = CategoryRepository.build();
        return new CategoryService(repository);
    }
}


