package com.a2xaccounting.database.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "ORDERS", schema = "test")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type = "uuid-char")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "amount")
    private double amount;

    @Column(name = "currency_code")
    private String currencyCode;

    @Column(name = "transaction_type")
    private String transactionType;
}
