package com.foodtech.store.bundle.model;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Setter
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BundleDoc {
    @Id
     private ObjectId id;
     private ObjectId productId;
     private Integer quantity;
}
