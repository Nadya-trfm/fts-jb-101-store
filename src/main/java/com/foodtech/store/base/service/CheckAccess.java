package com.foodtech.store.base.service;

import com.foodtech.store.account.model.AccountDoc;
import com.foodtech.store.auth.exceptions.AuthException;
import com.foodtech.store.auth.exceptions.NotAccessException;
import com.foodtech.store.auth.service.AuthService;
import org.bson.types.ObjectId;

public abstract class CheckAccess <T>{
    protected abstract ObjectId getOwnerFromEntity(T entity);

    protected AccountDoc checkAccess(T entity) throws AuthException, NotAccessException {
        ObjectId ownerId = getOwnerFromEntity(entity);

        AccountDoc owner = authService().currentAccount();
        if(owner.getId().equals(ownerId) == false) throw new NotAccessException();

        return owner;
    }

    protected abstract AuthService authService();
}
