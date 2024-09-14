package com.cybersoft.uniclub.entity;

import com.cybersoft.uniclub.entity.keys.IdProductCategory;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity(name = "product_category")
public class ProductCategoryEntity {

    @EmbeddedId
    private IdProductCategory id;

    @ManyToOne //nếu ko set nó sẽ đi ngược lại ProductEntity, tránh vòng lặp vô tận
    @JoinColumn(name = "id_product", updatable = false, insertable = false)
    private ProductEntity product;

    @ManyToOne
    @JoinColumn(name = "id_category", updatable = false, insertable = false)
    private CategoryEntity category;

}
