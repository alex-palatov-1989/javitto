package com.solar.academy.controllers;

import com.solar.academy.dto.CommentRequest;
import com.solar.academy.dto.PromoDTO;
import com.solar.academy.services.PostService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
@Tag(name = "Posts controls subpart ", description = "REST API add, delete, edit posts")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Успех"),
        @ApiResponse(responseCode = "400", description = "Неверно переданные данные"),
        @ApiResponse(responseCode = "500", description = "Ошибка работы сервиса")
})
@Validated
public class PostController {


    PostService service;

    @PostMapping @Operation(summary = "Create new post")
    public ResponseEntity<?> add(
            @Parameter(description =  "Promo DTO")
            @Valid  @RequestBody  PromoDTO req
    ) throws Exception {
        return  service.addMarketPromo(req);
        //return  service.addUserPromo(req);
    }

    @PutMapping @Operation(summary = "Edit post")
    public ResponseEntity<?> edit(
            @Parameter(description =  "Promo DTO")
            @Valid @RequestBody PromoDTO req
    ) throws Exception {
        return  service.editMarketPromo(req);
        //return  service.editUserPromo(req);
    }

    @DeleteMapping @Operation(summary = "Delete post")
    public ResponseEntity<?> delete(
            @Parameter(description =  "Promo DTO")
            @Valid @RequestBody CommentRequest req
    ){
        return  null;//service.delete(req);
    }

}
