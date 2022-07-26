package com.foodtech.store.order.controller;

import com.foodtech.store.address.exeception.AddressNotExistException;
import com.foodtech.store.auth.exceptions.AuthException;
import com.foodtech.store.auth.exceptions.NotAccessException;
import com.foodtech.store.base.api.request.SearchRequest;
import com.foodtech.store.base.api.response.OkResponse;
import com.foodtech.store.base.api.response.SearchResponse;
import com.foodtech.store.basket.exeception.BasketNotExistException;
import com.foodtech.store.order.api.request.CreateOrderRequest;
import com.foodtech.store.order.api.request.OrderRequest;
import com.foodtech.store.order.api.response.OrderResponse;
import com.foodtech.store.order.exeception.OrderExistException;
import com.foodtech.store.order.exeception.OrderNotExistException;
import com.foodtech.store.order.mapping.OrderMapping;
import com.foodtech.store.order.routes.OrderApiRoutes;
import com.foodtech.store.order.service.OrderApiService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(value = "Order api")
public class OrderApiController {
    private final OrderApiService orderApiService;

    @PostMapping(OrderApiRoutes.ROOT)
    @ApiOperation(value = "Create",notes="use this when you need create new Order")
    @ApiResponses(value = {
           @ApiResponse(code = 200,message = "Success")
    })
    public OkResponse<OrderResponse> create(@RequestBody CreateOrderRequest request) throws BasketNotExistException, AddressNotExistException {
        return OkResponse.of(OrderMapping.getInstance().getResponse().convert(orderApiService.create(request)));
    }

    @GetMapping(OrderApiRoutes.BY_ID)
    @ApiOperation(value = "find Order by id",notes = "use this if you need full information by Order")
    @ApiResponses(value={
            @ApiResponse(code = 200,message = "Success"),
            @ApiResponse(code = 404,message = "Order not found"),
    })
    public OkResponse<OrderResponse> byId( @ApiParam(value = "Order id")@PathVariable ObjectId id) throws ChangeSetPersister.NotFoundException {
    return  OkResponse.of(OrderMapping.getInstance().getResponse().convert(
            orderApiService.findByID(id).orElseThrow(ChangeSetPersister.NotFoundException::new)
    ));
    }
    @GetMapping(OrderApiRoutes.ROOT)
    @ApiOperation(value = "search Order (for admin)",notes = "use this if you need find Order by status/basket id/address id/ time to delivery")
    @ApiResponses(value={
            @ApiResponse(code = 200,message = "Success")
    })
    public OkResponse<SearchResponse<OrderResponse>> search(
            @ModelAttribute SearchRequest request
            ) throws NotAccessException, AuthException {
        return  OkResponse.of(OrderMapping.getInstance().getSearch().convert(
                orderApiService.search(request)
        ));
    }

    @PutMapping(OrderApiRoutes.BY_ID)
    @ApiOperation(value = "update Order (for admin)",notes = "use this if you need update Order")
    @ApiResponses(value={
            @ApiResponse(code = 200,message = "Success"),
            @ApiResponse(code = 400,message = "Order ID invalid"),
    })
    public OkResponse<OrderResponse> updateById(
            @ApiParam(value = "Order id") @PathVariable ObjectId id,
            @RequestBody OrderRequest orderRequest
            ) throws OrderNotExistException, NotAccessException, AuthException {
        return OkResponse.of(OrderMapping.getInstance().getResponse().convert(
                orderApiService.update(id, orderRequest)
        ));
    }

    @DeleteMapping(OrderApiRoutes.BY_ID)
    @ApiOperation(value = "delete Order (for admin)",notes = "use this if you need delete Order")
    @ApiResponses(value={
            @ApiResponse(code = 200,message = "Success"),
            @ApiResponse(code = 400, message = "Order ID invalid"),
            @ApiResponse(code = 403, message = "no access rights")
    })
    public OkResponse<String> deleteById(@ApiParam(value = "Order id") @PathVariable ObjectId id)
        throws OrderNotExistException, NotAccessException, AuthException{
        orderApiService.delete(id);
        return OkResponse.of(HttpStatus.OK.toString());
    }
}
