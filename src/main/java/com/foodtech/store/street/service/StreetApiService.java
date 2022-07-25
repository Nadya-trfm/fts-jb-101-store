 package com.foodtech.store.street.service;

import com.foodtech.store.account.model.AccountDoc;
import com.foodtech.store.auth.exceptions.AuthException;
import com.foodtech.store.auth.exceptions.NotAccessException;
import com.foodtech.store.auth.service.AuthService;
import com.foodtech.store.base.api.request.SearchRequest;
import com.foodtech.store.base.api.response.SearchResponse;
import com.foodtech.store.street.api.request.StreetRequest;
import com.foodtech.store.street.mapping.StreetMapping;
import com.foodtech.store.street.api.response.StreetResponse;
import com.foodtech.store.street.exeception.StreetExistException;
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
public class StreetApiService {
    private final StreetRepository streetRepository;
    private final AuthService authService;
    private final MongoTemplate mongoTemplate;

    public StreetDoc create(StreetRequest request) throws NotAccessException, AuthException {
         AccountDoc verification =authService.currentAccount();
         if(verification.getIsAdmin() == false) throw new NotAccessException();
        StreetDoc streetDoc =StreetMapping.getInstance().getRequest().convert(request);
        streetRepository.save(streetDoc);
        return  streetDoc;
    }

    public Optional<StreetDoc> findByID(ObjectId id){
        return streetRepository.findById(id);
    }
    public SearchResponse<StreetDoc> search(
             SearchRequest request
    ){
        Criteria criteria = new Criteria();
        if(request.getQuery() != null && request.getQuery()!=""){
            criteria = criteria.orOperator(
                    Criteria.where("title").regex(request.getQuery(), "i"),
                    Criteria.where("cityId").regex(request.getQuery(), "i")

            );
        }

        Query query = new Query(criteria);
        Long count = mongoTemplate.count(query, StreetDoc.class);
        query.limit(request.getSize());
        query.skip(request.getSkip());

        List<StreetDoc> streetDocs = mongoTemplate.find(query, StreetDoc.class);
        return SearchResponse.of(streetDocs, count);
    }

    public StreetDoc update(ObjectId id, StreetRequest request) throws StreetNotExistException,NotAccessException, AuthException  {
         AccountDoc verification =authService.currentAccount();
         if(verification.getIsAdmin() == false) throw new NotAccessException();
        Optional<StreetDoc> streetDocOptional = streetRepository.findById(id);
        if(streetDocOptional.isPresent() == false){
            throw new StreetNotExistException();
        }
        StreetDoc streetDoc = streetDocOptional.get();
        streetDoc.setId(id);
        streetDoc.setCityId(request.getCityId());
        streetDoc.setTitle(request.getTitle());

        streetRepository.save(streetDoc);

        return streetDoc;
    }

    public void delete(ObjectId id) throws StreetNotExistException,NotAccessException, AuthException {
         AccountDoc verification =authService.currentAccount();
         if(verification.getIsAdmin() == false) throw new NotAccessException();
         Optional<StreetDoc> streetDocOptional = streetRepository.findById(id);
         if(streetDocOptional.isPresent() == false){
         throw new StreetNotExistException();
         }
        streetRepository.deleteById(id);
    }
}
