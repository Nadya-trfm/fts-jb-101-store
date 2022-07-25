package com.foodtech.store.street.controller;

import com.foodtech.store.auth.exceptions.AuthException;
import com.foodtech.store.auth.exceptions.NotAccessException;
import com.foodtech.store.base.api.request.SearchRequest;
import com.foodtech.store.base.api.response.OkResponse;
import com.foodtech.store.base.api.response.SearchResponse;
import com.foodtech.store.street.api.request.StreetRequest;
import com.foodtech.store.street.api.response.StreetResponse;
import com.foodtech.store.street.exeception.StreetExistException;
import com.foodtech.store.street.exeception.StreetNotExistException;
import com.foodtech.store.street.mapping.StreetMapping;
import com.foodtech.store.street.routes.StreetApiRoutes;
import com.foodtech.store.street.service.StreetApiService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(value = "Street api")
public class StreetApiController {
    private final StreetApiService streetApiService;

    @PostMapping(StreetApiRoutes.ROOT)
    @ApiOperation(value = "Create (for admin)",notes="use this when you need create new Street")
    @ApiResponses(value = {
           @ApiResponse(code = 200,message = "Success"),
           @ApiResponse(code = 400,message = "Street already exist")
    })
    public OkResponse<StreetResponse> create(@RequestBody StreetRequest request) throws NotAccessException, AuthException {
        return OkResponse.of(StreetMapping.getInstance().getResponse().convert(streetApiService.create(request)));
    }

    @GetMapping(StreetApiRoutes.BY_ID)
    @ApiOperation(value = "find Street by id",notes = "use this if you need full information by Street")
    @ApiResponses(value={
            @ApiResponse(code = 200,message = "Success"),
            @ApiResponse(code = 404,message = "Street not found"),
    })
    public OkResponse<StreetResponse> byId( @ApiParam(value = "Street id")@PathVariable ObjectId id) throws ChangeSetPersister.NotFoundException {
    return  OkResponse.of(StreetMapping.getInstance().getResponse().convert(
            streetApiService.findByID(id).orElseThrow(ChangeSetPersister.NotFoundException::new)
    ));
    }
    @GetMapping(StreetApiRoutes.ROOT)
    @ApiOperation(value = "search Street",notes = "use this if you need find Street by ????")
    @ApiResponses(value={
            @ApiResponse(code = 200,message = "Success")
    })
    public OkResponse<SearchResponse<StreetResponse>> search(
            @ModelAttribute SearchRequest request
            ){
        return  OkResponse.of(StreetMapping.getInstance().getSearch().convert(
                streetApiService.search(request)
        ));
    }

    @PutMapping(StreetApiRoutes.BY_ID)
    @ApiOperation(value = "update Street (for admin)",notes = "use this if you need update Street")
    @ApiResponses(value={
            @ApiResponse(code = 200,message = "Success"),
            @ApiResponse(code = 400,message = "Street ID invalid"),
    })
    public OkResponse<StreetResponse> updateById(
            @ApiParam(value = "Street id") @PathVariable ObjectId id,
            @RequestBody StreetRequest streetRequest
            ) throws StreetNotExistException, NotAccessException, AuthException{
        return OkResponse.of(StreetMapping.getInstance().getResponse().convert(
                streetApiService.update(id, streetRequest)
        ));
    }

    @DeleteMapping(StreetApiRoutes.BY_ID)
    @ApiOperation(value = "delete Street (for admin)",notes = "use this if you need delete Street")
    @ApiResponses(value={
            @ApiResponse(code = 200,message = "Success"),
            @ApiResponse(code = 400, message = "Street ID invalid"),
            @ApiResponse(code = 403, message = "no access rights")
    })
    public OkResponse<String> deleteById(@ApiParam(value = "Street id") @PathVariable ObjectId id)
        throws StreetNotExistException, NotAccessException, AuthException{
        streetApiService.delete(id);
        return OkResponse.of(HttpStatus.OK.toString());
    }
}
