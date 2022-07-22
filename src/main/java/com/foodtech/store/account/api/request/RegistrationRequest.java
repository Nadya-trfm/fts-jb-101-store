package com.foodtech.store.account.api.request;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value ="RegistrationRequest",description = "Model for register")
public class RegistrationRequest {
    private String email;
    private String password;
    private Boolean isAdmin = false;
}

