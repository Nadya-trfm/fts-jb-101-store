package com.foodtech.store.address.model;

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
public class AddressDoc {
    @Id
     private ObjectId id;
     private ObjectId streetId;
     private String house;
     private String room;
     private Integer entrance;
     private Integer level;
}
