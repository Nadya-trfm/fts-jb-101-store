package com.foodtech.store.order.model;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum Payment {
    CASH("наличные"),CARD("карта");

    private String title;
    Payment(String title) {
        this.title =title;
    }
}
