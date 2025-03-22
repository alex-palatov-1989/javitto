package com.solar.academy.models.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

import com.solar.academy.dao.IRelative;
import com.solar.academy.models.BaseID;
import com.solar.academy.models.messages.Message;
import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.GrantedAuthority;

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


    @AllArgsConstructor
    class Role implements GrantedAuthority {
        enum authority {
            CUSTOMER    ("customer"),
            SELLER      ("seller"),
            MODERATOR   ("admin");
            GrantedAuthority  authority;
            authority(String val)
            {
                authority = new GrantedAuthority() {
                    @Override
                    public String getAuthority() {
                        return val;
                    }
                };
            }
        }
        GrantedAuthority role;
        @Override
        public String   getAuthority() {
            return role.getAuthority();
        }
    }
    Role    role;
    String  password;
}
