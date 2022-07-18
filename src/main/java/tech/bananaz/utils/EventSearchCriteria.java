package tech.bananaz.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import tech.bananaz.enums.Operation;

@Data
@AllArgsConstructor
public class EventSearchCriteria {

    private String key;
    private Object value;
    private Operation operation;

}
