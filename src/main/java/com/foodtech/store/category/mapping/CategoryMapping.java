package

        com.foodtech.store.category.mapping;


import com.foodtech.store.base.api.response.SearchResponse;
import com.foodtech.store.base.mapping.BaseMapping;
import com.foodtech.store.category.api.request.CategoryRequest;
import com.foodtech.store.category.api.response.CategoryResponse;
import com.foodtech.store.category.model.CategoryDoc;
import lombok.Getter;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CategoryMapping{

    public static class RequestMapping {


        public CategoryDoc convert(CategoryRequest categoryRequest){
            return CategoryDoc.builder()
                .id(categoryRequest.getId())
                .title(categoryRequest.getTitle())
            .build();
            }

    }

    public static class ResponseMapping extends BaseMapping<CategoryDoc,CategoryResponse>{
        @Override
        public CategoryResponse convert(CategoryDoc categoryDoc){
            return CategoryResponse.builder()
                .id(categoryDoc.getId().toString())
                .title(categoryDoc.getTitle())
                .build();
            }


            }



    public static class SearchMapping extends BaseMapping<SearchResponse< CategoryDoc>,SearchResponse<CategoryResponse>>{
        private ResponseMapping responseMapping=new ResponseMapping();

        @Override
        public SearchResponse<CategoryResponse>convert(SearchResponse<CategoryDoc> searchResponse){
                return SearchResponse.of(
                searchResponse.getList().stream().map(responseMapping::convert).collect(Collectors.toList()),
                searchResponse.getCount()
                );

                }


                }

        private final RequestMapping request=new RequestMapping();
        private final ResponseMapping response=new ResponseMapping();
        private final SearchMapping search=new SearchMapping();

        public static CategoryMapping getInstance(){
            return new CategoryMapping();
            }
}

