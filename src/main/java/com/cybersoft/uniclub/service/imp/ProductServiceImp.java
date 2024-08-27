package com.cybersoft.uniclub.service.imp;

import com.cybersoft.uniclub.dto.ProductDTO;
import com.cybersoft.uniclub.entity.*;
import com.cybersoft.uniclub.repository.ProductRepository;
import com.cybersoft.uniclub.repository.VariantRepository;
import com.cybersoft.uniclub.request.AddProductRequest;
import com.cybersoft.uniclub.service.FileService;
import com.cybersoft.uniclub.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductServiceImp implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private VariantRepository variantRepository;

    @Autowired
    private FileService fileService;

    //(rollbackFor = {Exception.class}) bắt exception
    @Transactional //chỉ dành cho tính năng thêm/xóa/sửa
    @Override
    public void addProduct(AddProductRequest request) {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setName(request.name());
        productEntity.setDesc(request.desc());
        productEntity.setInfo(request.information());
        productEntity.setPrice(request.price());

        //Thêm id_brand vào product
        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setId(request.idBrand());
        productEntity.setBrand(brandEntity);

        ProductEntity productInserted = productRepository.save(productEntity);

        VariantEntity variantEntity = new VariantEntity();
        variantEntity.setProduct(productEntity);

        ColorEntity colorEntity = new ColorEntity();
        colorEntity.setId(request.idColor());
        variantEntity.setColor(colorEntity);

        SizeEntity sizeEntity = new SizeEntity();
        sizeEntity.setId(request.idSize());
        variantEntity.setSize(sizeEntity);
        variantEntity.setPrice(request.price());
        variantEntity.setQuantity(request.quantity());
        variantEntity.setImages(request.files().getOriginalFilename());

        variantRepository.save(variantEntity);

        fileService.save(request.files());
    }

    @Override
    public List<ProductDTO> getProduct() {

        return productRepository.findAll().stream().map(item -> {
            ProductDTO productDTO = new ProductDTO();
            productDTO.setName(item.getName());
            productDTO.setPrice(item.getPrice());
            if (item.getVariants().size()>0){
                productDTO.setImageLink("http://localhost:8080/file/" + item.getVariants().getFirst().getImages());
            }else {
                productDTO.setImageLink("");
            }
            return productDTO;
        }).toList();

//        return productDTOList;
    }
}
