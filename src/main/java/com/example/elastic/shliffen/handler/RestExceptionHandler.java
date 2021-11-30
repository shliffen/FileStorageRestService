package com.example.elastic.shliffen.handler;

import com.example.elastic.shliffen.dto.error.ErrorDetail;
import com.example.elastic.shliffen.exception.CustomException;
import com.fasterxml.jackson.core.JsonParseException;
import org.elasticsearch.common.inject.Inject;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class RestExceptionHandler {

    @Inject
    private MessageSource messageSource;

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> handleCustomException(CustomException customException,
                                                   HttpServletRequest request) {
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setErrorMessage(customException.getMessage());
        errorDetail.setSuccess(false);
        return new ResponseEntity<>(errorDetail, null, customException.getStatus());
    }

    @ExceptionHandler(JsonParseException.class)
    public ResponseEntity<?> handleCustomException(JsonParseException jsonException,
                                                   HttpServletRequest request) {
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setErrorMessage("you didn't entered the acceptable values of filename and size");
        errorDetail.setSuccess(false);
        return new ResponseEntity<>(errorDetail, null, HttpStatus.BAD_REQUEST);
    }

}
