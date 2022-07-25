package com.foodtech.store.account.service;

import com.foodtech.store.account.api.request.AccountRequest;
import com.foodtech.store.account.api.request.RegistrationRequest;
import com.foodtech.store.account.exceptions.AccountExistException;
import com.foodtech.store.account.model.AccountDoc;
import com.foodtech.store.account.repository.AccountRepository;
import com.foodtech.store.auth.exceptions.AuthException;
import com.foodtech.store.auth.exceptions.NotAccessException;
import com.foodtech.store.auth.service.AuthService;
import com.foodtech.store.base.api.request.SearchRequest;
import com.foodtech.store.base.api.response.SearchResponse;
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
public class AccountApiService {
    private final AccountRepository accountRepository;
    private final MongoTemplate mongoTemplate;
    private final AuthService authService;


    public AccountDoc registration(RegistrationRequest request) throws AccountExistException {
        if(accountRepository.findByEmail(request.getEmail()).isPresent() == true){
            throw new AccountExistException();
        }
        AccountDoc accountDoc = new AccountDoc();
        accountDoc.setEmail(request.getEmail());
        accountDoc.setPassword(AccountDoc.hexPassword(request.getPassword()));
        accountDoc.setIsAdmin(request.getIsAdmin());
        accountDoc = accountRepository.save(accountDoc);

        return  accountDoc;
    }

    public Optional<AccountDoc> findByID(ObjectId id) throws AuthException, NotAccessException {
        AccountDoc verification =authService.currentAccount();
        Boolean isAuthor =verification.getId().equals(id);
        Boolean isAdmin=verification.getIsAdmin();
        Boolean canEdit= isAuthor||isAdmin;
        if(!canEdit) throw new NotAccessException();
        return accountRepository.findById(id);
    }

    public SearchResponse<AccountDoc> search(
            SearchRequest request
    ) throws AuthException, NotAccessException {
        AccountDoc verification =authService.currentAccount();
        if(verification.getIsAdmin() == false) throw new NotAccessException();
        Criteria criteria = new Criteria();
        if(request.getQuery() != null && request.getQuery()!=""){
            criteria = criteria.orOperator(
                    Criteria.where("name").regex(request.getQuery(), "i"),
                    Criteria.where("phone").regex(request.getQuery(), "i"),
                    Criteria.where("email").regex(request.getQuery(), "i")
            );
        }

        Query query = new Query(criteria);
        Long count = mongoTemplate.count(query, AccountDoc.class);
        query.limit(request.getSize());
        query.skip(request.getSkip());

        List<AccountDoc> accountDocs = mongoTemplate.find(query, AccountDoc.class);
        return SearchResponse.of(accountDocs, count);
    }

    public AccountDoc update(ObjectId id, AccountRequest request) throws  AuthException, NotAccessException {
        AccountDoc verification =authService.currentAccount();
        Boolean isAuthor =verification.getId().equals(id);
        Boolean isAdmin=verification.getIsAdmin();
        Boolean canEdit= isAuthor||isAdmin;
        if(!canEdit) throw new NotAccessException();


        Optional<AccountDoc> accountDocOptional = accountRepository.findById(id);
        AccountDoc accountDoc = accountDocOptional.get();
        accountDoc.setName(request.getName());
        accountDoc.setPhone(request.getPhone());
        accountDoc.setEmail(request.getEmail());
        accountRepository.save(accountDoc);

        return accountDoc;
    }

    public void delete(ObjectId id) throws NotAccessException, AuthException {
        AccountDoc verification =authService.currentAccount();
        Boolean isAuthor =verification.getId().equals(id);
        Boolean isAdmin=verification.getIsAdmin();
        Boolean canEdit= isAuthor||isAdmin;
        if(!canEdit) throw new NotAccessException();
        accountRepository.deleteById(id);
    }
}
