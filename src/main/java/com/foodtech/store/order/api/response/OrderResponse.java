package com.foodtech.store.order.api.response;

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
@ApiModel(value ="OrderResponse",description = "Order data(for search and list)")
public class OrderResponse {
            protected String id;
            protected String payment;
            protected String basketId;
            protected String addressId;
            protected String status;
            protected Long timeToDelivery;
            protected Integer finalPrice;
}
