package com.foodtech.store.street.api.response;

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
@ApiModel(value ="StreetResponse",description = "Street data(for search and list)")
public class StreetResponse {
            protected String id;
            protected String title;
            protected String cityId;
}
