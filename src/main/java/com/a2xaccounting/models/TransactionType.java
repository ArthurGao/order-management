package com.a2xaccounting.models;

import lombok.Getter;

public enum TransactionType {
    SALE("Sale"),
    REFUND("Refund");

    @Getter
    private String value;

    TransactionType(String value) {
        this.value = value;
    }
}
