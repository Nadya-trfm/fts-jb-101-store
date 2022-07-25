package com.foodtech.store.photo.api.request;


import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@Getter
@Setter
@ApiModel(value ="PhotoRequest",description = "Model for update Photo")
public class PhotoRequest {
            private ObjectId id;
            private String title;
            private String contentType;
}
