package com.foodtech.store.photo.api.response;

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
@ApiModel(value ="PhotoResponse",description = "Photo data(for search and list)")
public class PhotoResponse {
            protected String id;
            protected String title;
            protected String contentType;
}
