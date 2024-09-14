package com.cybersoft.uniclub.service;

import com.cybersoft.uniclub.dto.ProductDTO;
import com.cybersoft.uniclub.request.AddProductRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {
    void addProduct(AddProductRequest request);
    List<ProductDTO> getProduct(int page);

    ProductDTO getDetailProduct(int id);
}
