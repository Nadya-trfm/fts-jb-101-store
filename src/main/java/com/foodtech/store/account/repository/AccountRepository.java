package com.foodtech.store.account.repository;

import com.foodtech.store.account.model.AccountDoc;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface AccountRepository extends MongoRepository<AccountDoc, ObjectId> {
    public Optional<AccountDoc> findByEmail(String email);
}
