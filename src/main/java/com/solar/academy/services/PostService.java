package com.solar.academy.services;

import com.solar.academy.dto.PromoDTO;
import com.solar.academy.mapping.MessageMapper;
import com.solar.academy.mapping.PostMapper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;


@Service
@Scope ("session")
public class PostService extends SearchService{

    CategoryService tagService;
    PostMapper      postMapper;
    MessageMapper   msgMapper;

    PromoDTO getUserPromoById( String id ) throws Exception{
        var item= repository.getUsers().get(id);
        var dto = postMapper.toFull(item);
        if( item.getAnswers()!=null )
            dto.setAnswers(
                    msgMapper.toListAnswer( item.getAnswers() )
            );

        if( getTmpRelated()==null ){
            search( item.getHeader() ).findUser();
        }   dto.setRelated(
                postMapper.toList( getTmpRelated() )
        );
        return dto;
    }

    PromoDTO getMarketPromoById( String id ) throws Exception{
        var item= repository.getMarkets().get(id);
        var dto = postMapper.toFull(item);
        if( item.getReviews()!=null )
            dto.setReviews(
                    msgMapper.toListReview( item.getReviews() )
            );

        if( getTmpRelated()==null ){
            search( item.getHeader() ).findMarket();
        }   dto.setRelated(
                postMapper.toList( getTmpRelated() )
        );
        return dto;
    }


}


