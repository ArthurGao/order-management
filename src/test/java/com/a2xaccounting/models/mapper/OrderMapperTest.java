package com.a2xaccounting.models.mapper;

import com.a2xaccounting.AbstractTest;
import com.a2xaccounting.database.models.OrderEntity;
import com.a2xaccounting.models.OrderDTO;
import com.a2xaccounting.models.TransactionType;
import com.a2xaccounting.ordermanagement.model.OrderDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test to test all mapper methods
 */
class OrderMapperTest extends AbstractTest {

    private static OrderMapper orderMapper;

    @BeforeAll
    public static void setup() {
        orderMapper = new OrderMapperImpl();
    }

    @Test
    void testDTOtoEntity() {
        com.a2xaccounting.models.OrderDTO orderDTO = createObject(com.a2xaccounting.models.OrderDTO.class);
        OrderEntity orderEntity = orderMapper.map(orderDTO);
        assertThat(orderDTO.getAmount()).isEqualTo(orderEntity.getAmount());
        assertThat(orderDTO.getDate()).isEqualTo(orderEntity.getDate());
        assertThat(orderDTO.getCurrencyCode()).isEqualTo(orderEntity.getCurrencyCode());
        assertThat(orderDTO.getTransactionType().getValue().toLowerCase(Locale.ROOT)).isEqualTo(orderEntity.getTransactionType().toLowerCase(Locale.ROOT));
    }

    @Test
    void testEntityToDTO() {
        OrderEntity orderEntity = createObject(OrderEntity.class);
        orderEntity.setTransactionType("REFUND");
        com.a2xaccounting.models.OrderDTO orderDTO = orderMapper.map(orderEntity);
        assertThat(getRoundResult(orderEntity.getAmount())).isEqualTo(orderDTO.getAmount());
        assertThat(orderEntity.getDate()).isEqualTo(orderDTO.getDate());
        assertThat(orderEntity.getCurrencyCode()).isEqualTo(orderDTO.getCurrencyCode());
        assertThat(orderEntity.getTransactionType().toLowerCase(Locale.ROOT)).isEqualTo(orderDTO.getTransactionType().getValue().toLowerCase(Locale.ROOT));
    }

    @Test
    void testDTOToRest() {
        com.a2xaccounting.models.OrderDTO orderDTO = createObject(com.a2xaccounting.models.OrderDTO.class);
        orderDTO.setTransactionType(TransactionType.SALE);
        OrderDto orderDto = orderMapper.mapToRest(orderDTO);
        assertThat(orderDto.getAmount()).isEqualTo(orderDTO.getAmount());
        assertThat(orderDto.getDate()).isEqualTo(orderDTO.getDate());
        assertThat(orderDto.getCurrencyCode()).isEqualTo(orderDTO.getCurrencyCode());
        assertThat(orderDto.getTransactionType().getValue()).isEqualTo(orderDTO.getTransactionType().getValue());
    }

    @Test
    void testRestToDTO() {
        OrderDto orderDto = createObject(OrderDto.class);
        OrderDTO orderDTO = orderMapper.map(orderDto);
        assertThat(orderDTO.getAmount()).isEqualTo(getRoundResult(orderDto.getAmount()));
        assertThat(orderDTO.getDate()).isEqualTo(orderDto.getDate());
        assertThat(orderDTO.getCurrencyCode()).isEqualTo(orderDto.getCurrencyCode());
        assertThat(orderDTO.getTransactionType().getValue()).isEqualTo(orderDto.getTransactionType().getValue());
    }

}
