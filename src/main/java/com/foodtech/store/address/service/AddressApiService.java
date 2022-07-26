 package com.foodtech.store.address.service;

import com.foodtech.store.account.model.AccountDoc;
import com.foodtech.store.auth.exceptions.AuthException;
import com.foodtech.store.auth.exceptions.NotAccessException;
import com.foodtech.store.auth.service.AuthService;
import com.foodtech.store.base.api.request.SearchRequest;
import com.foodtech.store.base.api.response.SearchResponse;
import com.foodtech.store.address.api.request.AddressRequest;
import com.foodtech.store.address.mapping.AddressMapping;
import com.foodtech.store.address.api.response.AddressResponse;
import com.foodtech.store.address.exeception.AddressExistException;
import com.foodtech.store.address.exeception.AddressNotExistException;
import com.foodtech.store.address.model.AddressDoc;
import com.foodtech.store.address.repository.AddressRepository;
import com.foodtech.store.street.exeception.StreetNotExistException;
import com.foodtech.store.street.model.StreetDoc;
import com.foodtech.store.street.repository.StreetRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AddressApiService {
    private final AddressRepository addressRepository;
    private final AuthService authService;
    private final MongoTemplate mongoTemplate;
    private final StreetRepository streetRepository;

    public AddressDoc create(AddressRequest request) throws StreetNotExistException {
        Optional<StreetDoc> streetDocOptional = streetRepository.findById(request.getStreetId());
        if(streetDocOptional.isPresent() == false){
            throw new StreetNotExistException();
        }
        AddressDoc addressDoc =AddressMapping.getInstance().getRequest().convert(request);
        addressRepository.save(addressDoc);
        return  addressDoc;
    }

    public Optional<AddressDoc> findByID(ObjectId id){
        return addressRepository.findById(id);
    }
    public SearchResponse<AddressDoc> search(
             SearchRequest request
    ) throws AuthException, NotAccessException {
        AccountDoc verification =authService.currentAccount();
        if(verification.getIsAdmin() == false) throw new NotAccessException();
        Criteria criteria = new Criteria();
        if(request.getQuery() != null && request.getQuery()!=""){
            criteria = criteria.orOperator(
                    Criteria.where("streetId").regex(request.getQuery(), "i")

            );
        }

        Query query = new Query(criteria);
        Long count = mongoTemplate.count(query, AddressDoc.class);
        query.limit(request.getSize());
        query.skip(request.getSkip());

        List<AddressDoc> addressDocs = mongoTemplate.find(query, AddressDoc.class);
        return SearchResponse.of(addressDocs, count);
    }



    public void delete(ObjectId id) throws AddressNotExistException,NotAccessException, AuthException {
         AccountDoc verification =authService.currentAccount();
         if(verification.getIsAdmin() == false) throw new NotAccessException();
         Optional<AddressDoc> addressDocOptional = addressRepository.findById(id);
         if(addressDocOptional.isPresent() == false){
         throw new AddressNotExistException();
         }
        addressRepository.deleteById(id);
    }
}
