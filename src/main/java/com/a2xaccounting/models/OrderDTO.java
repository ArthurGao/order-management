package com.a2xaccounting.models;

import lombok.Data;
import lombok.Getter;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class OrderDTO {

    private UUID id;

    private LocalDate date;

    private Double amount;

    private String currencyCode;

    private TransactionType transactionType;

    public Double getAmount() {
        DecimalFormat df = new DecimalFormat("#.##");
        return Double.parseDouble(df.format(amount));
    }
}

