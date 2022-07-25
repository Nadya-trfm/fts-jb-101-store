package com.foodtech.store.product.routes;

import com.foodtech.store.base.routers.BaseApiRoutes;

public class ProductApiRoutes {
    public static final String ROOT = BaseApiRoutes.V1 +"/product";
    public static final String BY_ID = ROOT+"/{id}";
}
