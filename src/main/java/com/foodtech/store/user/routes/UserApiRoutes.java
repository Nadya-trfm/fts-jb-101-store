package com.foodtech.store.user.routes;

import com.foodtech.store.base.routers.BaseApiRoutes;

public class UserApiRoutes {
    public static final String ROOT = BaseApiRoutes.V1 +"/user";
    public static final String BY_ID = ROOT+"/{id}";
}
