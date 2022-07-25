package com.foodtech.store.user.model;

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
public class UserDoc {
    @Id
     private ObjectId id;
     private String name;
     private Long phone;
     private ObjectId accountId ;
}
