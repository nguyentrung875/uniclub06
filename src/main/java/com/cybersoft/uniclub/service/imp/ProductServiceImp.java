package com.cybersoft.uniclub.service.imp;

import com.cybersoft.uniclub.controller.FileController;
import com.cybersoft.uniclub.dto.ColorDTO;
import com.cybersoft.uniclub.dto.ProductDTO;
import com.cybersoft.uniclub.dto.SizeDTO;
import com.cybersoft.uniclub.entity.*;
import com.cybersoft.uniclub.repository.ColorRepository;
import com.cybersoft.uniclub.repository.ProductRepository;
import com.cybersoft.uniclub.repository.SizeRepository;
import com.cybersoft.uniclub.repository.VariantRepository;
import com.cybersoft.uniclub.request.AddProductRequest;
import com.cybersoft.uniclub.service.FileService;
import com.cybersoft.uniclub.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.swing.text.html.Option;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductServiceImp implements ProductService {

    @Autowired
    private ServletContext servletContext;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private VariantRepository variantRepository;

    @Autowired
    private SizeRepository sizeRepository;

    @Autowired
    private ColorRepository colorRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private RedisTemplate redisTemplate;

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
    public List<ProductDTO> getProduct(int page) {
        Pageable pageable = PageRequest.of(page, 4);
        return productRepository.findAll(pageable).stream().map(item -> {
            ProductDTO productDTO = new ProductDTO();
            productDTO.setId(item.getId());
            productDTO.setName(item.getName());
            productDTO.setPrice(item.getPrice());
            if (item.getVariants().size() > 0) {
                productDTO.setImageLink(MvcUriComponentsBuilder.fromMethodName(FileController.class, "getFile",
                        item.getVariants().getFirst().getImages().split(",")[0]).build().toString());
            } else {
                productDTO.setImageLink("");
            }
            return productDTO;
        }).toList();

//        return productDTOList;
    }

//    @Cacheable("productDetail")
    @Override
    public ProductDTO getDetailProduct(int id) {
        ObjectMapper objectMapper = new ObjectMapper();

        ProductDTO productDTO1 = new ProductDTO();

        if (redisTemplate.hasKey(String.valueOf(id))) {
            System.out.println("Kiem tra co key");
            String data = redisTemplate.opsForValue().get(String.valueOf(id)).toString();
            try {
                productDTO1 = objectMapper.readValue(data, ProductDTO.class);
            } catch (Exception e) {
                throw new RuntimeException("Lỗi: " + e.getMessage());
            }
            return productDTO1;
        }

        System.out.println("Kiem tra ko co key");
        Optional<ProductEntity> optionalProduct = productRepository.findById(id);




        productDTO1 = optionalProduct.stream().map(productEntity -> {
            ProductDTO productDTO = new ProductDTO();
            productDTO.setId(productEntity.getId());
            productDTO.setName(productEntity.getName());
            productDTO.setPrice(productEntity.getPrice());
            productDTO.setOverview(productEntity.getDesc());

            String imageLink = MvcUriComponentsBuilder.fromMethodName(FileController.class, "getFile",
                    productEntity.getVariants().getFirst().getImages().split(",")[0]).build().toString();
            productDTO.setImageLink(imageLink);

            productDTO.setCategories(productEntity.getProductCategories().stream().map(
                    productCategory -> productCategory.getCategory().getName()
            ).toList());

//            var sizeList = productEntity.getVariants().stream().map(
//                    variantEntity -> {
//                        SizeDTO sizeDTO = new SizeDTO();
//                        sizeDTO.setId(variantEntity.getSize().getId());
//                        sizeDTO.setName(variantEntity.getSize().getName());
//                        return sizeDTO;
//                    }
//            ).collect(Collectors.toCollection(() -> new TreeSet<>(
//                    Comparator.comparing(SizeDTO::getId))));

//            var listSizeDTO = productEntity.getVariants().stream().map(
//                    variantEntity -> {
//                        SizeDTO sizeDTO = new SizeDTO();
//                        sizeDTO.setId(variantEntity.getSize().getId());
//                        sizeDTO.setName(variantEntity.getSize().getName());
//                        return sizeDTO;
//                    }).distinct().toList();
//            productDTO.setSizes(listSizeDTO);

//            productDTO.setSizes(sizeList.stream().toList());

            List<SizeDTO> listSizeDTO = sizeRepository.findAll().stream().map(sizeEntity -> {
                SizeDTO sizeDTO = new SizeDTO();
                sizeDTO.setId(sizeEntity.getId());
                sizeDTO.setName(sizeEntity.getName());
                return sizeDTO;
            }).toList();

            productDTO.setSizes(listSizeDTO);

            productDTO.setPriceColorSize(productEntity.getVariants().stream().map(
                    variantEntity -> {
                        ColorDTO colorDTO = new ColorDTO();
                        colorDTO.setId(variantEntity.getColor().getId());

                        colorDTO.setImages(Arrays.stream(variantEntity.getImages().split(",")).toList().stream().map(image -> {
                            String imglink = MvcUriComponentsBuilder.fromMethodName(FileController.class, "getFile",
                                    image).build().toString();
                            return imglink;

                        }).toList());

                        colorDTO.setName(variantEntity.getColor().getName());

                        colorDTO.setSizes(productEntity.getVariants().stream().map(variantEntity1 -> {

                            if (variantEntity.getColor().getId()==variantEntity1.getColor().getId()){
                                SizeDTO sizeDTO = new SizeDTO();
                                sizeDTO.setId(variantEntity1.getSize().getId());
                                sizeDTO.setName(variantEntity1.getSize().getName());
                                sizeDTO.setPrice(variantEntity1.getPrice());
                                sizeDTO.setSku(variantEntity1.getSku());
                                sizeDTO.setQuantity(variantEntity1.getQuantity());
                                return sizeDTO;
                            } else {
                                return null;
                            }

                        }).filter(Objects::nonNull).toList());

                        return colorDTO;
                    }
            ).distinct().toList());

            productDTO.setColors(colorRepository.findAll().stream().map(colorEntity -> {
                ColorDTO colorDTO = new ColorDTO();
                colorDTO.setId(colorEntity.getId());
                colorDTO.setName(colorEntity.getName());

                return colorDTO;
            }).toList());
            return productDTO;
        }).findFirst().orElseThrow(() -> new RuntimeException("Can not find data"));

        try {
            String json = objectMapper.writeValueAsString(productDTO1);
            redisTemplate.opsForValue().set(String.valueOf(id), json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Lỗi parse json: " + e.getMessage());
        }

        return productDTO1;
    }
}
