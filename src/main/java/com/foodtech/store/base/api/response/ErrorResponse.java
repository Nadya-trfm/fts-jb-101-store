package com.foodtech.store.base.api.response;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@ApiModel(value = "ErrorResponse",description = "Template for error response")
public class ErrorResponse extends OkResponse{
    private String error;
    private HttpStatus httpStatus;

    public static ErrorResponse of(String errorString, HttpStatus httpStatus){
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setResult(null);
        errorResponse.setStatus(Status.ERROR);
        errorResponse.setHttpStatus(httpStatus);
        errorResponse.setError(errorString);
        return errorResponse;
    }
}
