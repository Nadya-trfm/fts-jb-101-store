package com.foodtech.store.city.api.response;

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
@ApiModel(value ="CityResponse",description = "City data(for search and list)")
public class CityResponse {
            protected String id;
            protected String title;
            protected Float priceMultiplier;
}
