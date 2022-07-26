package com.foodtech.store.bundle.api.request;


import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@Getter
@Setter
@ApiModel(value ="BundleRequest",description = "Model for update Bundle")
public class BundleRequest {
            private ObjectId id;
            private ObjectId productId;
            private Integer quantity;
}
