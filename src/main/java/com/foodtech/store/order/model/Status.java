package com.foodtech.store.order.model;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum Status {
        IN_PROCESSING("� ���������"), ORDERED("��������"), DELIVERED("����������");
        private String title;
  Status(String title) {
        this.title =title;
    }
}
