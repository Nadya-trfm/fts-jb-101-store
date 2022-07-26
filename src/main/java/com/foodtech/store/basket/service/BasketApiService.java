 package com.foodtech.store.basket.service;

import com.foodtech.store.account.model.AccountDoc;
import com.foodtech.store.auth.exceptions.AuthException;
import com.foodtech.store.auth.exceptions.NotAccessException;
import com.foodtech.store.auth.service.AuthService;
import com.foodtech.store.base.api.request.SearchRequest;
import com.foodtech.store.base.api.response.SearchResponse;
import com.foodtech.store.basket.api.request.BasketRequest;
import com.foodtech.store.basket.mapping.BasketMapping;
import com.foodtech.store.basket.exeception.BasketNotExistException;
import com.foodtech.store.basket.model.BasketDoc;
import com.foodtech.store.basket.repository.BasketRepository;
import com.foodtech.store.bundle.exeception.BundleNotExistException;
import com.foodtech.store.bundle.model.BundleDoc;
import com.foodtech.store.bundle.repository.BundleRepository;
import com.foodtech.store.user.exeception.UserNotExistException;
import com.foodtech.store.user.model.UserDoc;
import com.foodtech.store.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BasketApiService {
    private final BasketRepository basketRepository;
    private final AuthService authService;
    private final MongoTemplate mongoTemplate;
    private final UserRepository userRepository;
    private final BundleRepository bundleRepository;

    public BasketDoc create(BasketRequest request) throws UserNotExistException, BundleNotExistException {
        Optional<UserDoc> userDocOptional = userRepository.findById(request.getUserId());
        if (userDocOptional.isPresent() == false) {
            throw new UserNotExistException();
        }
        for(ObjectId bundle: request.getBundlesId()){
            Optional<BundleDoc> bundleDocOptional = bundleRepository.findById(bundle);
            if (bundleDocOptional.isPresent() == false) {
                throw new BundleNotExistException();
            }
        }
        UserDoc userDoc =userDocOptional.get();
        BasketDoc basketDoc =BasketMapping.getInstance().getRequest().convert(request, userDoc.getId());
        basketRepository.save(basketDoc);
        return  basketDoc;
    }

    public Optional<BasketDoc> findByID(ObjectId id){
        return basketRepository.findById(id);
    }
    public SearchResponse<BasketDoc> search(
             SearchRequest request
    ) throws AuthException, NotAccessException {
        AccountDoc verification =authService.currentAccount();
        if(verification.getIsAdmin() == false) throw new NotAccessException();

        Query query = new Query();
        Long count = mongoTemplate.count(query, BasketDoc.class);
        query.limit(request.getSize());
        query.skip(request.getSkip());

        List<BasketDoc> basketDocs = mongoTemplate.find(query, BasketDoc.class);
        return SearchResponse.of(basketDocs, count);
    }

    public BasketDoc update(ObjectId id, BasketRequest request) throws BasketNotExistException, NotAccessException, BundleNotExistException {

        Optional<BasketDoc> basketDocOptional = basketRepository.findById(id);
        if(basketDocOptional.isPresent() == false){
            throw new BasketNotExistException();
        }
        Optional<UserDoc> userDocOptional = userRepository.findById(request.getUserId());
        if (userDocOptional.isPresent() == false) {
            throw new NotAccessException();
        }
        for(ObjectId bundle: request.getBundlesId()){
        Optional<BundleDoc> bundleDocOptional = bundleRepository.findById(bundle);
            if (bundleDocOptional.isPresent() == false) {
                throw new BundleNotExistException();
            }
        }

        BasketDoc basketDoc = basketDocOptional.get();
        basketDoc.setId(id);
        basketDoc.setBundlesId(request.getBundlesId());

        basketRepository.save(basketDoc);

        return basketDoc;
    }

    public void delete(ObjectId id) throws BasketNotExistException {
        Optional<BasketDoc> basketDocOptional = basketRepository.findById(id);
        if(basketDocOptional.isPresent() == false){
            throw new BasketNotExistException();
        }

        basketRepository.deleteById(id);
    }
}
