package

        com.foodtech.store.address.mapping;


import com.foodtech.store.base.api.response.SearchResponse;
import com.foodtech.store.base.mapping.BaseMapping;
import com.foodtech.store.address.api.request.AddressRequest;
import com.foodtech.store.address.api.response.AddressResponse;
import com.foodtech.store.address.model.AddressDoc;
import lombok.Getter;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class AddressMapping{

    public static class RequestMapping {


        public AddressDoc convert(AddressRequest addressRequest){
            return AddressDoc.builder()
                .id(addressRequest.getId())
                .streetId(addressRequest.getStreetId())
                .house(addressRequest.getHouse())
                .room(addressRequest.getRoom())
                .entrance(addressRequest.getEntrance())
                .level(addressRequest.getLevel())
            .build();
            }

    }

    public static class ResponseMapping extends BaseMapping<AddressDoc,AddressResponse>{
        @Override
        public AddressResponse convert(AddressDoc addressDoc){
            return AddressResponse.builder()
                .id(addressDoc.getId().toString())
                .streetId(addressDoc.getStreetId().toString())
                .house(addressDoc.getHouse())
                .room(addressDoc.getRoom())
                .entrance(addressDoc.getEntrance())
                .level(addressDoc.getLevel())
                .build();
            }


            }



    public static class SearchMapping extends BaseMapping<SearchResponse< AddressDoc>,SearchResponse<AddressResponse>>{
        private ResponseMapping responseMapping=new ResponseMapping();

        @Override
        public SearchResponse<AddressResponse>convert(SearchResponse<AddressDoc> searchResponse){
                return SearchResponse.of(
                searchResponse.getList().stream().map(responseMapping::convert).collect(Collectors.toList()),
                searchResponse.getCount()
                );

                }


                }

        private final RequestMapping request=new RequestMapping();
        private final ResponseMapping response=new ResponseMapping();
        private final SearchMapping search=new SearchMapping();

        public static AddressMapping getInstance(){
            return new AddressMapping();
            }
}

