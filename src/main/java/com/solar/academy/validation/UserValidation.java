package com.solar.academy.validation;
import com.solar.academy.controllers.UserController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class UserValidation implements Validation {

    public UserValidation(UserController ctrl) {
        checkBinds(ctrl, UserValidation.class);
    }

    public void getUser(String id){
        var n = Integer.parseInt(id);
        if( n<=0 ) throw new UserValidationException("ID must be positive integer");
    }



    public static class UserValidationException extends RuntimeException{
        UserValidationException(String err){ super(err); }
    }
    @ExceptionHandler(UserValidationException.class)
    public ResponseEntity<?> handleValidationError(UserValidationException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
