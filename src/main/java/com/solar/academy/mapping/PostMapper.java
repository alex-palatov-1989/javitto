package com.solar.academy.mapping;


import com.solar.academy.dto.PromoDTO;
import com.solar.academy.models.posts.BasePost;
import com.solar.academy.models.posts.FullPost;

import com.solar.academy.models.posts.MarketPost;
import com.solar.academy.models.posts.UserPost;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope("prototype")
@Mapper( componentModel = "spring" )
public interface PostMapper {

    /*=======================================================*/
    @Mapping(target = "datetime",   ignore = true)
    @Mapping(target = "mainImg",    source = "mainImg" )
    @Mapping(target = "authorID",   source = "authorID")
    @Mapping(target = "header",     source = "header"  )
    @Mapping(target = "category",   source = "category")
    @Mapping(target = "price",      source = "price")
    @Mapping(target = "id",         source = "id"   )
    @Mapping(target = "host",       source = "host" )
    /*=======================================================*/

                                        // listed preview
    List<PromoDTO> toList(List<BasePost>   req);

    @Mapping(target = "photos",         source = "photos"       )
    @Mapping(target = "description",    source = "description"  )
    @Mapping(target = "rootComment",    source = "rootComment"  )

    PromoDTO toFull             (FullPost req);       // page of opened pos
    FullPost toTemplatePost     (PromoDTO req);       // POST req template

    /*=======================================================*/
}