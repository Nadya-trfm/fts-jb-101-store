package com.foodtech.store.order.model;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum Payment {
    CASH("��������"),CARD("�����");

    private String title;
    Payment(String title) {
        this.title =title;
    }
}
