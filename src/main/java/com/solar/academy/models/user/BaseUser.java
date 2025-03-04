package com.solar.academy.models.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import com.solar.academy.dao.IRelative;
import com.solar.academy.models.BaseID;
import com.solar.academy.models.messages.Message;

@Getter @Setter
@NoArgsConstructor
public class BaseUser extends BaseID{

    @IRelative.ToList
    List<Seller> asSeller;    

    String name;
    String email;
    String phone;
    
    @IRelative.ToList
    public Message[] messages;

    @IRelative.ToList
    public List<String> photo;
}
