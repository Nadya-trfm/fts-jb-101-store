package com.foodtech.store.category.api.request;


import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@Getter
@Setter
@ApiModel(value ="CategoryRequest",description = "Model for update Category")
public class CategoryRequest {
            private ObjectId id;
            private String title;
}
