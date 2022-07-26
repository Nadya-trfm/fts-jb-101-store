package com.foodtech.store.order.api.request;


import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;

@Getter
@Setter

@ApiModel(value ="OrderRequest",description = "Model for update Order")
public class OrderRequest extends CreateOrderRequest {
    private ObjectId id;
            private String status;

            private Integer finalPrice;
}
