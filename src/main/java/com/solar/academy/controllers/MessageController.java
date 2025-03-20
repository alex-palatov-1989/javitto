package com.solar.academy.controllers;

import com.solar.academy.dto.messages.MessageBase;
import com.solar.academy.dto.messages.AnswerDTO;
import com.solar.academy.dto.messages.LetterDTO;
import com.solar.academy.dto.messages.ReviewDTO;
import com.solar.academy.services.MessagingService;
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
@Tag(name = "Messages REST subpart ", description = "API reviews, messages, answers")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Успех"),
        @ApiResponse(responseCode = "400", description = "Неверно переданные данные"),
        @ApiResponse(responseCode = "500", description = "Ошибка работы сервиса")
})
@AllArgsConstructor
@Validated
public class MessageController implements Validation {

    MessagingService service;
    private  ResponseEntity<?> add(MessageBase req){
        StringBuilder id = new StringBuilder();
        var res = service.addStringMessage(req, id);
        return res.isPresent() ?
                ResponseEntity.badRequest().body( res.get().getMessage() )
                : ResponseEntity.ok().body( id.toString() );
    }
    private  ResponseEntity<?> edit(MessageBase req){
        var res = service.editStringMessage(req);
        return res.isPresent() ?
                ResponseEntity.badRequest().body(res.get().getMessage())
                : ResponseEntity.ok().build();
    }
    private  ResponseEntity<?> del(MessageBase req){
        var res = service.deleteStringMessage(req);
        return res.isPresent() ?
                ResponseEntity.badRequest().body(res.get().getMessage())
                : ResponseEntity.ok().build();
    }

    @PostMapping("/letter") @Operation(summary = "Create new letter")
    public ResponseEntity<?> post(
            @Parameter(description =  "LetterDTO")
            @Valid @RequestBody LetterDTO req
    ){
        return add(req);
    }
    @PutMapping("/letter") @Operation(summary = "Edit existing letter")
    public ResponseEntity<?> put(
            @Parameter(description =  "LetterDTO")
            @Valid @RequestBody LetterDTO req
    ){
        return edit(req);
    }
    @DeleteMapping("/letter") @Operation(summary = "Delete existing letter")
    public ResponseEntity<?> delete(
            @Parameter(description =  "LetterDTO")
            @Valid @RequestBody LetterDTO req ){
        return del(req);
    }

    @PostMapping("/answer") @Operation(summary = "Create new answer")
    public ResponseEntity<?> post(
            @Parameter(description =  "AnswerDTO")
            @Valid @RequestBody AnswerDTO req ){
        return add(req);
    }
    @PutMapping("/answer") @Operation(summary = "Edit existing answer")
    public ResponseEntity<?> put(
            @Parameter(description =  "AnswerDTO")
            @Valid @RequestBody AnswerDTO req ){
        return edit(req);
    }
    @DeleteMapping("/answer") @Operation(summary = "Delete existing answer")
    public ResponseEntity<?> delete(
            @Parameter(description =  "AnswerDTO")
            @Valid @RequestBody AnswerDTO req ){
        return del(req);
    }

    @PostMapping("/review") @Operation(summary = "Create new review")
    public ResponseEntity<?> post(
            @Valid @RequestBody ReviewDTO req ){
        return add(req);
    }
    @PutMapping("/review") @Operation(summary = "Edit existing review")
    public ResponseEntity<?> put(
            @Valid @RequestBody ReviewDTO req ){
        return edit(req);
    }
    @DeleteMapping("/review") @Operation(summary = "Delete existing review")
    public ResponseEntity<?> delete(
            @Valid @RequestBody ReviewDTO req ){
        return del(req);
    }
}
