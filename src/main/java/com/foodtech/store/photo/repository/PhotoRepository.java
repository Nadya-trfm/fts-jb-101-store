package com.foodtech.store.photo.repository;

import com.foodtech.store.photo.model.PhotoDoc;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhotoRepository extends MongoRepository<PhotoDoc, ObjectId> {

}
