 package com.foodtech.store.photo.service;

import com.foodtech.store.account.model.AccountDoc;
import com.foodtech.store.auth.exceptions.AuthException;
import com.foodtech.store.auth.exceptions.NotAccessException;
import com.foodtech.store.auth.service.AuthService;
import com.foodtech.store.base.api.request.SearchRequest;
import com.foodtech.store.base.api.response.SearchResponse;
import com.foodtech.store.photo.api.request.PhotoRequest;
import com.foodtech.store.photo.mapping.PhotoMapping;
import com.foodtech.store.photo.api.response.PhotoResponse;
import com.foodtech.store.photo.exeception.PhotoExistException;
import com.foodtech.store.photo.exeception.PhotoNotExistException;
import com.foodtech.store.photo.model.PhotoDoc;
import com.foodtech.store.photo.repository.PhotoRepository;
import com.foodtech.store.street.model.StreetDoc;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PhotoApiService {
    private final PhotoRepository photoRepository;
    private final AuthService authService;
    private final MongoTemplate mongoTemplate;
    private final GridFsTemplate gridFsTemplate;
    private final GridFsOperations operations;

    public PhotoDoc create(MultipartFile file) throws NotAccessException, AuthException, IOException {
         AccountDoc verification =authService.currentAccount();
         if(verification.getIsAdmin() == false) throw new NotAccessException();

        DBObject metaData = new BasicDBObject();
        metaData.put("type",file.getContentType());
        metaData.put("title",file.getOriginalFilename());

        ObjectId id = gridFsTemplate.store(
                file.getInputStream(), file.getOriginalFilename(), file.getContentType(), metaData
        );

        PhotoDoc photoDoc = PhotoDoc.builder()
                .id(id)
                .title(file.getOriginalFilename())
                .contentType(file.getContentType())
                .build();
        photoRepository.save(photoDoc);
        return  photoDoc;
    }

    public Optional<PhotoDoc> findByID(ObjectId id){
        return photoRepository.findById(id);
    }

    public SearchResponse<PhotoDoc> search(
            SearchRequest request
    ) throws AuthException, NotAccessException {
        AccountDoc verification =authService.currentAccount();
        if(verification.getIsAdmin() == false) throw new NotAccessException();
        Criteria criteria = new Criteria();
        if(request.getQuery() != null && request.getQuery()!=""){
            criteria = criteria.orOperator(
                    Criteria.where("title").regex(request.getQuery(), "i")

            );
        }

        Query query = new Query(criteria);
        Long count = mongoTemplate.count(query, PhotoDoc.class);
        query.limit(request.getSize());
        query.skip(request.getSkip());

        List<PhotoDoc> photoDocs = mongoTemplate.find(query, PhotoDoc.class);
        return SearchResponse.of(photoDocs, count);
    }


    public void delete(ObjectId id) throws PhotoNotExistException,NotAccessException, AuthException {
         AccountDoc verification =authService.currentAccount();
         if(verification.getIsAdmin() == false) throw new NotAccessException();
         Optional<PhotoDoc> photoDocOptional = photoRepository.findById(id);
         if(photoDocOptional.isPresent() == false){
         throw new PhotoNotExistException();
         }
        gridFsTemplate.delete(new Query(Criteria.where("_id").is(id)));
        photoRepository.deleteById(id);
    }
}
