package com.cybersoft.uniclub.controller;

import com.cybersoft.uniclub.request.AddProductRequest;
import com.cybersoft.uniclub.response.BaseResponse;
import com.cybersoft.uniclub.service.FileService;
import com.cybersoft.uniclub.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private FileService fileService;

    @Autowired
    private ProductService productService;

    @GetMapping("/{page}")
    public ResponseEntity<?> getProduct(@PathVariable int page){
        BaseResponse response = new BaseResponse();
        response.setData(productService.getProduct(page));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getDetailProduct(@PathVariable int id, HttpServletRequest request){
        BaseResponse response = new BaseResponse();
        response.setStatusCode(200);
        response.setData(productService.getDetailProduct(id));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addProduct(AddProductRequest request) {
        System.out.println("Hello add product");
//        System.out.println("kiemtra" + request.name());
        productService.addProduct(request);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatusCode(200);
        baseResponse.setMessage("Success! ");

        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }
}
