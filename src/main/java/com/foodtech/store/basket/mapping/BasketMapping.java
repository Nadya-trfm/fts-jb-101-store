package

        com.foodtech.store.basket.mapping;


import com.foodtech.store.base.api.response.SearchResponse;
import com.foodtech.store.base.mapping.BaseMapping;
import com.foodtech.store.basket.api.request.BasketRequest;
import com.foodtech.store.basket.api.response.BasketResponse;
import com.foodtech.store.basket.model.BasketDoc;
import lombok.Getter;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class BasketMapping {

    public static class RequestMapping {


        public BasketDoc convert(BasketRequest basketRequest, ObjectId userId) {
            return BasketDoc.builder()
                    .id(basketRequest.getId())
                    .userId(userId)
                    .bundlesId(basketRequest.getBundlesId())
                    .build();
        }

    }

    public static class ResponseMapping extends BaseMapping<BasketDoc, BasketResponse> {
        @Override
        public BasketResponse convert(BasketDoc basketDoc) {
            return BasketResponse.builder()
                    .id(basketDoc.getId().toString())
                    .userId(basketDoc.getUserId().toString())
                    .bundlesId(basketDoc.getBundlesId().stream().map(ObjectId::toString).collect(Collectors.toList()))
                    .build();
        }


    }


    public static class SearchMapping extends BaseMapping<SearchResponse<BasketDoc>, SearchResponse<BasketResponse>> {
        private ResponseMapping responseMapping = new ResponseMapping();

        @Override
        public SearchResponse<BasketResponse> convert(SearchResponse<BasketDoc> searchResponse) {
            return SearchResponse.of(
                    searchResponse.getList().stream().map(responseMapping::convert).collect(Collectors.toList()),
                    searchResponse.getCount()
            );

        }


    }

    private final RequestMapping request = new RequestMapping();
    private final ResponseMapping response = new ResponseMapping();
    private final SearchMapping search = new SearchMapping();

    public static BasketMapping getInstance() {
        return new BasketMapping();
    }
}

