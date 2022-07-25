package com.foodtech.store.category.controller;

import com.foodtech.store.auth.exceptions.AuthException;
import com.foodtech.store.auth.exceptions.NotAccessException;
import com.foodtech.store.base.api.request.SearchRequest;
import com.foodtech.store.base.api.response.OkResponse;
import com.foodtech.store.base.api.response.SearchResponse;
import com.foodtech.store.category.api.request.CategoryRequest;
import com.foodtech.store.category.api.response.CategoryResponse;
import com.foodtech.store.category.exeception.CategoryExistException;
import com.foodtech.store.category.exeception.CategoryNotExistException;
import com.foodtech.store.category.mapping.CategoryMapping;
import com.foodtech.store.category.routes.CategoryApiRoutes;
import com.foodtech.store.category.service.CategoryApiService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(value = "Category api")
public class CategoryApiController {
    private final CategoryApiService categoryApiService;

    @PostMapping(CategoryApiRoutes.ROOT)
    @ApiOperation(value = "Create (for admin)",notes="use this when you need create new Category")
    @ApiResponses(value = {
           @ApiResponse(code = 200,message = "Success"),
           @ApiResponse(code = 400,message = "Category already exist")
    })
    public OkResponse<CategoryResponse> create(@RequestBody CategoryRequest request) throws NotAccessException, AuthException, CategoryExistException {
        return OkResponse.of(CategoryMapping.getInstance().getResponse().convert(categoryApiService.create(request)));
    }

    @GetMapping(CategoryApiRoutes.BY_ID)
    @ApiOperation(value = "find Category by id",notes = "use this if you need full information by Category")
    @ApiResponses(value={
            @ApiResponse(code = 200,message = "Success"),
            @ApiResponse(code = 404,message = "Category not found"),
    })
    public OkResponse<CategoryResponse> byId( @ApiParam(value = "Category id")@PathVariable ObjectId id) throws ChangeSetPersister.NotFoundException {
    return  OkResponse.of(CategoryMapping.getInstance().getResponse().convert(
            categoryApiService.findByID(id).orElseThrow(ChangeSetPersister.NotFoundException::new)
    ));
    }
    @GetMapping(CategoryApiRoutes.ROOT)
    @ApiOperation(value = "search Category",notes = "use this if you need find Category by ????")
    @ApiResponses(value={
            @ApiResponse(code = 200,message = "Success")
    })
    public OkResponse<SearchResponse<CategoryResponse>> search(
            @ModelAttribute SearchRequest request
            ){
        return  OkResponse.of(CategoryMapping.getInstance().getSearch().convert(
                categoryApiService.search(request)
        ));
    }

    @PutMapping(CategoryApiRoutes.BY_ID)
    @ApiOperation(value = "update Category (for admin)",notes = "use this if you need update Category")
    @ApiResponses(value={
            @ApiResponse(code = 200,message = "Success"),
            @ApiResponse(code = 400,message = "Category ID invalid"),
    })
    public OkResponse<CategoryResponse> updateById(
            @ApiParam(value = "Category id") @PathVariable ObjectId id,
            @RequestBody CategoryRequest categoryRequest
            ) throws CategoryNotExistException, NotAccessException, AuthException{
        return OkResponse.of(CategoryMapping.getInstance().getResponse().convert(
                categoryApiService.update(id, categoryRequest)
        ));
    }

    @DeleteMapping(CategoryApiRoutes.BY_ID)
    @ApiOperation(value = "delete Category (for admin)",notes = "use this if you need delete Category")
    @ApiResponses(value={
            @ApiResponse(code = 200,message = "Success"),
            @ApiResponse(code = 400, message = "Category ID invalid"),
            @ApiResponse(code = 403, message = "no access rights")
    })
    public OkResponse<String> deleteById(@ApiParam(value = "Category id") @PathVariable ObjectId id)
        throws CategoryNotExistException, NotAccessException, AuthException{
        categoryApiService.delete(id);
        return OkResponse.of(HttpStatus.OK.toString());
    }
}
