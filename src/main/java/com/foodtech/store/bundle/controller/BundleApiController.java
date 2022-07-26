package com.foodtech.store.bundle.controller;

import com.foodtech.store.auth.exceptions.AuthException;
import com.foodtech.store.auth.exceptions.NotAccessException;
import com.foodtech.store.base.api.request.SearchRequest;
import com.foodtech.store.base.api.response.OkResponse;
import com.foodtech.store.base.api.response.SearchResponse;
import com.foodtech.store.bundle.api.request.BundleRequest;
import com.foodtech.store.bundle.api.response.BundleResponse;
import com.foodtech.store.bundle.exeception.BundleExistException;
import com.foodtech.store.bundle.exeception.BundleNotExistException;
import com.foodtech.store.bundle.mapping.BundleMapping;
import com.foodtech.store.bundle.routes.BundleApiRoutes;
import com.foodtech.store.bundle.service.BundleApiService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(value = "Bundle api")
public class BundleApiController {
    private final BundleApiService bundleApiService;

    @PostMapping(BundleApiRoutes.ROOT)
    @ApiOperation(value = "Create bundle",notes="use this when you need create new Bundle")
    @ApiResponses(value = {
           @ApiResponse(code = 200,message = "Success"),
           @ApiResponse(code = 400,message = "Bundle already exist")
    })
    public OkResponse<BundleResponse> create(@RequestBody BundleRequest request) {
        return OkResponse.of(BundleMapping.getInstance().getResponse().convert(bundleApiService.create(request)));
    }

    @GetMapping(BundleApiRoutes.ROOT)
    @ApiOperation(value = "search Bundle (for admin)",notes = "use this if you need  BundleId")
    @ApiResponses(value={
            @ApiResponse(code = 200,message = "Success"),
            @ApiResponse(code = 403, message = "no access rights")
    })
    public OkResponse<SearchResponse<BundleResponse>> search(
            @ModelAttribute SearchRequest request
    ) throws NotAccessException, AuthException {
        return  OkResponse.of(BundleMapping.getInstance().getSearch().convert(
                bundleApiService.search(request)
        ));
    }

    @PutMapping(BundleApiRoutes.BY_ID)
    @ApiOperation(value = "update Bundle (for admin)",notes = "use this if you need update Bundle")
    @ApiResponses(value={
            @ApiResponse(code = 200,message = "Success"),
            @ApiResponse(code = 400,message = "Bundle ID invalid"),
    })
    public OkResponse<BundleResponse> updateById(
            @ApiParam(value = "Bundle id") @PathVariable ObjectId id,
            @RequestBody BundleRequest bundleRequest
            ) throws BundleNotExistException {
        return OkResponse.of(BundleMapping.getInstance().getResponse().convert(
                bundleApiService.update(id, bundleRequest)
        ));
    }

    @DeleteMapping(BundleApiRoutes.BY_ID)
    @ApiOperation(value = "delete Bundle ",notes = "use this if you need delete Bundle")
    @ApiResponses(value={
            @ApiResponse(code = 200,message = "Success"),
            @ApiResponse(code = 400, message = "Bundle ID invalid")
    })
    public OkResponse<String> deleteById(@ApiParam(value = "Bundle id") @PathVariable ObjectId id)
        throws BundleNotExistException{
        bundleApiService.delete(id);
        return OkResponse.of(HttpStatus.OK.toString());
    }
}
