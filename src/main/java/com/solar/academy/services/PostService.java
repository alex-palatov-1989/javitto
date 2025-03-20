package com.solar.academy.services;

import com.solar.academy.dto.CommentRequest;
import com.solar.academy.dto.PromoDTO;
import com.solar.academy.handlers.Comment;
import com.solar.academy.mapping.MessageMapper;
import com.solar.academy.mapping.PostMapper;
import com.solar.academy.models.posts.FullPost;
import com.solar.academy.models.posts.MarketPost;
import com.solar.academy.models.posts.UserPost;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;


@Service
@Scope ("session")
public class PostService extends SearchService{

    CategoryService tagService;
    PostMapper      postMapper;
    MessageMapper   msgMapper;

    public
    PromoDTO getUserPromoById( String id ) {
        var item= repository.getUsers().get(id).orElseThrow();
        var dto = postMapper.toFull(item);
        if( item.getAnswers()!=null )
            dto.setAnswers(
                    msgMapper.toListAnswer( item.getAnswers() )
            );
        createRelated( dto, item );
        return dto;
    }
    public
    PromoDTO getMarketPromoById( String id ) {
        var item= repository.getMarkets().get(id).orElseThrow();
        var dto = postMapper.toFull(item);
        if( item.getReviews()!=null )
            dto.setReviews(
                    msgMapper.toListReview( item.getReviews() )
            );
        createRelated( dto, item );
        return dto;
    }
    private
    void createRelated( PromoDTO dto, FullPost item ){
        if( getTmpRelated()==null ){
            search( item.getHeader() ).findMarket();
        }   dto.setRelated(
                postMapper.toList( getTmpRelated() )
        );
    }

    public
    ResponseEntity<String> addMarketPromo( PromoDTO post ) throws Exception{
        return ResponseEntity.ofNullable(
                createNewPost( post, MarketPost.class )
        );
    }
    public
    ResponseEntity<String> addUserPromo( PromoDTO post ) throws Exception{
        return ResponseEntity.ofNullable(
                createNewPost( post, UserPost.class )
        );
    }
    private
    <T extends FullPost> String createNewPost(PromoDTO req, Class<T> clazz) throws Exception {
        T item = (T)postMapper.toTemplatePost( req );
        item.setRootComment( new Comment() );
        if ( item instanceof MarketPost )
            return repository.getMarkets()  .create((MarketPost) item);
        if ( item instanceof UserPost )
            return repository.getUsers()    .create((UserPost) item);
        return null;
    }

    public
    ResponseEntity<?> editMarketPromo( PromoDTO post ) throws Exception{
        return editPost( post, MarketPost.class );
    }
    public
    ResponseEntity<?> editUserPromo( PromoDTO post ) throws Exception{
        return editPost( post, UserPost.class );
    }
    private
    <T extends FullPost> ResponseEntity<?> editPost(PromoDTO req, Class<T> clazz) throws Exception {
        T item = (T)postMapper.toTemplatePost( req );
        if ( item instanceof MarketPost )
            repository.getMarkets()  .edit((MarketPost) item, item.getId() );
        if ( item instanceof UserPost )
            repository.getUsers()    .edit((UserPost) item, item.getId() );
        return ResponseEntity.ok().build();
    }
}


