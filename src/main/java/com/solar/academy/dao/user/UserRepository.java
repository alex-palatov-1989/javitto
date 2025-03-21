package com.solar.academy.dao.user;

import com.solar.academy.models.user.BaseUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    @Autowired SellerDAO    sellers;
    @Autowired BaseUserDAO  users;

    public BaseUser getById(String id){
        return null;
    }
}
