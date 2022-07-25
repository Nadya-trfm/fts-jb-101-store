package com.foodtech.store.product.controller;

import com.foodtech.store.auth.exceptions.AuthException;
import com.foodtech.store.auth.exceptions.NotAccessException;
import com.foodtech.store.base.api.request.SearchRequest;
import com.foodtech.store.base.api.response.OkResponse;
import com.foodtech.store.base.api.response.SearchResponse;
import com.foodtech.store.category.exeception.CategoryNotExistException;
import com.foodtech.store.photo.exeception.PhotoNotExistException;
import com.foodtech.store.product.api.request.ProductRequest;
import com.foodtech.store.product.api.response.ProductResponse;
import com.foodtech.store.product.exeception.ProductExistException;
import com.foodtech.store.product.exeception.ProductNotExistException;
import com.foodtech.store.product.mapping.ProductMapping;
import com.foodtech.store.product.routes.ProductApiRoutes;
import com.foodtech.store.product.service.ProductApiService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(value = "Product api")
public class ProductApiController {
    private final ProductApiService productApiService;

    @PostMapping(ProductApiRoutes.ROOT)
    @ApiOperation(value = "Create (for admin)",notes="use this when you need create new Product")
    @ApiResponses(value = {
           @ApiResponse(code = 200,message = "Success"),
    })
    public OkResponse<ProductResponse> create(@RequestBody ProductRequest request) throws NotAccessException, AuthException, PhotoNotExistException, CategoryNotExistException {
        return OkResponse.of(ProductMapping.getInstance().getResponse().convert(productApiService.create(request)));
    }

    @GetMapping(ProductApiRoutes.BY_ID)
    @ApiOperation(value = "find Product by id",notes = "use this if you need full information by Product")
    @ApiResponses(value={
            @ApiResponse(code = 200,message = "Success"),
            @ApiResponse(code = 404,message = "Product not found"),
    })
    public OkResponse<ProductResponse> byId( @ApiParam(value = "Product id")@PathVariable ObjectId id) throws ChangeSetPersister.NotFoundException {
    return  OkResponse.of(ProductMapping.getInstance().getResponse().convert(
            productApiService.findByID(id).orElseThrow(ChangeSetPersister.NotFoundException::new)
    ));
    }
    @GetMapping(ProductApiRoutes.ROOT)
    @ApiOperation(value = "search Product",notes = "use this if you need find Product by ????")
    @ApiResponses(value={
            @ApiResponse(code = 200,message = "Success")
    })
    public OkResponse<SearchResponse<ProductResponse>> search(
            @ModelAttribute SearchRequest request
            ){
        return  OkResponse.of(ProductMapping.getInstance().getSearch().convert(
                productApiService.search(request)
        ));
    }

    @PutMapping(ProductApiRoutes.BY_ID)
    @ApiOperation(value = "update Product (for admin)",notes = "use this if you need update Product")
    @ApiResponses(value={
            @ApiResponse(code = 200,message = "Success"),
            @ApiResponse(code = 400,message = "Product ID invalid"),
    })
    public OkResponse<ProductResponse> updateById(
            @ApiParam(value = "Product id") @PathVariable ObjectId id,
            @RequestBody ProductRequest productRequest
            ) throws ProductNotExistException, NotAccessException, AuthException, PhotoNotExistException, CategoryNotExistException {
        return OkResponse.of(ProductMapping.getInstance().getResponse().convert(
                productApiService.update(id, productRequest)
        ));
    }

    @DeleteMapping(ProductApiRoutes.BY_ID)
    @ApiOperation(value = "delete Product (for admin)",notes = "use this if you need delete Product")
    @ApiResponses(value={
            @ApiResponse(code = 200,message = "Success"),
            @ApiResponse(code = 400, message = "Product ID invalid"),
            @ApiResponse(code = 403, message = "no access rights")
    })
    public OkResponse<String> deleteById(@ApiParam(value = "Product id") @PathVariable ObjectId id)
        throws ProductNotExistException, NotAccessException, AuthException{
        productApiService.delete(id);
        return OkResponse.of(HttpStatus.OK.toString());
    }
}
