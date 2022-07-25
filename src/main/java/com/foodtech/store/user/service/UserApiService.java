package com.foodtech.store.user.service;

import com.foodtech.store.account.model.AccountDoc;
import com.foodtech.store.account.repository.AccountRepository;
import com.foodtech.store.auth.exceptions.AuthException;
import com.foodtech.store.auth.exceptions.NotAccessException;
import com.foodtech.store.auth.service.AuthService;
import com.foodtech.store.base.api.request.SearchRequest;
import com.foodtech.store.base.api.response.SearchResponse;
import com.foodtech.store.user.api.request.UserRequest;
import com.foodtech.store.user.mapping.UserMapping;
import com.foodtech.store.user.api.response.UserResponse;
import com.foodtech.store.user.exeception.UserExistException;
import com.foodtech.store.user.exeception.UserNotExistException;
import com.foodtech.store.user.model.UserDoc;
import com.foodtech.store.user.repository.UserRepository;
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
public class UserApiService {
    private final UserRepository userRepository;
    private final AuthService authService;
    private final MongoTemplate mongoTemplate;



    public UserDoc create(UserRequest request)  {
        UserDoc userDoc =new UserDoc();
        try {
            AccountDoc accountDoc =authService.currentAccount();

            userDoc.setName(accountDoc.getName());
            userDoc.setPhone(accountDoc.getPhone());
            userDoc.setAccountId(accountDoc.getId());
            userRepository.save(userDoc);
            return userDoc;

        } catch (AuthException e) {
            userDoc.setName(request.getName());
            userDoc.setPhone(request.getPhone());
            userDoc.setAccountId(null);
            userRepository.save(userDoc);
            return userDoc;
        }

    }

    public Optional<UserDoc> findByID(ObjectId id) throws AuthException, NotAccessException {
        AccountDoc verification = authService.currentAccount();
        if (verification.getIsAdmin() == false) throw new NotAccessException();
        return userRepository.findById(id);
    }

    public SearchResponse<UserDoc> search(
            SearchRequest request
    ) throws AuthException, NotAccessException {
        AccountDoc verification = authService.currentAccount();
        if (verification.getIsAdmin() == false) throw new NotAccessException();
        Criteria criteria = new Criteria();
        if (request.getQuery() != null && request.getQuery() != "") {
            criteria = criteria.orOperator(
                    Criteria.where("name").regex(request.getQuery(), "i"),
                    Criteria.where("phone").regex(request.getQuery(), "i")

            );
        }

        Query query = new Query(criteria);
        Long count = mongoTemplate.count(query, UserDoc.class);
        query.limit(request.getSize());
        query.skip(request.getSkip());

        List<UserDoc> userDocs = mongoTemplate.find(query, UserDoc.class);
        return SearchResponse.of(userDocs, count);
    }


    public void delete(ObjectId id) throws UserNotExistException, NotAccessException, AuthException {
        AccountDoc verification = authService.currentAccount();
        if (verification.getIsAdmin() == false) throw new NotAccessException();
        Optional<UserDoc> userDocOptional = userRepository.findById(id);
        if (userDocOptional.isPresent() == false) {
            throw new UserNotExistException();
        }
        userRepository.deleteById(id);
    }
}
