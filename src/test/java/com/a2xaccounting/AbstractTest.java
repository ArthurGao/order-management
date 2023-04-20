package com.a2xaccounting;

import com.a2xaccounting.models.OrderDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.jeasy.random.EasyRandom;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

public class AbstractTest {

    private EasyRandom generator;

    protected double getRoundResult(Double amount){
        DecimalFormat df = new DecimalFormat("#.##");
        return Double.parseDouble(df.format(amount));
    }

    protected <T> T createObject(Class<T> c) {
        if (generator == null) {
            generator = new EasyRandom();
        }
        return generator.nextObject(c);
    }

    protected static String getJsonString(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(obj);
    }

    protected void validateOrder(OrderDTO actualOrder, OrderDTO orderDTO) {
        assertThat(actualOrder.getDate()).isEqualTo(orderDTO.getDate());
        assertThat(actualOrder.getAmount()).isEqualTo(orderDTO.getAmount());
        assertThat(actualOrder.getCurrencyCode()).isEqualTo(orderDTO.getCurrencyCode());
        assertThat(actualOrder.getTransactionType()).isEqualTo(orderDTO.getTransactionType());
    }

    protected static LocalDate toLocalDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
        return dateTime.toLocalDate();
    }

}
