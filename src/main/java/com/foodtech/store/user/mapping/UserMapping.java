package

        com.foodtech.store.user.mapping;


import com.foodtech.store.base.api.response.SearchResponse;
import com.foodtech.store.base.mapping.BaseMapping;
import com.foodtech.store.user.api.request.UserRequest;
import com.foodtech.store.user.api.response.UserResponse;
import com.foodtech.store.user.model.UserDoc;
import lombok.Getter;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class UserMapping{



    public static class ResponseMapping extends BaseMapping<UserDoc,UserResponse>{
        @Override
        public UserResponse convert(UserDoc userDoc){
            return UserResponse.builder()
                .id(userDoc.getId().toString())
                .name(userDoc.getName())
                .phone(userDoc.getPhone())
                .accountId(String.valueOf(userDoc.getAccountId()))
                .build();
            }


            }



    public static class SearchMapping extends BaseMapping<SearchResponse< UserDoc>,SearchResponse<UserResponse>>{
        private ResponseMapping responseMapping=new ResponseMapping();

        @Override
        public SearchResponse<UserResponse>convert(SearchResponse<UserDoc> searchResponse){
                return SearchResponse.of(
                searchResponse.getList().stream().map(responseMapping::convert).collect(Collectors.toList()),
                searchResponse.getCount()
                );

                }


                }

        private final ResponseMapping response=new ResponseMapping();
        private final SearchMapping search=new SearchMapping();

        public static UserMapping getInstance(){
            return new UserMapping();
            }
}

