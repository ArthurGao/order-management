package com.a2xaccounting.models.mapper;

import com.a2xaccounting.database.models.OrderEntity;
import com.a2xaccounting.models.OrderDTO;
import com.a2xaccounting.models.TransactionType;
import com.a2xaccounting.ordermanagement.model.OrderDto;
import com.a2xaccounting.ordermanagement.model.OrderToUpdateDto;
import com.a2xaccounting.ordermanagement.model.TransactionTypeDto;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring", collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED)
@Component
public interface OrderMapper {

    OrderEntity map(OrderDTO order);
    OrderDTO map(OrderEntity order);
    OrderDto mapToRest(OrderDTO order);
    OrderDTO map(OrderDto order);
    OrderDTO map(OrderToUpdateDto order);

    default TransactionTypeDto map(TransactionType contentTypeValue) {
        if (contentTypeValue == null) {
            return null;
        }
        return TransactionTypeDto.fromValue(contentTypeValue.getValue());
    }
}
