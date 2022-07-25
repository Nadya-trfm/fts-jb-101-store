package com.foodtech.store.city.api.request;


import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@Getter
@Setter
@ApiModel(value ="CityRequest",description = "Model for update City")
public class CityRequest {
            private ObjectId id;
            private String title;
            private String priceMultiplier;
}
