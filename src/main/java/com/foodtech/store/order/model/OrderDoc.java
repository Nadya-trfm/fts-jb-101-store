package com.foodtech.store.order.model;

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
public class OrderDoc {
    @Id
     private ObjectId id;
     private Payment payment;
     private ObjectId basketId;
     private ObjectId addressId;
     private Status status;
     private Long timeToDelivery;
     private Integer finalPrice;
}
