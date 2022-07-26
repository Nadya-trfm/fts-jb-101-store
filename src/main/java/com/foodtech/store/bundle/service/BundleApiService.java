 package com.foodtech.store.bundle.service;


import com.foodtech.store.account.model.AccountDoc;
import com.foodtech.store.auth.exceptions.AuthException;
import com.foodtech.store.auth.exceptions.NotAccessException;
import com.foodtech.store.auth.service.AuthService;
import com.foodtech.store.base.api.request.SearchRequest;
import com.foodtech.store.base.api.response.SearchResponse;
import com.foodtech.store.bundle.api.request.BundleRequest;
import com.foodtech.store.bundle.mapping.BundleMapping;
import com.foodtech.store.bundle.exeception.BundleNotExistException;
import com.foodtech.store.bundle.model.BundleDoc;
import com.foodtech.store.bundle.repository.BundleRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BundleApiService {
    private final BundleRepository bundleRepository;
    private final MongoTemplate mongoTemplate;
    private final AuthService authService;

    public BundleDoc create(BundleRequest request){
        BundleDoc bundleDoc =BundleMapping.getInstance().getRequest().convert(request);
        bundleRepository.save(bundleDoc);
        return  bundleDoc;
    }

    public Optional<BundleDoc> findByID(ObjectId id){
        return bundleRepository.findById(id);
    }

    public SearchResponse<BundleDoc> search(
            SearchRequest request
    ) throws AuthException, NotAccessException {
        AccountDoc verification =authService.currentAccount();
        if(verification.getIsAdmin() == false) throw new NotAccessException();

        Query query = new Query();
        Long count = mongoTemplate.count(query, BundleDoc.class);
        query.limit(request.getSize());
        query.skip(request.getSkip());

        List<BundleDoc> bundleDocs = mongoTemplate.find(query, BundleDoc.class);
        return SearchResponse.of(bundleDocs, count);
    }
    public BundleDoc update(ObjectId id, BundleRequest request) throws BundleNotExistException  {
        Optional<BundleDoc> bundleDocOptional = bundleRepository.findById(id);
        if(bundleDocOptional.isPresent() == false){
            throw new BundleNotExistException();
        }
        BundleDoc bundleDoc = bundleDocOptional.get();
        bundleDoc.setId(id);
        bundleDoc.setQuantity(request.getQuantity());
        bundleRepository.save(bundleDoc);

        return bundleDoc;
    }

    public void delete(ObjectId id) throws BundleNotExistException {

         Optional<BundleDoc> bundleDocOptional = bundleRepository.findById(id);
         if(bundleDocOptional.isPresent() == false){
         throw new BundleNotExistException();
         }
        bundleRepository.deleteById(id);
    }
}
