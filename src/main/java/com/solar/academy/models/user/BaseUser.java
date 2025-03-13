package com.solar.academy.models.user;

import lombok.Data;
import java.util.List;

import com.solar.academy.dao.IRelative;
import com.solar.academy.models.BaseID;
import com.solar.academy.models.messages.Message;

@Data
public class BaseUser extends BaseID{

    @IRelative.ToList
    List<Seller> asSeller;    

    String name;
    String email;
    String phone;
    
    @IRelative.ToList
    public List<Message> messages;

    @IRelative.ToList
    public List<String> photo;
}
