package com.foodtech.store.base.controller;

import com.foodtech.store.base.api.response.IndexResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExampleController {
    @GetMapping(value ="/")
    public IndexResponse index(){
        return IndexResponse.builder().message("Hello, World").build();
    }
}
