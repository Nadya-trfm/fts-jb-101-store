 package com.foodtech.store.order.service;

import com.foodtech.store.account.model.AccountDoc;
import com.foodtech.store.address.exeception.AddressNotExistException;
import com.foodtech.store.address.model.AddressDoc;
import com.foodtech.store.address.repository.AddressRepository;
import com.foodtech.store.auth.exceptions.AuthException;
import com.foodtech.store.auth.exceptions.NotAccessException;
import com.foodtech.store.auth.service.AuthService;
import com.foodtech.store.base.api.request.SearchRequest;
import com.foodtech.store.base.api.response.SearchResponse;
import com.foodtech.store.basket.exeception.BasketNotExistException;
import com.foodtech.store.basket.model.BasketDoc;
import com.foodtech.store.basket.repository.BasketRepository;
import com.foodtech.store.bundle.model.BundleDoc;
import com.foodtech.store.bundle.repository.BundleRepository;
import com.foodtech.store.order.api.request.CreateOrderRequest;
import com.foodtech.store.order.api.request.OrderRequest;
import com.foodtech.store.order.mapping.OrderMapping;
import com.foodtech.store.order.exeception.OrderNotExistException;
import com.foodtech.store.order.model.OrderDoc;
import com.foodtech.store.order.model.Payment;
import com.foodtech.store.order.model.Status;
import com.foodtech.store.order.repository.OrderRepository;
import com.foodtech.store.product.model.ProductDoc;
import com.foodtech.store.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderApiService {
    private final OrderRepository orderRepository;
    private final AuthService authService;
    private final MongoTemplate mongoTemplate;
    private final BasketRepository basketRepository;
    private final AddressRepository addressRepository;
    private final BundleRepository bundleRepository;
    private final ProductRepository productRepository;

    public OrderDoc create(CreateOrderRequest request) throws BasketNotExistException, AddressNotExistException {
        System.out.println(request);
       Optional<BasketDoc> basketDocOptional = basketRepository.findById(request.getBasketId());
       if(basketDocOptional.isPresent() == false){
           throw new BasketNotExistException();
       }
       Optional<AddressDoc> addressDocOptional = addressRepository.findById(request.getAddressId());
       if(addressDocOptional.isPresent() == false){
           throw  new AddressNotExistException();
       }
       Integer finalPrice=0;
       BasketDoc basketDoc = basketDocOptional.get();
      List<ObjectId> bundlesId = basketDoc.getBundlesId();
      for(ObjectId bundleId: bundlesId){
          Optional<BundleDoc> bundleDocOptional = bundleRepository.findById(bundleId);
          if(bundleDocOptional.isPresent() == false){
              bundlesId.remove(bundleId);
              continue;
          }
            BundleDoc bundleDoc = bundleDocOptional.get();
          Integer quantity = bundleDoc.getQuantity();
          ObjectId productId = bundleDoc.getProductId();
          Optional<ProductDoc> productDocOptional = productRepository.findById(productId);
          if(productDocOptional.isPresent() == false){
              bundlesId.remove(bundleId);
              continue;
          }
          ProductDoc productDoc = productDocOptional.get();
          Integer productPrice = productDoc.getPrice();
          finalPrice += productPrice*quantity;
      }
        OrderDoc orderDoc =OrderDoc.builder()
                        .payment(Payment.valueOf(request.getPayment()))
                                .basketId(request.getBasketId())
                                        .addressId(request.getBasketId())
                                                .status(Status.valueOf("IN_PROCESSING"))
                                                        .timeToDelivery(request.getTimeToDelivery())
                                                                .finalPrice(finalPrice)
                                                                        .build();
        orderRepository.save(orderDoc);
        return  orderDoc;
    }

    public Optional<OrderDoc> findByID(ObjectId id){
        return orderRepository.findById(id);
    }
    public SearchResponse<OrderDoc> search(
             SearchRequest request
    ) throws AuthException, NotAccessException {
        AccountDoc verification =authService.currentAccount();
        if(verification.getIsAdmin() == false) throw new NotAccessException();
        Criteria criteria = new Criteria();
        if(request.getQuery() != null && request.getQuery()!=""){
            criteria = criteria.orOperator(
                    Criteria.where("status").regex(request.getQuery(), "i"),
                    Criteria.where("basketId").regex(request.getQuery(), "i"),
                    Criteria.where("addressId").regex(request.getQuery(), "i"),
                    Criteria.where("timeToDelivery").regex(request.getQuery(), "i")

            );
        }

        Query query = new Query(criteria);
        Long count = mongoTemplate.count(query, OrderDoc.class);
        query.limit(request.getSize());
        query.skip(request.getSkip());

        List<OrderDoc> orderDocs = mongoTemplate.find(query, OrderDoc.class);
        return SearchResponse.of(orderDocs, count);
    }

    public OrderDoc update(ObjectId id, OrderRequest request) throws OrderNotExistException,NotAccessException, AuthException  {
         AccountDoc verification =authService.currentAccount();
         if(verification.getIsAdmin() == false) throw new NotAccessException();
        Optional<OrderDoc> orderDocOptional = orderRepository.findById(id);
        if(orderDocOptional.isPresent() == false){
            throw new OrderNotExistException();
        }
        OrderDoc orderDoc = orderDocOptional.get();
        orderDoc.setId(id);
        orderDoc.setStatus(Status.valueOf(request.getStatus()));
        orderRepository.save(orderDoc);

        return orderDoc;
    }

    public void delete(ObjectId id) throws OrderNotExistException,NotAccessException, AuthException {
         AccountDoc verification =authService.currentAccount();
         if(verification.getIsAdmin() == false) throw new NotAccessException();
         Optional<OrderDoc> orderDocOptional = orderRepository.findById(id);
         if(orderDocOptional.isPresent() == false){
         throw new OrderNotExistException();
         }
        orderRepository.deleteById(id);
    }
}
