package com.oberla.ecommerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oberla.ecommerce.model.User;
import com.oberla.ecommerce.model.WishList;
import com.oberla.ecommerce.repository.WishListRepository;

@Service
public class WishListService {

	@Autowired
	WishListRepository wishListRepository;

	public void createWishList(WishList wishList) {
		wishListRepository.save(wishList);
	}

	public List<WishList> readWishList(User user) {
		return wishListRepository.findAllByUserOrderByCreatedDateDesc(user);
	}

}
