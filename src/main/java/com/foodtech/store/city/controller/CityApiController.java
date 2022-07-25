package com.foodtech.store.city.controller;

import com.foodtech.store.auth.exceptions.AuthException;
import com.foodtech.store.auth.exceptions.NotAccessException;
import com.foodtech.store.base.api.request.SearchRequest;
import com.foodtech.store.base.api.response.OkResponse;
import com.foodtech.store.base.api.response.SearchResponse;
import com.foodtech.store.city.api.request.CityRequest;
import com.foodtech.store.city.api.response.CityResponse;
import com.foodtech.store.city.exeception.CityExistException;
import com.foodtech.store.city.exeception.CityNotExistException;
import com.foodtech.store.city.mapping.CityMapping;
import com.foodtech.store.city.routes.CityApiRoutes;
import com.foodtech.store.city.service.CityApiService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(value = "City api")
public class CityApiController {
    private final CityApiService cityApiService;

    @PostMapping(CityApiRoutes.ROOT)
    @ApiOperation(value = "Create",notes="use this when you need create new City")
    @ApiResponses(value = {
           @ApiResponse(code = 200,message = "Success"),
           @ApiResponse(code = 400,message = "City already exist")
    })
    public OkResponse<CityResponse> create(@RequestBody CityRequest request) throws  NotAccessException, AuthException {
        return OkResponse.of(CityMapping.getInstance().getResponse().convert(cityApiService.create(request)));
    }

    @GetMapping(CityApiRoutes.BY_ID)
    @ApiOperation(value = "find City by id",notes = "use this if you need full information by City")
    @ApiResponses(value={
            @ApiResponse(code = 200,message = "Success"),
            @ApiResponse(code = 404,message = "City not found"),
    })
    public OkResponse<CityResponse> byId( @ApiParam(value = "City id")@PathVariable ObjectId id) throws ChangeSetPersister.NotFoundException {
    return  OkResponse.of(CityMapping.getInstance().getResponse().convert(
            cityApiService.findByID(id).orElseThrow(ChangeSetPersister.NotFoundException::new)
    ));
    }
    @GetMapping(CityApiRoutes.ROOT)
    @ApiOperation(value = "search City",notes = "use this if you need find City by ????")
    @ApiResponses(value={
            @ApiResponse(code = 200,message = "Success")
    })
    public OkResponse<SearchResponse<CityResponse>> search(
            @ModelAttribute SearchRequest request
            ){
        return  OkResponse.of(CityMapping.getInstance().getSearch().convert(
                cityApiService.search(request)
        ));
    }

    @PutMapping(CityApiRoutes.BY_ID)
    @ApiOperation(value = "update City (for admin)",notes = "use this if you need update City")
    @ApiResponses(value={
            @ApiResponse(code = 200,message = "Success"),
            @ApiResponse(code = 400,message = "City ID invalid"),
    })
    public OkResponse<CityResponse> updateById(
            @ApiParam(value = "City id(for admin)") @PathVariable ObjectId id,
            @RequestBody CityRequest cityRequest
            ) throws CityNotExistException, NotAccessException, AuthException {
        return OkResponse.of(CityMapping.getInstance().getResponse().convert(
                cityApiService.update(id, cityRequest)
        ));
    }

    @DeleteMapping(CityApiRoutes.BY_ID)
    @ApiOperation(value = "delete City (for admin)",notes = "use this if you need delete City")
    @ApiResponses(value={
            @ApiResponse(code = 200,message = "Success"),
            @ApiResponse(code = 400, message = "city ID invalid"),
            @ApiResponse(code = 403, message = "no access rights")
    })
    public OkResponse<String> deleteById(@ApiParam(value = "City id") @PathVariable ObjectId id) throws CityNotExistException, NotAccessException, AuthException {
        cityApiService.delete(id);
        return OkResponse.of(HttpStatus.OK.toString());
    }
}
