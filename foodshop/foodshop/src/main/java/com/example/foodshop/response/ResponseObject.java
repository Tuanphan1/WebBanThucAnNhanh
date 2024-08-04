package com.example.foodshop.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ResponseObject {
    private Number status;
    private String message;
    private boolean Success;
    private Object data;
}
