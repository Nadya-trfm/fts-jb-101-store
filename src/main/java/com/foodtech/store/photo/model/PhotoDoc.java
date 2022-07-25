package com.foodtech.store.photo.model;

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
public class PhotoDoc {
    @Id
     private ObjectId id;
     private String title;
     private String contentType;
}
