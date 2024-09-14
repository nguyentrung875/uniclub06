package com.cybersoft.uniclub.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SizeDTO {
    private int id;
    private String name;
    private Integer quantity;
    private Double price;
    private Integer sku;
}
