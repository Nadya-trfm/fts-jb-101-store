package com.foodtech.store.category.api.response;

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
@ApiModel(value ="CategoryResponse",description = "Category data(for search and list)")
public class CategoryResponse {
            protected String id;
            protected String title;
}
