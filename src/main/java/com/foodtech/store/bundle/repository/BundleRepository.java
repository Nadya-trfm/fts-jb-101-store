package com.foodtech.store.bundle.repository;

import com.foodtech.store.bundle.model.BundleDoc;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BundleRepository extends MongoRepository<BundleDoc, ObjectId> {

}
