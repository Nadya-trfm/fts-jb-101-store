package com.foodtech.store.user.api.response;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;

@Getter
@Setter
@SuperBuilder
@RequiredArgsConstructor
@AllArgsConstructor
@ApiModel(value ="UserResponse",description = "User data(for search and list)")
public class UserResponse {
            protected String id;
            protected String name;
            protected Long phone;
            protected String accountId =null;
}
