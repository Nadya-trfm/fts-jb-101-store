package com.foodtech.store.product.model;

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
public class ProductDoc {
    @Id
     private ObjectId id;
     private String title;
     private String description;
     private ObjectId photoId;
     private Integer price;
     private ObjectId categoryId;
     private Float proteins;
     private Float fats;
     private Float carbohydrates;
     private Float kcal;
}
