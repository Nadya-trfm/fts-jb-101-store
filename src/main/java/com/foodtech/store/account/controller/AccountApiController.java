package com.foodtech.store.account.controller;

import com.foodtech.store.account.api.request.AccountRequest;
import com.foodtech.store.account.api.response.AccountResponse;
import com.foodtech.store.account.exceptions.AccountNotExistException;
import com.foodtech.store.account.mapping.AccountMapping;
import com.foodtech.store.account.routers.AccountApiRoutes;
import com.foodtech.store.account.service.AccountApiService;
import com.foodtech.store.auth.exceptions.AuthException;
import com.foodtech.store.auth.exceptions.NotAccessException;
import com.foodtech.store.base.api.request.SearchRequest;
import com.foodtech.store.base.api.response.OkResponse;
import com.foodtech.store.base.api.response.SearchResponse;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Api(value = "Account api")
public class AccountApiController {
    private final AccountApiService accountApiService;


    @GetMapping(AccountApiRoutes.BY_ID)
    @ApiOperation(value = "find account by id(for admin)/view your account (for user)",
            notes = "use this if you need full information by account")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 403, message = "no access rights"),
            @ApiResponse(code = 404, message = "account not found"),
    })
    public OkResponse<AccountResponse> byId(@ApiParam(value = "user id") @PathVariable ObjectId id) throws ChangeSetPersister.NotFoundException, NotAccessException, AuthException {
        return OkResponse.of(AccountMapping.getInstance().getResponse().convert(
                accountApiService.findByID(id).orElseThrow(ChangeSetPersister.NotFoundException::new)
        ));
    }

    @GetMapping(AccountApiRoutes.ROOT)
    @ApiOperation(value = "search account for admin only", notes = "use this if you need find account by name/email or search admins/users")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "no access rights")

    })
    public OkResponse<SearchResponse<AccountResponse>> search(
            @ModelAttribute SearchRequest request
    ) throws NotAccessException, AuthException {
        return OkResponse.of(AccountMapping.getInstance().getSearch().convert(
                accountApiService.search(request)
        ));
    }

    @PutMapping(AccountApiRoutes.BY_ID)
    @ApiOperation(value = "update account", notes = "use this if you need update account")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "account ID invalid"),
            @ApiResponse(code = 403, message = "no access rights")
    })
    public OkResponse<AccountResponse> updateById(
            @ApiParam(value = "account id") @PathVariable ObjectId id,
            @RequestBody AccountRequest accountRequest
    ) throws AccountNotExistException, AuthException, NotAccessException {
        return OkResponse.of(AccountMapping.getInstance().getResponse().convert(
                accountApiService.update(id, accountRequest)
        ));
    }

    @DeleteMapping(AccountApiRoutes.BY_ID)
    @ApiOperation(value = "delete account", notes = "use this if you need delete account")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "account ID invalid"),
            @ApiResponse(code = 403, message = "no access rights")
    })
    public OkResponse<String> deleteById(@ApiParam(value = "account id") @PathVariable ObjectId id) throws NotAccessException, AuthException {
        accountApiService.delete(id);
        return OkResponse.of(HttpStatus.OK.toString());
    }
}
