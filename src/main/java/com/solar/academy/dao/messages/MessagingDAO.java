package com.solar.academy.dao.messages;

import com.solar.academy.models.messages.Answer;
import com.solar.academy.models.messages.Message;
import com.solar.academy.models.messages.Review;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class MessagingDAO  {
    public class AnswerDAO extends StringMsgDAO {

        @Override   public Class<?> dataclass() {   return Answer.class;
        }
    }

    public class ReviewDAO extends StringMsgDAO {

        @Override   public Class<?> dataclass() {   return Review.class;
        }
    }

    public class MessageDAO extends StringMsgDAO {

        @Override   public Class<?> dataclass() {   return Message.class;
        }
    }        

    final AnswerDAO     answerDAO       = new AnswerDAO();
    final ReviewDAO     reviewDAO       = new ReviewDAO();    
    final MessageDAO    messageDAO      = new MessageDAO();

}
