package com.foodtech.store.category.repository;

import com.foodtech.store.account.model.AccountDoc;
import com.foodtech.store.category.model.CategoryDoc;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends MongoRepository<CategoryDoc, ObjectId> {
    public Optional<CategoryDoc> findByTitle(String title);
}
