package com.solar.academy.dao.messages;

public interface IMessageRepository {
    public IMessageDAO getAnswers();
    public IMessageDAO getReviews();
    public IMessageDAO getLetters();
}
