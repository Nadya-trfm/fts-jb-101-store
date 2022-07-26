package

        com.foodtech.store.order.mapping;


import com.foodtech.store.base.api.response.SearchResponse;
import com.foodtech.store.base.mapping.BaseMapping;
import com.foodtech.store.order.api.request.CreateOrderRequest;
import com.foodtech.store.order.api.request.OrderRequest;
import com.foodtech.store.order.api.response.OrderResponse;
import com.foodtech.store.order.model.OrderDoc;
import com.foodtech.store.order.model.Payment;
import com.foodtech.store.order.model.Status;
import lombok.Getter;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class OrderMapping{

    public static class RequestMapping {


        public OrderDoc convert(OrderRequest orderRequest){
            return OrderDoc.builder()
                .id(orderRequest.getId())
                .payment(Payment.valueOf(orderRequest.getPayment()))
                .basketId(orderRequest.getBasketId())
                .addressId(orderRequest.getAddressId())
                .status(Status.valueOf(orderRequest.getStatus()))
                .timeToDelivery(orderRequest.getTimeToDelivery())
                .finalPrice(orderRequest.getFinalPrice())
            .build();
            }

    }

    public static class ResponseMapping extends BaseMapping<OrderDoc,OrderResponse>{
        @Override
        public OrderResponse convert(OrderDoc orderDoc){
            return OrderResponse.builder()
                .id(orderDoc.getId().toString())
                .payment(orderDoc.getPayment().toString())
                .basketId(orderDoc.getBasketId().toString())
                .addressId(orderDoc.getAddressId().toString())
                .status(orderDoc.getStatus().toString())
                .timeToDelivery(orderDoc.getTimeToDelivery())
                .finalPrice(orderDoc.getFinalPrice())
                .build();
            }


            }



    public static class SearchMapping extends BaseMapping<SearchResponse< OrderDoc>,SearchResponse<OrderResponse>>{
        private ResponseMapping responseMapping=new ResponseMapping();

        @Override
        public SearchResponse<OrderResponse>convert(SearchResponse<OrderDoc> searchResponse){
                return SearchResponse.of(
                searchResponse.getList().stream().map(responseMapping::convert).collect(Collectors.toList()),
                searchResponse.getCount()
                );

                }


                }

        private final RequestMapping request=new RequestMapping();

        private final ResponseMapping response=new ResponseMapping();
        private final SearchMapping search=new SearchMapping();

        public static OrderMapping getInstance(){
            return new OrderMapping();
            }
}

