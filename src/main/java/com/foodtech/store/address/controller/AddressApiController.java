package com.foodtech.store.address.controller;

import com.foodtech.store.auth.exceptions.AuthException;
import com.foodtech.store.auth.exceptions.NotAccessException;
import com.foodtech.store.base.api.request.SearchRequest;
import com.foodtech.store.base.api.response.OkResponse;
import com.foodtech.store.base.api.response.SearchResponse;
import com.foodtech.store.address.api.request.AddressRequest;
import com.foodtech.store.address.api.response.AddressResponse;
import com.foodtech.store.address.exeception.AddressExistException;
import com.foodtech.store.address.exeception.AddressNotExistException;
import com.foodtech.store.address.mapping.AddressMapping;
import com.foodtech.store.address.routes.AddressApiRoutes;
import com.foodtech.store.address.service.AddressApiService;
import com.foodtech.store.street.exeception.StreetNotExistException;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(value = "Address api")
public class AddressApiController {
    private final AddressApiService addressApiService;

    @PostMapping(AddressApiRoutes.ROOT)
    @ApiOperation(value = "Create ",notes="use this when you need create new Address for order")
    @ApiResponses(value = {
           @ApiResponse(code = 200,message = "Success"),
           @ApiResponse(code = 400,message = "Address already exist"),
            @ApiResponse(code = 403, message = "no access rights")
    })
    public OkResponse<AddressResponse> create(@RequestBody AddressRequest request) throws NotAccessException, AuthException, StreetNotExistException {
        return OkResponse.of(AddressMapping.getInstance().getResponse().convert(addressApiService.create(request)));
    }

    @GetMapping(AddressApiRoutes.BY_ID)
    @ApiOperation(value = "find Address by id",notes = "use this if you need full information by Address")
    @ApiResponses(value={
            @ApiResponse(code = 200,message = "Success"),
            @ApiResponse(code = 404,message = "Address not found"),
    })
    public OkResponse<AddressResponse> byId( @ApiParam(value = "Address id")@PathVariable ObjectId id) throws ChangeSetPersister.NotFoundException {
    return  OkResponse.of(AddressMapping.getInstance().getResponse().convert(
            addressApiService.findByID(id).orElseThrow(ChangeSetPersister.NotFoundException::new)
    ));
    }
    @GetMapping(AddressApiRoutes.ROOT)
    @ApiOperation(value = "search Address (for admin)",notes = "use this if you need find Address by streetId")
    @ApiResponses(value={
            @ApiResponse(code = 200,message = "Success")
    })
    public OkResponse<SearchResponse<AddressResponse>> search(
            @ModelAttribute SearchRequest request
            ) throws NotAccessException, AuthException {
        return  OkResponse.of(AddressMapping.getInstance().getSearch().convert(
                addressApiService.search(request)
        ));
    }


    @DeleteMapping(AddressApiRoutes.BY_ID)
    @ApiOperation(value = "delete Address (for admin)",notes = "use this if you need delete Address")
    @ApiResponses(value={
            @ApiResponse(code = 200,message = "Success"),
            @ApiResponse(code = 400, message = "Address ID invalid"),
            @ApiResponse(code = 403, message = "no access rights")
    })
    public OkResponse<String> deleteById(@ApiParam(value = "Address id") @PathVariable ObjectId id)
            throws AddressNotExistException, NotAccessException, AuthException, AuthException {
        addressApiService.delete(id);
        return OkResponse.of(HttpStatus.OK.toString());
    }
}
