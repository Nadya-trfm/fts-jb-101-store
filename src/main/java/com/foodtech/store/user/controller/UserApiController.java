package com.foodtech.store.user.controller;

import com.foodtech.store.auth.exceptions.AuthException;
import com.foodtech.store.auth.exceptions.NotAccessException;
import com.foodtech.store.base.api.request.SearchRequest;
import com.foodtech.store.base.api.response.OkResponse;
import com.foodtech.store.base.api.response.SearchResponse;
import com.foodtech.store.user.api.request.UserRequest;
import com.foodtech.store.user.api.response.UserResponse;
import com.foodtech.store.user.exeception.UserExistException;
import com.foodtech.store.user.exeception.UserNotExistException;
import com.foodtech.store.user.mapping.UserMapping;
import com.foodtech.store.user.routes.UserApiRoutes;
import com.foodtech.store.user.service.UserApiService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(value = "User api")
public class UserApiController {
    private final UserApiService userApiService;

    @PostMapping(UserApiRoutes.ROOT)
    @ApiOperation(value = "Create user",notes="use this when you need create new User (with/without account)")
    @ApiResponses(value = {
           @ApiResponse(code = 200,message = "Success")

    })
    public OkResponse<UserResponse> create(@RequestBody UserRequest request) {
        return OkResponse.of(UserMapping.getInstance().getResponse().convert(userApiService.create(request)));
    }

    @GetMapping(UserApiRoutes.BY_ID)
    @ApiOperation(value = "find User by id(for admin)",notes = "use this if you need full information by User")
    @ApiResponses(value={
            @ApiResponse(code = 200,message = "Success"),
            @ApiResponse(code = 404,message = "User not found"),
    })
    public OkResponse<UserResponse> byId( @ApiParam(value = "User id")@PathVariable ObjectId id) throws ChangeSetPersister.NotFoundException, NotAccessException, AuthException {
    return  OkResponse.of(UserMapping.getInstance().getResponse().convert(
            userApiService.findByID(id).orElseThrow(ChangeSetPersister.NotFoundException::new)
    ));
    }
    @GetMapping(UserApiRoutes.ROOT)
    @ApiOperation(value = "search User(for admin)",notes = "use this if you need find User by name/phone")
    @ApiResponses(value={
            @ApiResponse(code = 200,message = "Success")
    })
    public OkResponse<SearchResponse<UserResponse>> search(
            @ModelAttribute SearchRequest request
            ) throws NotAccessException, AuthException {
        return  OkResponse.of(UserMapping.getInstance().getSearch().convert(
                userApiService.search(request)
        ));
    }



    @DeleteMapping(UserApiRoutes.BY_ID)
    @ApiOperation(value = "delete User (for admin)",notes = "use this if you need delete User")
    @ApiResponses(value={
            @ApiResponse(code = 200,message = "Success"),
            @ApiResponse(code = 400, message = "User ID invalid"),
            @ApiResponse(code = 403, message = "no access rights")
    })
    public OkResponse<String> deleteById(@ApiParam(value = "User id") @PathVariable ObjectId id)
        throws UserNotExistException, NotAccessException, AuthException{
        userApiService.delete(id);
        return OkResponse.of(HttpStatus.OK.toString());
    }
}
