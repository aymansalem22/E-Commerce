package com.oberla.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oberla.ecommerce.model.OrderItem;

@Repository
public interface OrderItemsRepository extends JpaRepository<OrderItem, Integer> {

}
