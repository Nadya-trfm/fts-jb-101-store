package com.foodtech.store.auth.controller;

import com.foodtech.store.account.api.request.RegistrationRequest;
import com.foodtech.store.account.api.response.AccountResponse;
import com.foodtech.store.account.exceptions.AccountExistException;
import com.foodtech.store.account.exceptions.AccountNotExistException;
import com.foodtech.store.account.mapping.AccountMapping;
import com.foodtech.store.account.service.AccountApiService;
import com.foodtech.store.auth.api.request.AuthRequest;
import com.foodtech.store.auth.api.response.AuthResponse;
import com.foodtech.store.auth.exceptions.AuthException;
import com.foodtech.store.auth.routes.AuthRoutes;
import com.foodtech.store.auth.service.AuthService;
import com.foodtech.store.base.api.response.OkResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthController {
    private final AccountApiService accountApiService;
    private final AuthService authService;

    @PostMapping(AuthRoutes.REGISTRATION)
    @ApiOperation(value = "Register",notes="use this when you need registration and create new account," +
            " if account admin, add isAdmin:true ")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "Success"),
            @ApiResponse(code = 400,message = "User already exist")
    })
    public OkResponse<AccountResponse> registration(@RequestBody RegistrationRequest request) throws AccountExistException {
        return OkResponse.of(AccountMapping.getInstance().getResponse().convert(accountApiService.registration(request)));
    }

    @PostMapping(AuthRoutes.AUTH)
    @ApiOperation(value = "Auth",notes="get token")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "Success"),
            @ApiResponse(code = 400,message = "Account not exist"),
            @ApiResponse(code = 401,message = "Bad password")
    })
    public OkResponse<AuthResponse> auth(@RequestBody AuthRequest authRequest) throws AuthException, AccountNotExistException {
        return OkResponse.of(authService.auth(authRequest));
    }
}