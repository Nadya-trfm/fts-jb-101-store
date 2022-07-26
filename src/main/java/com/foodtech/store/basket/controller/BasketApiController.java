package com.foodtech.store.basket.controller;

import com.foodtech.store.auth.exceptions.AuthException;
import com.foodtech.store.auth.exceptions.NotAccessException;
import com.foodtech.store.base.api.request.SearchRequest;
import com.foodtech.store.base.api.response.OkResponse;
import com.foodtech.store.base.api.response.SearchResponse;
import com.foodtech.store.basket.api.request.BasketRequest;
import com.foodtech.store.basket.api.response.BasketResponse;
import com.foodtech.store.basket.exeception.BasketExistException;
import com.foodtech.store.basket.exeception.BasketNotExistException;
import com.foodtech.store.basket.mapping.BasketMapping;
import com.foodtech.store.basket.routes.BasketApiRoutes;
import com.foodtech.store.basket.service.BasketApiService;
import com.foodtech.store.bundle.exeception.BundleNotExistException;
import com.foodtech.store.user.exeception.UserNotExistException;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(value = "Basket api")
public class BasketApiController {
    private final BasketApiService basketApiService;

    @PostMapping(BasketApiRoutes.ROOT)
    @ApiOperation(value = "Create ",notes="use this when you need create new Basket")
    @ApiResponses(value = {
           @ApiResponse(code = 200,message = "Success")
    })
    public OkResponse<BasketResponse> create(@RequestBody BasketRequest request) throws NotAccessException, BundleNotExistException, UserNotExistException {
        return OkResponse.of(BasketMapping.getInstance().getResponse().convert(basketApiService.create(request)));
    }

    @GetMapping(BasketApiRoutes.BY_ID)
    @ApiOperation(value = "find Basket by id",notes = "use this if you need full information by Basket")
    @ApiResponses(value={
            @ApiResponse(code = 200,message = "Success"),
            @ApiResponse(code = 404,message = "Basket not found"),
    })
    public OkResponse<BasketResponse> byId( @ApiParam(value = "Basket id")@PathVariable ObjectId id) throws ChangeSetPersister.NotFoundException {
    return  OkResponse.of(BasketMapping.getInstance().getResponse().convert(
            basketApiService.findByID(id).orElseThrow(ChangeSetPersister.NotFoundException::new)
    ));
    }
    @GetMapping(BasketApiRoutes.ROOT)
    @ApiOperation(value = "search Basket(for admin)",notes = "use this if you need find Basket by ????")
    @ApiResponses(value={
            @ApiResponse(code = 200,message = "Success")
    })
    public OkResponse<SearchResponse<BasketResponse>> search(
            @ModelAttribute SearchRequest request
            ) throws NotAccessException, AuthException {
        return  OkResponse.of(BasketMapping.getInstance().getSearch().convert(
                basketApiService.search(request)
        ));
    }

    @PutMapping(BasketApiRoutes.BY_ID)
    @ApiOperation(value = "update Basket",notes = "use this if you need update Basket")
    @ApiResponses(value={
            @ApiResponse(code = 200,message = "Success"),
            @ApiResponse(code = 400,message = "Basket ID invalid"),
    })
    public OkResponse<BasketResponse> updateById(
            @ApiParam(value = "Basket id") @PathVariable ObjectId id,
            @RequestBody BasketRequest basketRequest
            ) throws BasketNotExistException, NotAccessException, BundleNotExistException {
        return OkResponse.of(BasketMapping.getInstance().getResponse().convert(
                basketApiService.update(id, basketRequest)
        ));
    }

    @DeleteMapping(BasketApiRoutes.BY_ID)
    @ApiOperation(value = "delete Basket ",notes = "use this if you need delete Basket")
    @ApiResponses(value={
            @ApiResponse(code = 200,message = "Success"),
            @ApiResponse(code = 400, message = "Basket ID invalid"),
            @ApiResponse(code = 403, message = "no access rights")
    })
    public OkResponse<String> deleteById(@ApiParam(value = "Basket id") @PathVariable ObjectId id)
        throws BasketNotExistException, NotAccessException, AuthException{
        basketApiService.delete(id);
        return OkResponse.of(HttpStatus.OK.toString());
    }
}
