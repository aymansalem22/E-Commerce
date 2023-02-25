package com.oberla.ecommerce.controllers;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oberla.ecommerce.common.ApiResponse;
import com.oberla.ecommerce.dto.ProductDto;
import com.oberla.ecommerce.model.Category;
import com.oberla.ecommerce.model.Product;
import com.oberla.ecommerce.service.CategoryService;
import com.oberla.ecommerce.service.ProductService;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/product")
public class ProductController {
	
	@Autowired
	ProductService productService;
	
	@Autowired
	CategoryService categoryService;
	
	
	@GetMapping("/")
	public ResponseEntity<List<ProductDto>>  getProducts(){
		List<ProductDto> productDtos=productService.listproducts();
		return new ResponseEntity<>(productDtos,HttpStatus.OK);
		
	}
	
	
	@PostMapping("/add")
	public ResponseEntity<ApiResponse> addProduct(@RequestBody ProductDto productDto){
		Optional<Category> optionalCategory=categoryService.readCategory(productDto.getCategoryId());
		if(!optionalCategory.isPresent()) {
			return new ResponseEntity<ApiResponse>(new ApiResponse(false, "category is invalid"),HttpStatus.OK);
		}
		Category category=optionalCategory.get();
		productService.addProduct(productDto,category);
		return new ResponseEntity<>(new ApiResponse(true,"Product has been added"),HttpStatus.CREATED);
	}
	
	
	@PostMapping("/update/{productID}")
	public ResponseEntity<ApiResponse> updateProduct(@PathVariable("productID") Integer productID,
			@Valid @RequestBody ProductDto productDto) {

		Optional<Category> optionalCategory=categoryService.readCategory(productDto.getCategoryId());
		if(!optionalCategory.isPresent()) {
			return new ResponseEntity<ApiResponse>(new ApiResponse(false,"category is invalid"),HttpStatus.CONFLICT);
		}
		Category category=optionalCategory.get();
		productService.updateProduct(productID, productDto, category);
		return new ResponseEntity<>(new ApiResponse(true,"Product has been updated"),HttpStatus.OK);
	}
	
	


}














































