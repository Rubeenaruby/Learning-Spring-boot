package com.example.product_ready_features.service;

import com.example.product_ready_features.dtos.ProductDto;
import com.example.product_ready_features.entity.ProductEntity;
import com.example.product_ready_features.exceptions.ResourceNotFoundException;
import com.example.product_ready_features.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    //Get All product
    public List<ProductDto> getAllProduct(){
        return productRepository.findAll() //Fetches all records from the database.
                .stream() //Converts the list into a Stream for processing.
                .map(productEntity -> modelMapper.map(productEntity, ProductDto.class)) // Convet entity into the DTO
                .collect(Collectors.toList()); //Collects all DTOs into a list.

    }

    // Create
    public ProductDto createNewProduct(ProductDto input){ //Receives product data from the controller.
        ProductEntity productEntity=modelMapper.map(input,ProductEntity.class);// Convert into DTO
        ProductEntity savedProductEntity=productRepository.save(productEntity);
        return modelMapper.map(savedProductEntity,ProductDto.class);
    }

   //Get By Id
    public ProductDto getBYProductId(Long id){

        ProductEntity productEntity=productRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Product not Found with id : "+ id));
        return modelMapper.map(productEntity,ProductDto.class);
    }

    public void deleteByProductId(Long id){
        if(!productRepository.existsById(id)){
           throw new  ResourceNotFoundException("Product not exist by that id:"+id);
        }
        productRepository.deleteById(id);
    }

}
