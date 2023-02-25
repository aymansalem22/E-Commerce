package com.oberla.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oberla.ecommerce.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {

}
