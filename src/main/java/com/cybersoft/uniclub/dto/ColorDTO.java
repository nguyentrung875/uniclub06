package com.cybersoft.uniclub.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ColorDTO {
    private int id;
    private String name;
    private List<String> images;
    private List<SizeDTO> sizes;
}
