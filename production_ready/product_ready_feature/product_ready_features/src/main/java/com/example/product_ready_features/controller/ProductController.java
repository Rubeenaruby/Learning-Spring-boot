package com.example.product_ready_features.controller;

import com.example.product_ready_features.dtos.ProductDto;
import com.example.product_ready_features.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    @GetMapping
    public List<ProductDto> findAllProduct(){

        return  productService.getAllProduct();
    }
    @GetMapping("/{id}")
    public ProductDto findByProductId(@PathVariable Long id){

        return  productService.getBYProductId(id);
    }
    @PostMapping
    public ProductDto createNewProduct(@RequestBody ProductDto input){
        return  productService.createNewProduct(input);
    }
    @DeleteMapping("/{id}")
    public void deleteByproductId(@PathVariable Long id){
        productService.deleteByProductId(id);
    }


}
