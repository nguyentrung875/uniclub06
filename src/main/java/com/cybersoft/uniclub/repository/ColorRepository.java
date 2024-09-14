package com.cybersoft.uniclub.repository;

import com.cybersoft.uniclub.entity.ColorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColorRepository extends JpaRepository<ColorEntity, Integer> {
}
