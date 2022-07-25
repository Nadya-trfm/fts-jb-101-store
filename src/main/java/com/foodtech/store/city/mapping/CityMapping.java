package

        com.foodtech.store.city.mapping;


import com.foodtech.store.base.api.response.SearchResponse;
import com.foodtech.store.base.mapping.BaseMapping;
import com.foodtech.store.city.api.request.CityRequest;
import com.foodtech.store.city.api.response.CityResponse;
import com.foodtech.store.city.model.CityDoc;
import lombok.Getter;
import org.bson.types.ObjectId;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CityMapping {

    public static class RequestMapping {

        public CityDoc convert(CityRequest cityRequest) {
            return CityDoc.builder()
                    .id(cityRequest.getId())
                    .title(cityRequest.getTitle())
                    .priceMultiplier(Float.parseFloat(cityRequest.getPriceMultiplier()))
                    .build();
        }


    }

    public static class ResponseMapping extends BaseMapping<CityDoc, CityResponse> {
        @Override
        public CityResponse convert(CityDoc cityDoc) {
            return CityResponse.builder()
                    .id(cityDoc.getId().toString())
                    .title(cityDoc.getTitle())
                    .priceMultiplier(cityDoc.getPriceMultiplier())
                    .build();
        }


    }


    public static class SearchMapping extends BaseMapping<SearchResponse<CityDoc>, SearchResponse<CityResponse>> {
        private ResponseMapping responseMapping = new ResponseMapping();

        @Override
        public SearchResponse<CityResponse> convert(SearchResponse<CityDoc> searchResponse) {
            return SearchResponse.of(
                    searchResponse.getList().stream().map(responseMapping::convert).collect(Collectors.toList()),
                    searchResponse.getCount()
            );

        }


    }

    private final RequestMapping request = new RequestMapping();
    private final ResponseMapping response = new ResponseMapping();
    private final SearchMapping search = new SearchMapping();

    public static CityMapping getInstance() {
        return new CityMapping();
    }
}

