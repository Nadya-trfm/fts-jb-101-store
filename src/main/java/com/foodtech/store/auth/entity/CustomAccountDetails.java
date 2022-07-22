package com.foodtech.store.auth.entity;

import com.foodtech.store.account.model.AccountDoc;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomAccountDetails implements UserDetails {
    private String login;
    private String password;
    private Collection<? extends  GrantedAuthority> grantedAuthorities;

    public static CustomAccountDetails fromAccountEntityToCustomAccountDetails(AccountDoc accountDoc){
        CustomAccountDetails c =new CustomAccountDetails();
        c.login = accountDoc.getEmail();
        c.password = accountDoc.getPassword();
        return c;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
