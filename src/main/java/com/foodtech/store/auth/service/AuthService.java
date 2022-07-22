package com.foodtech.store.auth.service;

import com.foodtech.store.account.exceptions.AccountNotExistException;
import com.foodtech.store.account.model.AccountDoc;
import com.foodtech.store.account.repository.AccountRepository;
import com.foodtech.store.auth.api.request.AuthRequest;
import com.foodtech.store.auth.entity.CustomAccountDetails;
import com.foodtech.store.auth.exceptions.AuthException;
import com.foodtech.store.security.JwtFilter;
import com.foodtech.store.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
@RequiredArgsConstructor
public class AuthService {
    private final AccountRepository accountRepository;
    private final JwtProvider jwtProvider;


    public CustomAccountDetails loadAccountByEmail(String email) throws AccountNotExistException {
        AccountDoc accountDoc = accountRepository.findByEmail(email).orElseThrow(AccountNotExistException::new);
        return CustomAccountDetails.fromAccountEntityToCustomAccountDetails(accountDoc);
    }

    public String auth(AuthRequest authRequest) throws AccountNotExistException, AuthException {
        AccountDoc accountDoc = accountRepository.findByEmail(authRequest.getEmail()).orElseThrow(AccountNotExistException::new);
        if(accountDoc.getPassword().equals(AccountDoc.hexPassword(authRequest.getPassword())) == false){
            accountRepository.save(accountDoc);
            throw new AuthException();
        }


        String token = jwtProvider.generateToken(authRequest.getEmail());
        return token;
    }

    public static HttpServletRequest getCurrentHttpRequest(){
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if( requestAttributes instanceof ServletRequestAttributes){
            HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();
            return request;
        }
        return null;
    }

    public AccountDoc currentAccount() throws AuthException {
        try {
            String email = jwtProvider.getEmailFromToken(JwtFilter.getTokenFromRequest(getCurrentHttpRequest()));
            AccountDoc accountDoc=accountRepository.findByEmail(email).orElseThrow(AccountNotExistException::new);
            return accountDoc;
        }catch (Exception e){
            throw new AuthException();
        }
    }

    public AccountDoc adminAccount() throws AuthException {
        try {
            String email = jwtProvider.getEmailFromToken(JwtFilter.getTokenFromRequest(getCurrentHttpRequest()));
            AccountDoc accountDoc=accountRepository.findByEmail(email).orElseThrow(AccountNotExistException::new);
            return accountDoc;
        }catch (Exception e){
            throw new AuthException();
        }
    }
}
