package com.solar.academy.dao.messages;

import com.solar.academy.database.Cache;
import com.solar.academy.models.messages.Answer;
import com.solar.academy.models.messages.Message;
import com.solar.academy.models.messages.Review;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public final class MessageRepository implements IMessageRepository {
    final static class AnswerDAO extends MessageDAO implements IMessageDAO {
        @Override   Cache db() { return getDB(); }
        @Override   public Class<?> dataclass() {   return Answer.class;
        }
    }
    final static class ReviewDAO extends MessageDAO implements IMessageDAO{
        @Override   Cache db() { return getDB(); }
        @Override   public Class<?> dataclass() {   return Review.class;
        }
    }
    final static class LetterDAO extends MessageDAO implements IMessageDAO{
        @Override   Cache db() { return getDB(); }
        @Override   public Class<?> dataclass() {   return Message.class;
        }
    }
    @Getter final AnswerDAO     answers   = new AnswerDAO();
    @Getter final ReviewDAO     reviews   = new ReviewDAO();
    @Getter final LetterDAO     letters   = new LetterDAO();

    @Autowired   static Cache _db;
    synchronized static Cache getDB(){
        return  _db;
    };
}
