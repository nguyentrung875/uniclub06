package com.cybersoft.uniclub.entity.keys;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable //Serializable dùng để mã hóa đối tượng
public class IdProductCategory implements Serializable {
    @Column(name = "id_product")
    private int idProduct;

    @Column(name = "id_category")
    private int idCategory;
}
