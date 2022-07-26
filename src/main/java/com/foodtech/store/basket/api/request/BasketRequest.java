package com.foodtech.store.basket.api.request;


import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.util.List;

@Getter
@Setter
@ApiModel(value ="BasketRequest",description = "Model for update Basket")
public class BasketRequest {
            private ObjectId id;
            private ObjectId userId;
            private List<ObjectId> bundlesId;
}
