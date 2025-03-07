package com.solar.academy.validation;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public interface Validation {

    default void checkBinds(Object sub, Class sup)
    {   var decorErr = this.getClass().getSimpleName()
            + ": can't find validator for -> \n\t";
        var cEnds = Arrays.asList(sub.getClass().getDeclaredMethods());
        var vEnds = Arrays.stream(sup.getDeclaredMethods())
                .map(Method::getName).toList();
        cEnds.stream().filter(
                mtd-> !vEnds.contains(mtd.getName())
        ).map( Method::getName ).forEach(
                name->  System.out.println(decorErr + name)
        );
    }

    @ExceptionHandler(RuntimeException.class)
    default ResponseEntity<?> defaultHandle(Exception exc) {
        if (exc instanceof RuntimeException) {
            var e = (RuntimeException) exc;
            String errors = this.getClass().getSimpleName()+" caught Exception!\n>_ ";
            errors += List.of(e.getStackTrace()).stream()
                    .map(line-> ((StackTraceElement) line).toString() )
                    .collect(Collectors.joining("\n"));
            return ResponseEntity.internalServerError().body(errors);
        }
        return ResponseEntity.internalServerError().body("Unclassified error");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    default ResponseEntity<?> handleValidationError(Exception e){
        if (e instanceof MethodArgumentNotValidException) {
            var exc = (MethodArgumentNotValidException)e;
            var errors = new StringBuilder();
            exc.getBindingResult().getAllErrors().forEach((err) -> {
                errors.append(
                        "\n"+((FieldError) err).getField()
                                +" -> "+err.getDefaultMessage()
                );
            });
            return ResponseEntity.badRequest().body(errors.toString());
        }
        return defaultHandle(e);
    }
}
