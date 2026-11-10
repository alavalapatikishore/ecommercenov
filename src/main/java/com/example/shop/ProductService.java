package com.example.shop;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProductService {
    private final List<Product> products = List.of(
        new Product(1L, "Wireless Headphones", "Comfortable headphones for music and calls.",
                new BigDecimal("2499.00"), "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=700"),
        new Product(2L, "Smart Watch", "Track your daily activity and notifications.",
                new BigDecimal("3999.00"), "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=700"),
        new Product(3L, "Running Shoes", "Lightweight shoes for everyday running.",
                new BigDecimal("1899.00"), "https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=700"),
        new Product(4L, "Backpack", "A practical backpack for work and travel.",
                new BigDecimal("1299.00"), "https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=700")
    );

    public List<Product> getProducts() {
        return products;
    }

    public Product getProduct(Long id) {
        return products.stream()
                .filter(product -> product.id().equals(id))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Product " + id + " was not found"));
    }
}
