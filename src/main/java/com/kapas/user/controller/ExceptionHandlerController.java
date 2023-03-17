package com.kapas.user.controller;

import com.kapas.user.model.ApiResponseError;
import org.hibernate.StaleStateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Objects;
import java.util.Set;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseError<String>> invalidAuth(MethodArgumentNotValidException e) {
        FieldError error = e.getBindingResult().getFieldError();
        if(Objects.nonNull(error)) {
            return new ResponseEntity<>(new ApiResponseError<>(error.getDefaultMessage()),
                    HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(new ApiResponseError<>("Method Argument Not Valid"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponseError<String>> invalidAuth(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        StringBuilder violationMessage = new StringBuilder("Invalid Parameters.");
        if (!constraintViolations.isEmpty()) {
            violationMessage = new StringBuilder("Following Constraints failed : ");
            for (ConstraintViolation<?> constraintViolation : constraintViolations) {
                violationMessage.append(constraintViolation.getMessage()).append(" ");
            }
        }
        return new ResponseEntity<>(new ApiResponseError<>(violationMessage.toString()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseError<String>> unHandledExceptions(Exception e) {
        if (e instanceof ObjectOptimisticLockingFailureException || e instanceof StaleStateException) {
            return new ResponseEntity<>(new ApiResponseError<>("Multiple requests invoked at same time, Please Try " +
                    "Again."), HttpStatus.CONFLICT);
        }
        if (e.getMessage() != null && e.getMessage().length() > 0) {
            return new ResponseEntity<>(new ApiResponseError<>(e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(new ApiResponseError<>("Something went wrong, please try again later"), HttpStatus.GONE);
        }
    }
}
