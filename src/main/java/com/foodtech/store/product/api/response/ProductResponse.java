package com.foodtech.store.product.api.response;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@ApiModel(value ="ProductResponse",description = "Product data(for search and list)")
public class ProductResponse {
            protected String id;
            protected String title;
            protected String description;
            protected String photoId;
            protected Integer price;
            protected String categoryId;
            protected Float proteins;
            protected Float fats;
            protected Float carbohydrates;
            protected Float kcal;
}
