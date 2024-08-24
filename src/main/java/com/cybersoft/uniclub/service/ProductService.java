package com.cybersoft.uniclub.service;

import com.cybersoft.uniclub.request.AddProductRequest;
import org.springframework.stereotype.Service;

@Service
public interface ProductService {
    void addProduct(AddProductRequest request);
}
