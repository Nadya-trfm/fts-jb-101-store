package com.foodtech.store.address.api.response;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@ApiModel(value ="AddressResponse",description = "Address data(for search and list)")
public class AddressResponse {
            protected String id;
            protected String streetId;
            protected String house;
            protected String room;
            protected Integer entrance;
            protected Integer level;
}
