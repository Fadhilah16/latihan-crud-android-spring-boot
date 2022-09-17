package com.crud.simplecrud.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.crud.simplecrud.models.ProductEntity;

@Repository
public interface ProductRepo extends JpaRepository<ProductEntity, Long> {
    
}
