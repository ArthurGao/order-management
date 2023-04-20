package com.a2xaccounting.controller;

import com.a2xaccounting.AbstractTest;
import com.a2xaccounting.container.MySQLExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({SpringExtension.class, MySQLExtension.class})
@AutoConfigureMockMvc
@WebAppConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrderApiControllerITest extends AbstractTest {

    @Autowired
    private MockMvc mockMvc;

    @DynamicPropertySource
    static void setDatasourceProperties(DynamicPropertyRegistry registry) {
        MySQLExtension.setDynamicPropertySource(registry);
    }

    /**
     * This is an integration test that tests the whole circle of the API.
     * It creates an order, gets it, updates it and search it by some criteria. Then delete it.
     */
    @Test
    @Sql({"classpath:sql/init_table_data.sql"})
    void testWholeCircle_allPass() throws Exception {
        String url = "/order";
        String request = "{\n" +
                "  \"date\": \"2019-08-24\",\n" +
                "  \"amount\": 10.2,\n" +
                "  \"currencyCode\": \"NZD\",\n" +
                "  \"transactionType\": \"Sale\"\n" +
                "}";
        mockMvc.perform(post(url).content(request).contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());

        url = "/order/e1aa1f1b-4d4a-44ef-ae43-77b55a6ddd81";
        mockMvc.perform(get(url).contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());

        String updateRequest = "{\"amount\": 10.2}";
        mockMvc.perform(put(url).content(updateRequest).contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());

        url = "/order/e1aa1f1b-4d4a-44ef-ae43-77b55a6ddd81";
        mockMvc.perform(get(url).contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());

        url = "/order/?dateFrom=2019-08-24&dateTo=2019-08-25";
        String searchRequest = "{\n" +
                "    \"pageSize\": 3,\n" +
                "    \"pageNumber\": 0\n" +
                "}";
        mockMvc.perform(get(url).content(searchRequest).contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());


        url = "/order/e1aa1f1b-4d4a-44ef-ae43-77b55a6ddd81";
        mockMvc.perform(delete(url).contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent());
    }

    @Test
    @Sql({"classpath:sql/table.sql", "classpath:sql/init_table_data.sql"})
    void testGetOrderById_givenInvalidId_returnEmpty() throws Exception {
        String url = "/order/abcd";
        mockMvc.perform(get(url).contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql({"classpath:sql/table.sql", "classpath:sql/init_table_data.sql"})
    void testCreateOrder_givenInvalidInput_returnEmpty() throws Exception {
        String url = "/order/";
        String request = "{\n" +
                "  \"dat\": \"2019-08-24\",\n" +
                "  \"amot\": 10.2,\n" +
                "  \"currye\": \"NZD\",\n" +
                "  \"transactionType\": \"Sale\"\n" +
                "}";
        mockMvc.perform(post(url).content(request).contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @Sql({"classpath:sql/table.sql", "classpath:sql/init_table_data.sql"})
    void testCreateOrder_givenInvalidCurrencyCode_returnEmpty() throws Exception {
        String url = "/order/";
        String request = "{\n" +
                "  \"date\": \"2019-08-24\",\n" +
                "  \"amount\": 10.2,\n" +
                "  \"currencyCode\": \"XXXX\",\n" +
                "  \"transactionType\": \"Sale\"\n" +
                "}";
        mockMvc.perform(post(url).content(request).contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql({"classpath:sql/table.sql", "classpath:sql/init_table_data.sql"})
    void testCreateOrder_givenInvalidFutureDate_returnEmpty() throws Exception {
        String url = "/order/";
        String request = "{\n" +
                "  \"date\": \"2159-08-24\",\n" +
                "  \"amount\": 10.2,\n" +
                "  \"currencyCode\": \"XXXX\",\n" +
                "  \"transactionType\": \"Sale\"\n" +
                "}";
        mockMvc.perform(post(url).content(request).contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql({"classpath:sql/table.sql", "classpath:sql/init_table_data.sql"})
    void testUpdateOrder_givenInvalidFutureDate_returnEmpty() throws Exception {
        String url = "/order/e19f2eb4-793b-47cc-bfab-6243c3a0b86b";
        String request = "{\n" +
                "  \"date\": \"2159-08-24\",\n" +
                "  \"amount\": 10.2,\n" +
                "  \"currencyCode\": \"XXXX\",\n" +
                "  \"transactionType\": \"Sale\"\n" +
                "}";
        mockMvc.perform(put(url).content(request).contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql({"classpath:sql/table.sql", "classpath:sql/init_table_data.sql"})
    void testUpdateOrder_givenUnsupportMethod_returnEmpty() throws Exception {
        String url = "/order";
        String request = "{\n" +
                "  \"date\": \"2159-08-24\",\n" +
                "  \"amount\": 10.2,\n" +
                "  \"currencyCode\": \"XXXX\",\n" +
                "  \"transactionType\": \"Sale\"\n" +
                "}";
        mockMvc.perform(put(url).content(request).contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isInternalServerError());
    }
}
