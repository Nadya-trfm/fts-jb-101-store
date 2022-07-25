package com.foodtech.store.user.api.request;


import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@Getter
@Setter
@ApiModel(value ="UserRequest",description = "Model for create/update User, data for account(if account exsist)")
public class UserRequest {
            private ObjectId id;
            private String name;
            private Long phone;

}
