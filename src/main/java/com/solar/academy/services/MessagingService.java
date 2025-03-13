package com.solar.academy.services;


import com.solar.academy.dao.messages.IMessageRepository;
import com.solar.academy.mapping.MessageMapper;
import com.solar.academy.models.messages.Answer;
import com.solar.academy.models.messages.Message;
import com.solar.academy.models.messages.Review;
import com.solar.academy.request.messages.AnswerRequest;
import com.solar.academy.request.messages.IMessageRequest;
import com.solar.academy.request.messages.LetterRequest;
import com.solar.academy.request.messages.ReviewRequest;
import org.springframework.stereotype.Service;

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

    <T extends IMessageRequest> Optional<Exception> addStringMessage(T req){
        try{

            if( req instanceof ReviewRequest ){
                Review post = mapper.toReview((ReviewRequest) req);
                repository.getReviews().create(post, post.getPostID());
                return returnError();
            }
            if( req instanceof AnswerRequest ){
                Answer post = mapper.toAnswer((AnswerRequest) req);
                repository.getAnswers().create(post, post.getPostID());
                return returnError();
            }
            if( req instanceof LetterRequest ){
                Message post = mapper.toMessage((LetterRequest) req);
                repository.getLetters().create(post, post.getSeller());
                return returnError();
            }
            return returnError("unrecognized request type");

        } catch (Exception e) { return returnError(e);
        }
    }
    /*---------------------------------------------------------------*/
    <T extends IMessageRequest> Optional<Exception> editStringMessage(T req){
        try{

            if( req instanceof ReviewRequest ){
                Review post = mapper.toReview((ReviewRequest) req);
                repository.getReviews().edit(post, post.getPostID());
                return returnError();
            }
            if( req instanceof AnswerRequest ){
                Answer post = mapper.toAnswer((AnswerRequest) req);
                repository.getAnswers().edit(post, post.getPostID());
                return returnError();
            }
            if( req instanceof LetterRequest ){
                Message post = mapper.toMessage((LetterRequest) req);
                repository.getLetters().edit(post, post.getSeller());
                return returnError();
            }
            return returnError("unrecognized request type");

        } catch (Exception e) { return returnError(e);
        }
    }
    /*---------------------------------------------------------------*/
    <T extends IMessageRequest> Optional<Exception> deleteStringMessage(T req){
        try{

            if( req instanceof ReviewRequest ){
                Review post = mapper.toReview((ReviewRequest) req);
                repository.getReviews().delete( post.getPostID(), post.getKey());
                return returnError();
            }
            if( req instanceof AnswerRequest ){
                Answer post = mapper.toAnswer((AnswerRequest) req);
                repository.getAnswers().delete( post.getPostID(), post.getKey());
                return returnError();
            }
            if( req instanceof LetterRequest ){
                Message post = mapper.toMessage((LetterRequest) req);
                repository.getLetters().delete( post.getSeller(), post.getKey() );
                return returnError();
            }
            return returnError("unrecognized request type");

        } catch (Exception e) { return returnError(e);
        }
    }
    /*==============================================================*/

    Optional<Exception> getReview(String req, List res){
        Exception err = null;
        try{
                List<?> list = repository.getReviews().getByHost(req);
                if(list.isEmpty())
                    returnError(" cant find reviews for id "+req);
                else
                    res.addAll(list);

        } catch (Exception e) { err = e;
        } finally {
            return returnError(err);
        }
    }
    /*---------------------------------------------------------------*/
    Optional<Exception> getAnswer(String req, List res){
        Exception err = null;
        try{
                List<?> list = repository.getReviews().getByHost(req);
            if(list.isEmpty())
                returnError(" cant find answers for id "+req);
            else
                res.addAll(list);

        } catch (Exception e) { err = e;
        } finally {
            return returnError(err);
        }
    }
    /*---------------------------------------------------------------*/
    Optional<Exception> getLetter(String req, List res){
        Exception err = null;
        try{
                List<?> list = repository.getLetters().getByHost(req);
            if(list.isEmpty())
                returnError(" cant find letters for id "+req);
            else
                res.addAll(list);

        } catch (Exception e) { err = e;
        } finally {
            return returnError(err);
        }
    }

    /*==============================================================*/


}
