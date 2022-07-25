package com.foodtech.store.street.model;

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
public class StreetDoc {
    @Id
     private ObjectId id;
     private String title;
     private ObjectId cityId;
}
