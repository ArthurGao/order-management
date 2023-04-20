package com.a2xaccounting.controller;

import com.a2xaccounting.AbstractTest;
import com.a2xaccounting.OrderManagementAPIApplication;
import com.a2xaccounting.database.services.OrderService;
import com.a2xaccounting.models.OrderDTO;
import com.a2xaccounting.models.TransactionType;
import com.a2xaccounting.models.mapper.OrderMapperImpl;
import com.a2xaccounting.ordermanagement.model.OrderDto;
import com.a2xaccounting.ordermanagement.model.OrderListResponseDto;
import com.a2xaccounting.ordermanagement.model.PageInfoDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@DirtiesContext
@AutoConfigureMockMvc
@SpringBootTest(classes = OrderManagementAPIApplication.class)
class OrderApiControllerTest extends AbstractTest {

    @Mock
    private OrderService orderService;

    @Autowired
    private OrderMapperImpl orderMapper;
    private static OrderDTO expectedOrderDTO;

    @BeforeAll
    public static void setup() {
        expectedOrderDTO = new OrderDTO();
        //Here do a decimal round to 2 precision
        expectedOrderDTO.setAmount(100.33);
        expectedOrderDTO.setCurrencyCode("CHY");
        expectedOrderDTO.setDate(toLocalDate("2021-08-26 00:00:00"));
        expectedOrderDTO.setTransactionType(TransactionType.REFUND);
        expectedOrderDTO.setId(UUID.fromString("e1aa1f1b-4d4a-44ef-ae43-77b55a6ddd81"));
    }

    @Test
    void testGetOrderById_givenValidInput_returnOrder() {

        OrderApiControllerImpl myController = new OrderApiControllerImpl(orderService, orderMapper);
        when(orderService.getOrderById(any(UUID.class))).thenReturn(Optional.of(expectedOrderDTO));

        ResponseEntity<OrderDto> responseEntity  =  myController.getOrderById("e1aa1f1b-4d4a-44ef-ae43-77b55a6ddd81");
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getAmount()).isEqualTo(100.33);
        assertThat(responseEntity.getBody().getCurrencyCode()).isEqualTo("CHY");
        assertThat(responseEntity.getBody().getDate()).isEqualTo(toLocalDate("2021-08-26 00:00:00"));
        assertThat(responseEntity.getBody().getTransactionType().getValue()).isEqualTo("Refund");
    }

    @Test
    void testSearchOrderById_givenValidInput_returnOrder() {
        OrderDTO expectedOrderDTO2 = createObject(OrderDTO.class);
        OrderDTO expectedOrderDTO3 = createObject(OrderDTO.class);
        OrderApiControllerImpl myController = new OrderApiControllerImpl(orderService, orderMapper);
        Page page = new PageImpl(List.of(expectedOrderDTO, expectedOrderDTO3, expectedOrderDTO2));
        when(orderService.searchOrders(any(String.class), any(LocalDate.class), any(LocalDate.class),
                any(String.class), any(Integer.class), any(Integer.class))).
                thenReturn(page);
        PageInfoDto pageInfoDto = new PageInfoDto();
        pageInfoDto.setPageSize(10);
        pageInfoDto.setPageNumber(0);
        ResponseEntity<OrderListResponseDto> responseEntity  =  myController.
                searchOrder("2021-08-26", "2021-08-27",
                        TransactionType.REFUND.getValue(), "NZD", pageInfoDto);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getResults()).hasSize(3);
        assertThat(responseEntity.getBody().getResults()).filteredOn(e -> e.getId().toString().equals("e1aa1f1b-4d4a-44ef-ae43-77b55a6ddd81"))
                .hasSize(1);
    }

}
