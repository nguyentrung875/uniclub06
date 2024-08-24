package com.cybersoft.uniclub.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

public record AddProductRequest(
        String name,
        String desc,
        String information,
        double price,
        int idBrand,
        int idColor,
        int idSize,
        MultipartFile files,
        int quantity,
        double priceSize
) {
}
