package com.foodtech.store.account.api.response;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@ApiModel(value ="AccountResponse",description = "account data(for search and list)")
public class AccountResponse {
    protected String id;
    protected String name;
    protected Long phone;
    protected String email;
    protected Boolean isAdmin;
}
