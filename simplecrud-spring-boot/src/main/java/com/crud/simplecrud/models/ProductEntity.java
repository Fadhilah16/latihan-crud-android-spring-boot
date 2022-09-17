package com.crud.simplecrud.models;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "product")
public class ProductEntity implements Serializable{
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank(message="name is required")
    @NotNull(message="name is required")
    @Column(length = 100)
    private String name;

    @NotNull(message="description is required")
    @Column(length = 500)
    private String description;

  
    @NotNull(message="price is required")
    private BigDecimal price;

    public ProductEntity(){}
    public ProductEntity(long id, String name, String description, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
    }


    public long getId() {
        return id;
    }


    public void setId(long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }


    public BigDecimal getPrice() {
        return price;
    }


    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
  
    
}
