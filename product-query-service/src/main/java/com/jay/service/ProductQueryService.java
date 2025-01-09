package com.jay.service;

import com.jay.dto.ProductEvent;
import com.jay.entity.Product;
import com.jay.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import javax.sound.sampled.Port;
import java.util.List;

@Service
public class ProductQueryService {

    @Autowired
    private ProductRepository repository;

    public List<Product> getProducts(){
        return repository.findAll();
    }

    @KafkaListener(topics = "product-event-topic", groupId = "product-event-group")
    public void processProductEvents(ProductEvent productEvent){
        System.out.println("******************"+productEvent.getProduct().getName());
        Product product = productEvent.getProduct();
        if(productEvent.getEventType().equals("CreateEvent")){
            repository.save(product);
        }

        if(productEvent.getEventType().equals("UpdateEvent")){
            Product existingProduct = repository.findById(product.getId()).get();
            existingProduct.setName(product.getName());
            existingProduct.setDescription(product.getDescription());
            existingProduct.setPrice(product.getPrice());
            repository.save(existingProduct);
        }
    }
}
