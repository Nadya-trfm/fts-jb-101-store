package

        com.foodtech.store.photo.mapping;


import com.foodtech.store.base.api.response.SearchResponse;
import com.foodtech.store.base.mapping.BaseMapping;
import com.foodtech.store.photo.api.request.PhotoRequest;
import com.foodtech.store.photo.api.response.PhotoResponse;
import com.foodtech.store.photo.model.PhotoDoc;
import lombok.Getter;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PhotoMapping{

    public static class RequestMapping {


        public PhotoDoc convert(PhotoRequest photoRequest){
            return PhotoDoc.builder()
                .id(photoRequest.getId())
                .title(photoRequest.getTitle())
                .contentType(photoRequest.getContentType())
            .build();
            }

    }

    public static class ResponseMapping extends BaseMapping<PhotoDoc,PhotoResponse>{
        @Override
        public PhotoResponse convert(PhotoDoc photoDoc){
            return PhotoResponse.builder()
                .id(photoDoc.getId().toString())
                .title(photoDoc.getTitle())
                .contentType(photoDoc.getContentType())
                .build();
            }


            }



    public static class SearchMapping extends BaseMapping<SearchResponse< PhotoDoc>,SearchResponse<PhotoResponse>>{
        private ResponseMapping responseMapping=new ResponseMapping();

        @Override
        public SearchResponse<PhotoResponse>convert(SearchResponse<PhotoDoc> searchResponse){
                return SearchResponse.of(
                searchResponse.getList().stream().map(responseMapping::convert).collect(Collectors.toList()),
                searchResponse.getCount()
                );

                }


                }

        private final RequestMapping request=new RequestMapping();
        private final ResponseMapping response=new ResponseMapping();
        private final SearchMapping search=new SearchMapping();

        public static PhotoMapping getInstance(){
            return new PhotoMapping();
            }
}

