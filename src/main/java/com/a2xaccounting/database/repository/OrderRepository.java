package com.a2xaccounting.database.repository;

import com.a2xaccounting.database.models.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {

    @Query(value = "SELECT o FROM OrderEntity o WHERE (:transactionType is null or o.transactionType= :transactionType) " +
            "and (:transactionFrom is null or o.date >= :transactionFrom)" +
            "and (:transactionTo is null or o.date <= :transactionTo) " +
            "and (:currencyCode is null or o.currencyCode <= :currencyCode) ",
            countQuery = "SELECT count(o) FROM OrderEntity o WHERE (:transactionType is null or o.transactionType= :transactionType) "+
                    " and (:transactionFrom is null or o.date >= :transactionFrom)" +
                    " and (:transactionTo is null or o.date <= :transactionTo)"   +
                    " and (:currencyCode is null or o.currencyCode <= :currencyCode)")
    Page<OrderEntity> searchOrders(String transactionType, LocalDate transactionFrom, LocalDate transactionTo,
                                   String currencyCode, Pageable pageable);
}
