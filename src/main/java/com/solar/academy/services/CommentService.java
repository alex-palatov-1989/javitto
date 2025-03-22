package com.solar.academy.services;

import com.solar.academy.dao.posts.IPostRepository;
import com.solar.academy.dto.CommentRequest;
import com.solar.academy.handlers.Comment;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import java.util.NoSuchElementException;

//@PreAuthorize( "hasRole('USER')" )
@Service @NoArgsConstructor
public class CommentService {

    public ResponseEntity<?> add(CommentRequest req) {
        return  modifyComment(req, addFunc);
    }
    public ResponseEntity<?> edit(CommentRequest req) {
        return  modifyComment(req, editFunc);
    }
    public ResponseEntity<?> delete(CommentRequest req) {
        return  modifyComment(req, delFunc);
    }

    private IPostRepository   repository;
    private ResponseEntity<?> modifyComment(CommentRequest req, Operation op) {
        try{
            var users = repository.getUsers().get(req.getPostID());
            if (users.isPresent()) {
                var res = op.run( req, users.get().getRootComment() );
                repository.getUsers().edit(users.get(), users.get().getId());
                return res;
            }
            var market = repository.getMarkets().get(req.getPostID());
            if (market.isPresent()) {
                var res = op.run( req, market.get().getRootComment() );
                repository.getUsers().edit(users.get(), users.get().getId());
                return res;
            }
        } catch (NoSuchElementException e) {
            return  ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return  ResponseEntity.internalServerError().build();
        }   return  ResponseEntity.badRequest().build();
    }
    @FunctionalInterface  interface Operation{
        ResponseEntity<?> run( CommentRequest req, Comment item)
        throws NoSuchElementException;
    }

    Operation addFunc = (req, item) -> {
        item.findById( req.getKey() )
            .addChild(
                req.getSender(),
                req.getBody()
        );
        return ResponseEntity.ok().build();
    };
    Operation editFunc = (req, item) -> {
        item.findById( req.getKey() ).setText( req.getBody() );
        return ResponseEntity.ok().build();
    };
    Operation delFunc = (req, item) -> {
        item.deleteById(req.getKey());
        return ResponseEntity.ok().build();
    };
}




