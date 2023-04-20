package com.a2xaccounting.database.services;

import com.a2xaccounting.database.models.OrderEntity;
import com.a2xaccounting.database.repository.OrderRepository;
import com.a2xaccounting.exceptions.NotFoundException;
import com.a2xaccounting.models.OrderDTO;
import com.a2xaccounting.models.mapper.OrderMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Service
@Log4j2
@Validated
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    public String createOrder(@NotNull OrderDTO orderDTO) {
        OrderEntity orderEntity = orderMapper.map(orderDTO);
        return orderRepository.save(orderEntity).getId().toString();
    }

    public void deleteOrder(@NotNull UUID id) {
        if (orderRepository.findById(id).isEmpty()) {
            throw new NotFoundException("Order not found with id " + id);
        }
        orderRepository.deleteById(id);
    }

    public Optional<OrderDTO> getOrderById(@NotNull UUID id) {
        return orderRepository.findById(id).map(orderMapper::map);
    }

    public void updateOrder(@NotNull UUID id, @NotNull OrderDTO orderDTO) {
        OrderEntity orderToUpdate = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found with id " + id));
        if (orderDTO.getTransactionType() != null) {
            orderToUpdate.setTransactionType(orderDTO.getTransactionType().getValue().toUpperCase(Locale.ROOT));
        }
        if (orderDTO.getAmount() != null) {
            orderToUpdate.setAmount(orderDTO.getAmount());
        }
        if (orderDTO.getCurrencyCode() != null) {
            orderToUpdate.setCurrencyCode(orderDTO.getCurrencyCode());
        }
        if (orderDTO.getDate() != null) {
            orderToUpdate.setDate(orderDTO.getDate());
        }
        orderRepository.save(orderToUpdate);
    }

    public Page<OrderDTO> searchOrders(String transactionType, LocalDate transactionFrom, LocalDate transactionTo,
                                       String currencyCode, int page, int size) {

        return orderRepository.searchOrders(transactionType, transactionFrom, transactionTo, currencyCode, PageRequest.of(page, size))
                .map(orderMapper::map);
    }
}