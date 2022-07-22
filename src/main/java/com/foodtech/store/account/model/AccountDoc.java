package com.foodtech.store.account.model;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.DigestUtils;

@Document
@Setter
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountDoc {
    @Id
    private ObjectId id;
    private String name;
    private Long phone;
    private String email;
    private String password;
    private Boolean isAdmin = false;

    public static String hexPassword(String clearPassword){
        return DigestUtils.md5DigestAsHex(clearPassword.getBytes());
    }

}
