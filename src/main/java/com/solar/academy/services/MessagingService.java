package com.solar.academy.services;


import com.solar.academy.dao.messages.IMessageRepository;
import com.solar.academy.mapping.MessageMapper;
import com.solar.academy.models.messages.Answer;
import com.solar.academy.models.messages.Message;
import com.solar.academy.models.messages.Review;

import com.solar.academy.dto.RequestBase;
import com.solar.academy.dto.messages.AnswerDTO;
import com.solar.academy.dto.messages.LetterDTO;
import com.solar.academy.dto.messages.ReviewDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MessagingService {
    IMessageRepository  repository;
    MessageMapper       mapper;
    Optional<Exception> returnError(String advise){ return returnError(new Exception(advise));}
    Optional<Exception> returnError(Exception err){ return Optional.ofNullable(err); }
    Optional<Exception> returnError()             { return Optional.ofNullable(null); }

    /*==============================================================*/
    public
    Optional<Exception> addStringMessage(RequestBase req){
        try{
            req.setDatetime( LocalDateTime.now() );
            if( req instanceof ReviewDTO){
                Review post = mapper.toReview((ReviewDTO) req);
                repository.getReviews().create(post, post.getPostID());
                return returnError();
            }
            if( req instanceof AnswerDTO ){
                Answer post = mapper.toAnswer((AnswerDTO) req);
                repository.getAnswers().create(post, post.getPostID());
                return returnError();
            }
            if( req instanceof LetterDTO){
                Message post = mapper.toMessage((LetterDTO) req);
                repository.getLetters().create(post, post.getSeller());
                return returnError();
            }
            return returnError("unrecognized request type");

        } catch (Exception e) { return returnError(e);
        }
    }
    /*---------------------------------------------------------------*/
    public
    Optional<Exception> editStringMessage(RequestBase req){
        try{
            if( req instanceof ReviewDTO){
                Review post = mapper.toReview((ReviewDTO) req);
                repository.getReviews().edit(post, post.getPostID());
                return returnError();
            }
            if( req instanceof AnswerDTO ){
                Answer post = mapper.toAnswer((AnswerDTO) req);
                repository.getAnswers().edit(post, post.getPostID());
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
    Optional<Exception> deleteStringMessage(RequestBase req){
        try{
            if( req instanceof ReviewDTO){
                Review post = mapper.toReview((ReviewDTO) req);
                repository.getReviews().delete( post.getPostID(), post.getKey());
                return returnError();
            }
            if( req instanceof AnswerDTO ){
                Answer post = mapper.toAnswer((AnswerDTO) req);
                repository.getAnswers().delete( post.getPostID(), post.getKey());
                return returnError();
            }
            if( req instanceof LetterDTO){
                Message post = mapper.toMessage((LetterDTO) req);
                repository.getLetters().delete( post.getSeller(), post.getKey() );
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
