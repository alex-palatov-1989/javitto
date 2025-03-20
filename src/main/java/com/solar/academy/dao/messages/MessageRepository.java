package com.solar.academy.dao.messages;

import com.solar.academy.cache.Cache;
import com.solar.academy.models.messages.Answer;
import com.solar.academy.models.messages.Message;
import com.solar.academy.models.messages.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;


@Repository @NoArgsConstructor
public final class MessageRepository implements IMessageRepository {

    final class AnswerDAO extends MessageDAO implements IMessageDAO {
        @Override   Cache db() { return getDB(); }
        @Override   public Class<?> dataclass() {
            return Answer.class;
        }
    }

    final class ReviewDAO extends MessageDAO implements IMessageDAO{
        @Override   Cache db() { return getDB(); }
        @Override   public Class<?> dataclass() {
            return Review.class;
        }
    }

    final class LetterDAO extends MessageDAO implements IMessageDAO{
        @Override   Cache db() { return getDB(); }
        @Override   public Class<?> dataclass() {
            return Message.class;
        }
    }
    @Getter  AnswerDAO     answers = new AnswerDAO();
    @Getter  ReviewDAO     reviews = new ReviewDAO();
    @Getter  LetterDAO     letters = new LetterDAO();

    @Autowired   Cache _db;
    synchronized Cache getDB(){
        return  _db;
    };
}
