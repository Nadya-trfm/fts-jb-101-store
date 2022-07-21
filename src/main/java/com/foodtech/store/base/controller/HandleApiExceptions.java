package com.foodtech.store.base.controller;

import com.foodtech.store.base.api.response.ErrorResponse;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

public class HandleApiExceptions {
    private ResponseEntity<Object> buildResponseEntity(ErrorResponse errorResponse){

        return new ResponseEntity<>(errorResponse, errorResponse.getHttpStatus());
    }

    @ExceptionHandler(ChangeSetPersister.NotFoundException.class)
    public ResponseEntity<Object> notFoundException(ChangeSetPersister.NotFoundException ex, WebRequest request){
        return buildResponseEntity(ErrorResponse.of("NotFoundException", HttpStatus.NOT_FOUND));
    }

}
