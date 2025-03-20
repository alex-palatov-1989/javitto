package com.solar.academy.services;


import com.solar.academy.dao.messages.IMessageRepository;
import com.solar.academy.mapping.MessageMapper;
import com.solar.academy.models.messages.Answer;
import com.solar.academy.models.messages.Message;
import com.solar.academy.models.messages.Review;

import com.solar.academy.dto.messages.MessageBase;
import com.solar.academy.dto.messages.AnswerDTO;
import com.solar.academy.dto.messages.LetterDTO;
import com.solar.academy.dto.messages.ReviewDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Service
@AllArgsConstructor
public class MessagingService {
    IMessageRepository  repository;
    MessageMapper       mapper;
    Optional<Exception> returnError(String advise){ return returnError(new Exception(advise));}
    Optional<Exception> returnError(Exception err){ return Optional.ofNullable(err); }
    Optional<Exception> returnError()             { return Optional.ofNullable(null); }

    /*==============================================================*/
    public
    Optional<Exception> addStringMessage(MessageBase req, StringBuilder id){
        try{
            String key = null;
            if( req instanceof ReviewDTO  ){
                Review post = mapper.toReview((ReviewDTO) req);
                key = repository.getReviews().create(post, post.getPostID());
            }
            if( req instanceof AnswerDTO ){
                Answer post = mapper.toAnswer((AnswerDTO) req);
                key = repository.getAnswers().create(post, post.getPostID());
            }
            if( req instanceof LetterDTO ){
                Message post = mapper.toMessage((LetterDTO) req);
                key = repository.getLetters().create(post, post.getSeller());
            }
            if(Objects.isNull(key))
                return returnError("unrecognized request type or other error");
            else
                id.append( key );

            return returnError();
        } catch (Exception e) { return returnError(e);
        }
    }
    /*---------------------------------------------------------------*/
    public
    Optional<Exception> editStringMessage(MessageBase req){
        try{
            if( req instanceof ReviewDTO){
                Review post = mapper.toReview((ReviewDTO) req);
                repository.getReviews().edit(post, post.getId());
                return returnError();
            }
            if( req instanceof AnswerDTO ){
                Answer post = mapper.toAnswer((AnswerDTO) req);
                repository.getAnswers().edit(post, post.getId());
                return returnError();
            }
            if( req instanceof LetterDTO){
                Message post = mapper.toMessage((LetterDTO) req);
                repository.getLetters().edit(post, post.getSeller());
                return returnError();
            }
            return returnError("unrecognized request type");

        } catch (Exception e) { return returnError(e);
        }
    }
    /*---------------------------------------------------------------*/
    public
    Optional<Exception> deleteStringMessage(MessageBase req){
        try{
            if( req instanceof ReviewDTO){
                Review post = mapper.toReview((ReviewDTO) req);
                repository.getReviews().delete( post.getPostID(), post.getId());
                return returnError();
            }
            if( req instanceof AnswerDTO ){
                Answer post = mapper.toAnswer((AnswerDTO) req);
                repository.getAnswers().delete( post.getPostID(), post.getId());
                return returnError();
            }
            if( req instanceof LetterDTO){ System.err.println("here");
                Message post = mapper.toMessage((LetterDTO) req);
                repository.getLetters().delete( post.getSeller(), post.getId() );
                return returnError();
            }
            return returnError("unrecognized request type");

        } catch (Exception e) { return returnError(e);
        }
    }
    /*==============================================================*/

    public
    Optional<Exception> getReviews(String req, List res){
        try{
            var list = (List<Review>) repository.getReviews().getByHost(req);
            if(list.isEmpty())
                returnError(" cant find reviews for id "+req);
            else
                res.addAll(
                        mapper.toListReview(list)
                );
        } catch (Exception e) { returnError(e); }
        return returnError();
    }
    /*---------------------------------------------------------------*/
    public
    Optional<Exception> getAnswers(String req, List res){
        try{
            var list = (List<Answer>) repository.getReviews().getByHost(req);
            if(list.isEmpty())
                returnError(" cant find answers for id "+req);
            else
                res.addAll(
                        mapper.toListAnswer(list)
                );
        } catch (Exception e) { returnError(e); }
        return returnError();
    }
    /*---------------------------------------------------------------*/
    public
    Optional<Exception> getLetters(String req, List res){
        try{
            var list = (List<Message>)repository.getLetters().getByHost(req);
            if(list.isEmpty())
                returnError(" cant find letters for id "+req);
            else
                res.addAll(
                        mapper.toListLetter(list)
                );
        } catch (Exception e) { returnError(e); }
        return returnError();
    }

    /*==============================================================*/
}
