package com.foodtech.store.account.api.request;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value ="AccountRequest",description = "Model for update account")
public class AccountRequest {

    protected String name;
    protected Long phone;
    protected String email;
}
