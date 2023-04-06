package com.oberla.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oberla.ecommerce.model.User;
import com.oberla.ecommerce.model.WishList;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Integer> {

	List<WishList> findAllByUserOrderByCreatedDateDesc(User user);

}
