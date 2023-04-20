package com.a2xaccounting.controller;

import com.a2xaccounting.database.services.OrderService;
import com.a2xaccounting.exceptions.BadRequestException;
import com.a2xaccounting.exceptions.NotFoundException;
import com.a2xaccounting.models.OrderDTO;
import com.a2xaccounting.models.mapper.OrderMapper;
import com.a2xaccounting.ordermanagement.api.OrderApi;
import com.a2xaccounting.ordermanagement.api.OrderApiDelegate;
import com.a2xaccounting.ordermanagement.model.*;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Currency;
import java.util.UUID;

@Log4j2
@Validated
@Service
public class OrderApiControllerImpl implements OrderApiDelegate {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @Autowired
    public OrderApiControllerImpl(OrderService orderService, OrderMapper orderMapper) {
        this.orderService = orderService;
        this.orderMapper = orderMapper;
    }

    /**
     * POST /order : Create an order
     * Create an order
     *
     * @param orderDto (optional)
     * @return Created (status code 201)
     * or Bad Request (status code 400)
     * or Conflict (status code 409)
     * or Internal Server Error (status code 500)
     * @see OrderApi#createOrder
     */
    @Override
    public ResponseEntity<CreateRepsonseDto> createOrder(OrderDto orderDto) {
        if (orderDto.getDate().isAfter(LocalDate.now())) {
            throw new BadRequestException("Date cannot be in the future");
        }
        try {
            Currency.getInstance(orderDto.getCurrencyCode());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Currency code is not valid");
        }
        String orderId = orderService.createOrder(orderMapper.map(orderDto));
        CreateRepsonseDto createRepsonseDto = new CreateRepsonseDto();
        createRepsonseDto.setId(orderId);
        return new ResponseEntity<>(createRepsonseDto, HttpStatus.CREATED);
    }

    /**
     * DELETE /order/{id} : Delete an order
     * Delete an existing order by id
     *
     * @param id (required)
     * @return No Content (status code 204)
     * or Bad Request (status code 400)
     * or Not Found (status code 404)
     * or Internal Server Error (status code 500)
     * @see OrderApi#deleteOrderById
     */
    @Override
    public ResponseEntity<Void> deleteOrderById(String id) {
        orderService.deleteOrder(getUUID(id));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * GET /order/{id} : Get User Info by User ID
     * Retrieve the information of the order with the matching order ID.
     *
     * @param id (required)
     * @return Order Found (status code 200)
     * or Bad Request (status code 400)
     * or Order Not Found (status code 404)
     * or Internal Server Error (status code 500)
     * @see OrderApi#getOrderById
     */
    @Override
    public ResponseEntity<OrderDto> getOrderById(String id) {
        OrderDTO order = orderService.getOrderById(getUUID(id))
                .orElseThrow(() -> new NotFoundException("Order not found with id " + id));
        return new ResponseEntity<>(orderMapper.mapToRest(order), HttpStatus.OK);
    }

    /**
     * GET /order : Search order(s) by criteria
     * Search order(s) by criteria and page
     *
     * @param dateFrom        Transaction date from (optional)
     * @param dateTo          Transaction date to (optional)
     * @param transactionType Transaction type (optional)
     * @param currencyCode    Currency code (optional)
     * @param pageInfoDto     (optional)
     * @return OK (status code 200)
     * or Bad Request (status code 400)
     * or Not Found (status code 404)
     * or Internal Server Error (status code 500)
     * @see OrderApi#searchOrder
     */
    @Override
    public ResponseEntity<OrderListResponseDto> searchOrder(String dateFrom,
                                                            String dateTo,
                                                            String transactionType,
                                                            String currencyCode,
                                                            PageInfoDto pageInfoDto) {
        if (pageInfoDto == null) {
            throw new BadRequestException("Page info is required");
        }
        if (pageInfoDto.getPageSize() == null || pageInfoDto.getPageNumber() == null) {
            throw new BadRequestException("Page size and page number are required");
        }

        LocalDate localDateFrom = StringUtils.isEmpty(dateFrom) ? null : LocalDate.parse(dateFrom, DateTimeFormatter.ISO_DATE);
        LocalDate localDateTo = StringUtils.isEmpty(dateTo) ? null : LocalDate.parse(dateTo, DateTimeFormatter.ISO_DATE);
        if (localDateFrom != null && localDateTo != null && localDateFrom.isAfter(localDateTo)) {
            throw new BadRequestException("Date from cannot be after date to");
        }

        Page<OrderDTO> orderDTOPage = orderService.searchOrders(transactionType,
                localDateFrom, localDateTo, currencyCode,
                pageInfoDto.getPageNumber(), pageInfoDto.getPageSize());
        OrderListResponseDto orderListResponseDto = new OrderListResponseDto();
        PageInfoDto returnPageInfoDto = new PageInfoDto();
        returnPageInfoDto.setPageNumber(orderDTOPage.getNumber());
        returnPageInfoDto.setPageSize(orderDTOPage.getSize());
        returnPageInfoDto.setTotal((int) orderDTOPage.getTotalElements());
        orderListResponseDto.setPageInfo(returnPageInfoDto);
        orderDTOPage.getContent().forEach(orderDTO -> orderListResponseDto.addResultsItem(orderMapper.mapToRest(orderDTO)));
        return new ResponseEntity<>(orderListResponseDto, HttpStatus.OK);
    }

    /**
     * PUT /order/{id} : Update an order
     * Update an existing order
     *
     * @param id       (required)
     * @param orderDto (optional)
     * @return OK (status code 200)
     * or Bad Request (status code 400)
     * or Not Found (status code 404)
     * or Internal Server Error (status code 500)
     * @see OrderApi#updateOrder
     */
    @Override
    public ResponseEntity<Void> updateOrder(String id,
                                            OrderToUpdateDto orderDto) {
        if (orderDto.getDate() != null && orderDto.getDate().isAfter(LocalDate.now())) {
            throw new BadRequestException("Date cannot be in the future");
        }
        if (StringUtils.isNotEmpty(orderDto.getCurrencyCode())) {
            try {
                Currency.getInstance(orderDto.getCurrencyCode());
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Currency code is not valid");
            }
        }
        orderService.updateOrder(getUUID(id), orderMapper.map(orderDto));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private UUID getUUID(String id) {
        try {
            return UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid UUID format");
        }
    }
}
