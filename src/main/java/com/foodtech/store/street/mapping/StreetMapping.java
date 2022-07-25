package

        com.foodtech.store.street.mapping;


import com.foodtech.store.base.api.response.SearchResponse;
import com.foodtech.store.base.mapping.BaseMapping;
import com.foodtech.store.street.api.request.StreetRequest;
import com.foodtech.store.street.api.response.StreetResponse;
import com.foodtech.store.street.model.StreetDoc;
import lombok.Getter;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class StreetMapping{

    public static class RequestMapping {


        public StreetDoc convert(StreetRequest streetRequest){
            return StreetDoc.builder()
                .id(streetRequest.getId())
                .title(streetRequest.getTitle())
                .cityId(streetRequest.getCityId())
            .build();
            }

    }

    public static class ResponseMapping extends BaseMapping<StreetDoc,StreetResponse>{
        @Override
        public StreetResponse convert(StreetDoc streetDoc){
            return StreetResponse.builder()
                .id(streetDoc.getId().toString())
                .title(streetDoc.getTitle())
                .cityId(streetDoc.getCityId().toString())
                .build();
            }


            }



    public static class SearchMapping extends BaseMapping<SearchResponse< StreetDoc>,SearchResponse<StreetResponse>>{
        private ResponseMapping responseMapping=new ResponseMapping();

        @Override
        public SearchResponse<StreetResponse>convert(SearchResponse<StreetDoc> searchResponse){
                return SearchResponse.of(
                searchResponse.getList().stream().map(responseMapping::convert).collect(Collectors.toList()),
                searchResponse.getCount()
                );

                }


                }

        private final RequestMapping request=new RequestMapping();
        private final ResponseMapping response=new ResponseMapping();
        private final SearchMapping search=new SearchMapping();

        public static StreetMapping getInstance(){
            return new StreetMapping();
            }
}

