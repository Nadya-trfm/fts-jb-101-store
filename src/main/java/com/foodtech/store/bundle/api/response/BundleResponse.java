package com.foodtech.store.bundle.api.response;

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
@ApiModel(value ="BundleResponse",description = "Bundle data(for search and list)")
public class BundleResponse {
            protected String id;
            protected String productId;
            protected Integer quantity;
}
