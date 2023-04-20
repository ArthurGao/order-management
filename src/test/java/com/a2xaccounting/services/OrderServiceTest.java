package com.a2xaccounting.services;

import com.a2xaccounting.AbstractTest;
import com.a2xaccounting.database.services.OrderService;
import com.a2xaccounting.exceptions.NotFoundException;
import com.a2xaccounting.models.OrderDTO;
import com.a2xaccounting.models.TransactionType;
import com.a2xaccounting.models.mapper.OrderMapperImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 *  Tests to test all service methods with DB (H2 database)
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest
@Import({OrderService.class, OrderMapperImpl.class})
@ActiveProfiles("h2")
class OrderServiceTest extends AbstractTest {

    @Autowired
    private OrderService orderService;

    private static OrderDTO expectedOrderDTO;

    @BeforeAll
    public static void setup() {
        expectedOrderDTO = new OrderDTO();
        //Here do a decimal round to 2 precision
        expectedOrderDTO.setAmount(100.33);
        expectedOrderDTO.setCurrencyCode("CHY");
        expectedOrderDTO.setDate(toLocalDate("2021-08-26 00:00:00"));
        expectedOrderDTO.setTransactionType(TransactionType.REFUND);
    }

    @Test
    @Sql({"classpath:sql/table.sql", "classpath:sql/init_table_data.sql"})
    void testGetOrderById_givenNotExistingId_returnEmpty() {
        Optional<OrderDTO> orderDTO = orderService.getOrderById(UUID.fromString("e19f2eb4-793b-47cc-bfab-6243c3a0b812"));
        assertThat(orderDTO).isEmpty();
    }

    @Test
    @Sql({"classpath:sql/table.sql", "classpath:sql/init_table_data.sql"})
    void testGetOrderById_givenExistingId_returnOrder() {
        Optional<OrderDTO> actualOrderDTO = orderService.getOrderById(UUID.fromString("e19f2eb4-793b-47cc-bfab-6243c3a0b86b"));
        assertThat(actualOrderDTO).isPresent();
        validateOrder(actualOrderDTO.get(), expectedOrderDTO);
    }

    @Test
    @Sql({"classpath:sql/table.sql", "classpath:sql/init_table_data.sql"})
    void testDeleteExistingOrder_givenValid_deleteOk() {
        orderService.deleteOrder(UUID.fromString("e19f2eb4-793b-47cc-bfab-6243c3a0b86b"));
        Optional<OrderDTO> actualOrderDTO = orderService.getOrderById(UUID.fromString("e19f2eb4-793b-47cc-bfab-6243c3a0b86b"));
        assertThat(actualOrderDTO).isEmpty();
    }

    @Test
    @Sql({"classpath:sql/table.sql", "classpath:sql/init_table_data.sql"})
    void testDeleteExistingOrder_givenNotExistingOrder_exception() {
        assertThatThrownBy(() -> orderService.deleteOrder(UUID.fromString("e19f2eb4-793b-47cc-bfab-6243c3a0b86a"))).isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Order not found with id");
    }

    @Test
    @Sql({"classpath:sql/table.sql", "classpath:sql/init_table_data.sql"})
    void testCreateOrder_givenValid_createOrder_deleteOk() {
        String id = orderService.createOrder(expectedOrderDTO);
        Optional<OrderDTO> actualOrderDTO = orderService.getOrderById(UUID.fromString(id));
        assertThat(actualOrderDTO).isPresent();
        validateOrder(actualOrderDTO.get(), expectedOrderDTO);
    }

    @Test
    @Sql({"classpath:sql/table.sql", "classpath:sql/init_table_data.sql"})
    void testSearchOrder_givenCurrencyCode_returnOrder() {
        Page<OrderDTO> actualOrderDTO = orderService.searchOrders
                (null, null, null, "CYN", 0, 1);
        assertThat(actualOrderDTO.getTotalElements()).isEqualTo(1);
        assertThat(actualOrderDTO.getContent()).hasSize(1);
        validateOrder(actualOrderDTO.getContent().get(0), expectedOrderDTO);
    }

    @Test
    @Sql({"classpath:sql/table.sql", "classpath:sql/init_table_data.sql"})
    void testSearchOrder_givenDataFromAndTo_returnOrder() {
        Page<OrderDTO> actualOrderDTO = orderService.searchOrders
                (null, toLocalDate("2021-08-25 00:00:00"),
                        toLocalDate("2021-08-27 00:00:00"), null, 0, 1);
        assertThat(actualOrderDTO.getTotalElements()).isEqualTo(1);
        assertThat(actualOrderDTO.getContent()).hasSize(1);
        validateOrder(actualOrderDTO.getContent().get(0), expectedOrderDTO);
    }

    @Test
    @Sql({"classpath:sql/table.sql", "classpath:sql/init_table_data.sql"})
    void testSearchOrder_givenTransactionType_returnOrder() {
        Page<OrderDTO> actualOrderDTO = orderService.searchOrders
                (TransactionType.REFUND.getValue().toUpperCase(Locale.ROOT), null, null, null, 0, 1);
        assertThat(actualOrderDTO.getTotalElements()).isEqualTo(1);
        assertThat(actualOrderDTO.getContent()).hasSize(1);
        validateOrder(actualOrderDTO.getContent().get(0), expectedOrderDTO);
    }

}
