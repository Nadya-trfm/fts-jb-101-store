package com.foodtech.store.account.mapping;

import com.foodtech.store.account.api.request.AccountRequest;
import com.foodtech.store.account.api.response.AccountResponse;
import com.foodtech.store.account.model.AccountDoc;
import com.foodtech.store.base.api.response.SearchResponse;
import com.foodtech.store.base.mapping.BaseMapping;
import lombok.Getter;
import org.bson.types.ObjectId;

import java.util.stream.Collectors;

@Getter
public class AccountMapping {
    public static class RequestMapping {

        public AccountDoc convert(AccountRequest accountRequest, ObjectId accountId){
            return AccountDoc.builder()
                    .id(accountId)
                    .name(accountRequest.getName())
                    .phone(accountRequest.getPhone())
                    .email(accountRequest.getEmail())
                    .build();
        }



    }
    public static class ResponseMapping extends BaseMapping<AccountDoc, AccountResponse> {
        @Override
        public AccountResponse convert(AccountDoc accountDoc){
            return AccountResponse.builder()
                    .id(accountDoc.getId().toString())
                    .name(accountDoc.getName())
                    .phone(accountDoc.getPhone())
                    .email(accountDoc.getEmail())
                    .isAdmin(accountDoc.getIsAdmin())
                    .build();
        }
    }

    public static class SearchMapping extends BaseMapping<SearchResponse<AccountDoc>, SearchResponse<AccountResponse>> {
        private ResponseMapping responseMapping = new ResponseMapping();

        @Override
        public SearchResponse<AccountResponse> convert(SearchResponse<AccountDoc> searchResponse) {
            return SearchResponse.of(
                    searchResponse.getList().stream().map(responseMapping::convert).collect(Collectors.toList()),
                    searchResponse.getCount()
            );

        }

    }

    private final RequestMapping request = new RequestMapping();
    private final ResponseMapping response = new ResponseMapping();
    private final SearchMapping search = new SearchMapping();

    public static AccountMapping getInstance(){
        return new AccountMapping();
    }
}
