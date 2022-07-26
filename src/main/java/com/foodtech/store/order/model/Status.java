package com.foodtech.store.order.model;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum Status {
        IN_PROCESSING("в обработке"), ORDERED("заказано"), DELIVERED("доставлено");
        private String title;
  Status(String title) {
        this.title =title;
    }
}
