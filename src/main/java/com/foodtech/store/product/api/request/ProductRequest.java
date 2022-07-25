package com.foodtech.store.product.api.request;


import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@Getter
@Setter
@ApiModel(value ="ProductRequest",description = "Model for update Product")
public class ProductRequest {
            private ObjectId id;
            private String title;
            private String description;
            private ObjectId photoId;
            private Integer price;
            private ObjectId categoryId;
            private String proteins;
            private String fats;
            private String carbohydrates;
            private String kcal;
}
