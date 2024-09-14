package com.cybersoft.uniclub.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.awt.*;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDTO {
    private int id;
    private String name;
    private String imageLink;
    private double price;
    private String overview;
    private List<String> categories;
    private List<String> tags;
    private List<SizeDTO> sizes;
    private List<ColorDTO> colors;
    private List<ColorDTO> priceColorSize;

}
