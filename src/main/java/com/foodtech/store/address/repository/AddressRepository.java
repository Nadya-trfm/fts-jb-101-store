package com.foodtech.store.address.repository;

import com.foodtech.store.address.model.AddressDoc;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends MongoRepository<AddressDoc, ObjectId> {

}
