package com.solar.academy.controllers;


import com.solar.academy.dto.CommentRequest;
import com.solar.academy.services.CommentService;

import com.solar.academy.validation.Validation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
@Tag(name = "Comments controls subpart ", description = "REST API add or edit comments inside post")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Успех"),
        @ApiResponse(responseCode = "404", description = "Неверно переданные данные"),
        @ApiResponse(responseCode = "500", description = "Ошибка работы сервиса")
})
@AllArgsConstructor
@Validated
public class CommentController implements Validation {

    CommentService service;


    @PostMapping @Operation(summary = "Post new comment")
    public ResponseEntity<?> add(
            @Parameter(description =  "CommentRequest")
            @Valid @RequestBody CommentRequest req
    ){
        return  service.add(req);
    }


    @PutMapping @Operation(summary = "Edit comment text")
    public ResponseEntity<?> edit(
            @Parameter(description =  "CommentRequest")
            @Valid @RequestBody CommentRequest req
    ){
        return  service.edit(req);
    }


    @DeleteMapping @Operation(summary = "Delete comment and its children")
    public ResponseEntity<?> delete(
            @Parameter(description =  "CommentRequest")
            @Valid @RequestBody CommentRequest req
    ){
        return  service.delete(req);
    }

}
