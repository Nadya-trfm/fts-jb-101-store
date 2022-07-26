package com.foodtech.store.address.api.request;


import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@Getter
@Setter
@ApiModel(value ="AddressRequest",description = "Model for update Address")
public class AddressRequest {
            private ObjectId id;
            private ObjectId streetId;
            private String house;
            private String room;
            private Integer entrance;
            private Integer level;
}
