package com.foodtech.store.basket.model;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@Setter
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BasketDoc {
    @Id
     private ObjectId id;
     private ObjectId userId;
     private List<ObjectId> bundlesId;
}
