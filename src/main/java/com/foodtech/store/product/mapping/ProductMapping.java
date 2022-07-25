package

        com.foodtech.store.product.mapping;


import com.foodtech.store.base.api.response.SearchResponse;
import com.foodtech.store.base.mapping.BaseMapping;
import com.foodtech.store.product.api.request.ProductRequest;
import com.foodtech.store.product.api.response.ProductResponse;
import com.foodtech.store.product.model.ProductDoc;
import lombok.Getter;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ProductMapping{

    public static class RequestMapping {


        public ProductDoc convert(ProductRequest productRequest){
            return ProductDoc.builder()
                .id(productRequest.getId())
                .title(productRequest.getTitle())
                .description(productRequest.getDescription())
                .photoId(productRequest.getPhotoId())
                .price(productRequest.getPrice())
                .categoryId(productRequest.getCategoryId())
                .proteins(Float.parseFloat(productRequest.getProteins()))
                .fats(Float.parseFloat(productRequest.getFats()))
                .carbohydrates(Float.parseFloat(productRequest.getCarbohydrates()))
                .kcal(Float.parseFloat(productRequest.getKcal()))
            .build();
            }

    }

    public static class ResponseMapping extends BaseMapping<ProductDoc,ProductResponse>{
        @Override
        public ProductResponse convert(ProductDoc productDoc){
            return ProductResponse.builder()
                .id(productDoc.getId().toString())
                .title(productDoc.getTitle())
                .description(productDoc.getDescription())
                .photoId(productDoc.getPhotoId().toString())
                .price(productDoc.getPrice())
                .categoryId(productDoc.getCategoryId().toString())
                .proteins(productDoc.getProteins())
                .fats(productDoc.getFats())
                .carbohydrates(productDoc.getCarbohydrates())
                .kcal(productDoc.getKcal())
                .build();
            }


            }



    public static class SearchMapping extends BaseMapping<SearchResponse< ProductDoc>,SearchResponse<ProductResponse>>{
        private ResponseMapping responseMapping=new ResponseMapping();

        @Override
        public SearchResponse<ProductResponse>convert(SearchResponse<ProductDoc> searchResponse){
                return SearchResponse.of(
                searchResponse.getList().stream().map(responseMapping::convert).collect(Collectors.toList()),
                searchResponse.getCount()
                );

                }


                }

        private final RequestMapping request=new RequestMapping();
        private final ResponseMapping response=new ResponseMapping();
        private final SearchMapping search=new SearchMapping();

        public static ProductMapping getInstance(){
            return new ProductMapping();
            }
}

