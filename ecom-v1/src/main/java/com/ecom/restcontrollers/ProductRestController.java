package com.ecom.restcontrollers;

import com.ecom.model.Product;
import com.ecom.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductRestController {

    @Autowired
    private ProductRepository productRepository;

    // ✅ PUBLIC - browse all products
    @GetMapping
    public ResponseEntity<List<Product>> list() {
        return ResponseEntity.ok(productRepository.findAll());
    }

    // ✅ PUBLIC - view single product
    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable Integer id) {
        return productRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
