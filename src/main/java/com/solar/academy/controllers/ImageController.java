package com.solar.academy.controllers;


import com.solar.academy.services.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/img")
@Tag(name = "Image hosting subpart ", description = "API load and save photos")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Успех"),
        @ApiResponse(responseCode = "404", description = "Неверно переданные данные"),
        @ApiResponse(responseCode = "500", description = "Ошибка работы сервиса")
})
@AllArgsConstructor
public class ImageController {

    ImageService service;

    @GetMapping("/{id}") @Operation(summary = "Search photo in DB by ID")
    ResponseEntity<?> getImage(
            @PathVariable String id
    ){
        return service.get(id);
    }

    @Operation(summary = "Upload photo in DB")
    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    ResponseEntity<?> addImage(
            @RequestParam("file") MultipartFile photo
    ){
        return service.post(photo);
    }


    @Operation(summary = "Upload list of photos")
    @PostMapping(
            path = "/list",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    ResponseEntity<?> addList(
            @RequestBody MultipartFile[] photos
    ){
        return service.post(List.of(photos));
    }
}
