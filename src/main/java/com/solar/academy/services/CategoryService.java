package com.solar.academy.services;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.solar.academy.dao.category.CategoryRepository;
import com.solar.academy.handlers.Category;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class CategoryService {

    static Category             category;
    static CategoryRepository   repository;
    public static synchronized Category getTag()
    {
        return  category;
    }

    public CategoryService(){
        try{
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
        }
    }
    public static String find( String tag )
    {
        synchronized (category){
            return  getTag().findById(tag).tag();
        }
    }
    public static String path( String tag )
    {
        synchronized (category){
            return  getTag().getPath(tag);
        }
    }

    /* callable from AdminCategoryService */
    protected static void append( String host, String child )
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
    protected static void remove( String tag )
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
    public static CategoryService build(){
        repository = CategoryRepository.build();
        return new CategoryService();
    }

    public static void main(String args[]) throws JsonProcessingException {
        var service = CategoryService.build();
        System.err.println(
                service.getTag().toJSON().toString(5)
        );
    }
}


