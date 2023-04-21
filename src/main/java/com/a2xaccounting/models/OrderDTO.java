package com.a2xaccounting.models;

import com.a2xaccounting.validator.IsoCurrencyCode;
import com.a2xaccounting.validator.OrderDate;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class OrderDTO {

    private UUID id;

    @OrderDate
    private LocalDate date;

    private Double amount;

    @IsoCurrencyCode
    private String currencyCode;

    private TransactionType transactionType;

    public Double getAmount() {
        DecimalFormat df = new DecimalFormat("#.##");
        return Double.parseDouble(df.format(amount));
    }
}

