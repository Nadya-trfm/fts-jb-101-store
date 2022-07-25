 package com.foodtech.store.category.service;

import com.foodtech.store.account.model.AccountDoc;
import com.foodtech.store.auth.exceptions.AuthException;
import com.foodtech.store.auth.exceptions.NotAccessException;
import com.foodtech.store.auth.service.AuthService;
import com.foodtech.store.base.api.request.SearchRequest;
import com.foodtech.store.base.api.response.SearchResponse;
import com.foodtech.store.category.api.request.CategoryRequest;
import com.foodtech.store.category.mapping.CategoryMapping;
import com.foodtech.store.category.api.response.CategoryResponse;
import com.foodtech.store.category.exeception.CategoryExistException;
import com.foodtech.store.category.exeception.CategoryNotExistException;
import com.foodtech.store.category.model.CategoryDoc;
import com.foodtech.store.category.repository.CategoryRepository;
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
public class CategoryApiService {
    private final CategoryRepository categoryRepository;
    private final AuthService authService;
    private final MongoTemplate mongoTemplate;

    public CategoryDoc create(CategoryRequest request) throws NotAccessException, AuthException, CategoryExistException {
         AccountDoc verification =authService.currentAccount();
         if(verification.getIsAdmin() == false) throw new NotAccessException();
         if(categoryRepository.findByTitle(request.getTitle()).isPresent()) throw new CategoryExistException();
        CategoryDoc categoryDoc =CategoryMapping.getInstance().getRequest().convert(request);
        categoryRepository.save(categoryDoc);
        return  categoryDoc;
    }

    public Optional<CategoryDoc> findByID(ObjectId id){
        return categoryRepository.findById(id);
    }
    public SearchResponse<CategoryDoc> search(
             SearchRequest request
    ){
        Criteria criteria = new Criteria();
        if(request.getQuery() != null && request.getQuery()!=""){
            criteria = criteria.orOperator(
                   Criteria.where("title").regex(request.getQuery(), "i")

            );
        }

        Query query = new Query(criteria);
        Long count = mongoTemplate.count(query, CategoryDoc.class);
        query.limit(request.getSize());
        query.skip(request.getSkip());

        List<CategoryDoc> categoryDocs = mongoTemplate.find(query, CategoryDoc.class);
        return SearchResponse.of(categoryDocs, count);
    }

    public CategoryDoc update(ObjectId id, CategoryRequest request) throws CategoryNotExistException,NotAccessException, AuthException  {
         AccountDoc verification =authService.currentAccount();
         if(verification.getIsAdmin() == false) throw new NotAccessException();
        Optional<CategoryDoc> categoryDocOptional = categoryRepository.findById(id);
        if(categoryDocOptional.isPresent() == false){
            throw new CategoryNotExistException();
        }
        CategoryDoc categoryDoc = categoryDocOptional.get();
        categoryDoc.setId(id);
        categoryDoc.setTitle(request.getTitle());

        categoryRepository.save(categoryDoc);

        return categoryDoc;
    }

    public void delete(ObjectId id) throws CategoryNotExistException,NotAccessException, AuthException {
         AccountDoc verification =authService.currentAccount();
         if(verification.getIsAdmin() == false) throw new NotAccessException();
         Optional<CategoryDoc> categoryDocOptional = categoryRepository.findById(id);
         if(categoryDocOptional.isPresent() == false){
         throw new CategoryNotExistException();
         }
        categoryRepository.deleteById(id);
    }
}
