package com.foodtech.store.basket.api.response;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@ApiModel(value ="BasketResponse",description = "Basket data(for search and list)")
public class BasketResponse {
            protected String id;
            protected String  userId;
            protected List<String> bundlesId;
}
