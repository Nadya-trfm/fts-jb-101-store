package com.foodtech.store.order.api.request;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;

@Getter
@Setter
@ToString
@ApiModel(value =" CreateOrderRequest",description = "Model for create/update Order for user")
public class CreateOrderRequest {
    protected String payment;
    protected ObjectId basketId;
    protected ObjectId addressId;
    protected Long timeToDelivery;
}
