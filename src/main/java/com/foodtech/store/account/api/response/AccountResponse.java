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
    private String id;
    private String name;
    private Long phone;
    private String email;
    private Boolean isAdmin;
}
