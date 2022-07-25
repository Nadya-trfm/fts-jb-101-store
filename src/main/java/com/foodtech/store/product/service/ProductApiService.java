package com.foodtech.store.product.service;

import com.foodtech.store.account.model.AccountDoc;
import com.foodtech.store.auth.exceptions.AuthException;
import com.foodtech.store.auth.exceptions.NotAccessException;
import com.foodtech.store.auth.service.AuthService;
import com.foodtech.store.base.api.request.SearchRequest;
import com.foodtech.store.base.api.response.SearchResponse;
import com.foodtech.store.category.exeception.CategoryNotExistException;
import com.foodtech.store.category.model.CategoryDoc;
import com.foodtech.store.category.repository.CategoryRepository;
import com.foodtech.store.photo.exeception.PhotoNotExistException;
import com.foodtech.store.photo.model.PhotoDoc;
import com.foodtech.store.photo.repository.PhotoRepository;
import com.foodtech.store.product.api.request.ProductRequest;
import com.foodtech.store.product.mapping.ProductMapping;
import com.foodtech.store.product.exeception.ProductNotExistException;
import com.foodtech.store.product.model.ProductDoc;
import com.foodtech.store.product.repository.ProductRepository;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductApiService {
    private final ProductRepository productRepository;
    private final AuthService authService;
    private final MongoTemplate mongoTemplate;
    private final CategoryRepository categoryRepository;
    private final PhotoRepository photoRepository;

    public ProductDoc create(ProductRequest request) throws NotAccessException, AuthException,
            CategoryNotExistException, PhotoNotExistException {
        AccountDoc verification = authService.currentAccount();
        if (verification.getIsAdmin() == false) throw new NotAccessException();
        if (categoryRepository.findById(request.getCategoryId()).isPresent() == false)
            throw new CategoryNotExistException();
        if (photoRepository.findById(request.getPhotoId()).isPresent() == false) throw new PhotoNotExistException();
        ProductDoc productDoc = ProductMapping.getInstance().getRequest().convert(request);
        productRepository.save(productDoc);
        return productDoc;
    }

    public Optional<ProductDoc> findByID(ObjectId id) {
        return productRepository.findById(id);
    }

    public SearchResponse<ProductDoc> search(
            SearchRequest request
    ) {
        Criteria criteria = new Criteria();
        if (request.getQuery() != null && request.getQuery() != "") {
            criteria = criteria.orOperator(
                    Criteria.where("title").regex(request.getQuery(), "i"),
                    Criteria.where("categoryId").regex(request.getQuery(), "i"),
                    Criteria.where("price").regex(request.getQuery(), "i")

            );
        }

        Query query = new Query(criteria);
        Long count = mongoTemplate.count(query, ProductDoc.class);
        query.limit(request.getSize());
        query.skip(request.getSkip());

        List<ProductDoc> productDocs = mongoTemplate.find(query, ProductDoc.class);
        return SearchResponse.of(productDocs, count);
    }

    public ProductDoc update(ObjectId id, ProductRequest request) throws ProductNotExistException, NotAccessException, AuthException, PhotoNotExistException, CategoryNotExistException {
        AccountDoc verification = authService.currentAccount();
        if (verification.getIsAdmin() == false) throw new NotAccessException();
        Optional<ProductDoc> productDocOptional = productRepository.findById(id);
        if (productDocOptional.isPresent() == false) {
            throw new ProductNotExistException();
        }
        if (categoryRepository.findById(request.getCategoryId()).isPresent() == false)
            throw new CategoryNotExistException();
        if (photoRepository.findById(request.getPhotoId()).isPresent() == false) throw new PhotoNotExistException();

        ProductDoc productDoc = ProductMapping.getInstance().getRequest().convert(request);
        ProductDoc productDoc1 = productDocOptional.get();
        productDoc.setId(productDoc1.getId());
        productRepository.save(productDoc);

        return productDoc;
    }

    public void delete(ObjectId id) throws ProductNotExistException, NotAccessException, AuthException {
        AccountDoc verification = authService.currentAccount();
        if (verification.getIsAdmin() == false) throw new NotAccessException();
        Optional<ProductDoc> productDocOptional = productRepository.findById(id);
        if (productDocOptional.isPresent() == false) {
            throw new ProductNotExistException();
        }
        productRepository.deleteById(id);
    }
}
