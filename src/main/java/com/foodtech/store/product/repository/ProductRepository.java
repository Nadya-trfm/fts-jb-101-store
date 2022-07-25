package com.foodtech.store.product.repository;

import com.foodtech.store.product.model.ProductDoc;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends MongoRepository<ProductDoc, ObjectId> {

}
