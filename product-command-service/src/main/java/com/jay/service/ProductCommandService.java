package com.jay.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jay.dto.ProductEvent;
import com.jay.entity.Product;
import com.jay.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProductCommandService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public Product createProduct(ProductEvent productEvent){
        Product productDO = productRepository.save(productEvent.getProduct());
        ProductEvent event = new ProductEvent("CreateEvent", productDO);

        try {
            // Log the serialized JSON to verify
            String json = objectMapper.writeValueAsString(event);
            System.out.println("Serialized JSON: " + json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        kafkaTemplate.send("product-event-topic", event);
        return productDO;
    }


    public Product updateProduct(long id, ProductEvent productEvent){
        Product existingProduct = productRepository.findById(id).get();
        Product newProduct = productEvent.getProduct();
        existingProduct.setName(newProduct.getName());
        existingProduct.setDescription(newProduct.getDescription());
        existingProduct.setPrice(newProduct.getPrice());
        Product productDO = productRepository.save(existingProduct);

        ProductEvent event = new ProductEvent("UpdateEvent", productDO);
        kafkaTemplate.send("product-event-topic", event);
        return productDO;
    }
}
