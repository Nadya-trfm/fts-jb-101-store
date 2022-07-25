package com.foodtech.store.city.service;

import com.foodtech.store.account.model.AccountDoc;
import com.foodtech.store.auth.exceptions.AuthException;
import com.foodtech.store.auth.exceptions.NotAccessException;
import com.foodtech.store.auth.service.AuthService;
import com.foodtech.store.base.api.request.SearchRequest;
import com.foodtech.store.base.api.response.SearchResponse;
import com.foodtech.store.city.api.request.CityRequest;
import com.foodtech.store.city.mapping.CityMapping;
import com.foodtech.store.city.api.response.CityResponse;
import com.foodtech.store.city.exeception.CityExistException;
import com.foodtech.store.city.exeception.CityNotExistException;
import com.foodtech.store.city.model.CityDoc;
import com.foodtech.store.city.repository.CityRepository;
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
public class CityApiService {
    private final CityRepository cityRepository;
    private final AuthService authService;
    private final MongoTemplate mongoTemplate;

    public CityDoc create(CityRequest request) throws NotAccessException, AuthException {
        AccountDoc verification = authService.currentAccount();
        if (verification.getIsAdmin() == false) throw new NotAccessException();
        CityDoc cityDoc = CityMapping.getInstance().getRequest().convert(request);

        cityRepository.save(cityDoc);
        return cityDoc;
    }

    public Optional<CityDoc> findByID(ObjectId id) {
        return cityRepository.findById(id);
    }

    public SearchResponse<CityDoc> search(
            SearchRequest request
    ) {
        Criteria criteria = new Criteria();
        if (request.getQuery() != null && request.getQuery() != "") {
            criteria = criteria.orOperator(
                    //TODO: Add Criteria
                    Criteria.where("title").regex(request.getQuery(), "i")

            );
        }

        Query query = new Query(criteria);
        Long count = mongoTemplate.count(query, CityDoc.class);
        query.limit(request.getSize());
        query.skip(request.getSkip());

        List<CityDoc> cityDocs = mongoTemplate.find(query, CityDoc.class);
        return SearchResponse.of(cityDocs, count);
    }

    public CityDoc update(ObjectId id, CityRequest request) throws CityNotExistException, NotAccessException, AuthException {
        AccountDoc verification = authService.currentAccount();
        if (verification.getIsAdmin() == false) throw new NotAccessException();
            Optional<CityDoc> cityDocOptional = cityRepository.findById(id);
        if (cityDocOptional.isPresent() == false) {
            throw new CityNotExistException();
        }
        CityDoc cityDoc = cityDocOptional.get();
        cityDoc.setId(id);
        cityDoc.setPriceMultiplier(Float.parseFloat(request.getPriceMultiplier()));
        cityDoc.setTitle(request.getTitle());

        cityRepository.save(cityDoc);

        return cityDoc;
    }

    public void delete(ObjectId id) throws CityNotExistException, NotAccessException, AuthException {
        AccountDoc verification = authService.currentAccount();
        if (verification.getIsAdmin() == false) throw new NotAccessException();

        Optional<CityDoc> cityDocOptional = cityRepository.findById(id);

        if (cityDocOptional.isPresent() == false) {
            throw new CityNotExistException();
        }

        cityRepository.deleteById(id);
    }
}
