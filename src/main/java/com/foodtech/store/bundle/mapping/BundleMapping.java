package

        com.foodtech.store.bundle.mapping;


import com.foodtech.store.base.api.response.SearchResponse;
import com.foodtech.store.base.mapping.BaseMapping;
import com.foodtech.store.bundle.api.request.BundleRequest;
import com.foodtech.store.bundle.api.response.BundleResponse;
import com.foodtech.store.bundle.model.BundleDoc;
import lombok.Getter;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class BundleMapping{

    public static class RequestMapping {


        public BundleDoc convert(BundleRequest bundleRequest){
            return BundleDoc.builder()
                .id(bundleRequest.getId())
                .productId(bundleRequest.getProductId())
                .quantity(bundleRequest.getQuantity())
            .build();
            }

    }

    public static class ResponseMapping extends BaseMapping<BundleDoc,BundleResponse>{
        @Override
        public BundleResponse convert(BundleDoc bundleDoc){
            return BundleResponse.builder()
                .id(bundleDoc.getId().toString())
                .productId(bundleDoc.getProductId().toString())
                .quantity(bundleDoc.getQuantity())
                .build();
            }


            }



    public static class SearchMapping extends BaseMapping<SearchResponse< BundleDoc>,SearchResponse<BundleResponse>>{
        private ResponseMapping responseMapping=new ResponseMapping();

        @Override
        public SearchResponse<BundleResponse>convert(SearchResponse<BundleDoc> searchResponse){
                return SearchResponse.of(
                searchResponse.getList().stream().map(responseMapping::convert).collect(Collectors.toList()),
                searchResponse.getCount()
                );

                }


                }

        private final RequestMapping request=new RequestMapping();
        private final ResponseMapping response=new ResponseMapping();
        private final SearchMapping search=new SearchMapping();

        public static BundleMapping getInstance(){
            return new BundleMapping();
            }
}

