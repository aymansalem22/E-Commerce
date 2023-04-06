package com.oberla.ecommerce.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oberla.ecommerce.dto.product.ProductDto;
import com.oberla.ecommerce.exceptions.ProductNotExistException;
import com.oberla.ecommerce.model.Category;
import com.oberla.ecommerce.model.Product;
import com.oberla.ecommerce.repository.ProductRepository;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;

	public void addProduct(ProductDto productDto, Category category) {
		Product product = getProductFromDto(productDto, category);
		productRepository.save(product);

	}

	public static Product getProductFromDto(ProductDto productDto, Category category) {
		Product product = new Product();
		product.setCategory(category);
		product.setDescription(productDto.getDescription());
		product.setImageUrl(productDto.getImageUrl());
		product.setPrice(productDto.getPrice());
		product.setName(productDto.getName());
		return product;
	}

	public List<ProductDto> listproducts() {
		List<Product> products = productRepository.findAll();
		List<ProductDto> productDtos = new ArrayList<>();

		for (Product product : products) {
			productDtos.add(new ProductDto(product));
		}
		return productDtos;
	}

	public Optional<Product> readProduct(Integer productID) {
		return productRepository.findById(productID);
	}

	public void updateProduct(Integer productID, @Valid ProductDto productDto, Category category) {
		Product product = getProductFromDto(productDto, category);
		product.setId(productID);
		productRepository.save(product);
	}

	public Product getProductById(Integer productId) throws ProductNotExistException {
		Optional<Product> optionalProduct = productRepository.findById(productId);
		if (!optionalProduct.isPresent())
			throw new ProductNotExistException("Product id is invalid " + productId);
		return optionalProduct.get();

	}

}
