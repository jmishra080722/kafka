package com.jay.controller;

import com.jay.dto.ProductEvent;
import com.jay.entity.Product;
import com.jay.service.ProductCommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductCommandController {

    @Autowired
    private ProductCommandService commandService;

    @PostMapping
    public Product createProduct( @RequestBody ProductEvent productEvent){
        return commandService.createProduct(productEvent);
    }


    @PutMapping("/{id}")
    public Product updateProduct( @PathVariable long id,  @RequestBody ProductEvent productEvent){
        return commandService.updateProduct(id, productEvent);
    }
}
