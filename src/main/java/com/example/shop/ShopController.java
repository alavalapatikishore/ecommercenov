package com.example.shop;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api")
public class ShopController {
    private final ProductService products;
    private final CartService cart;

    public ShopController(ProductService products, CartService cart) {
        this.products = products;
        this.cart = cart;
    }

    @GetMapping("/products")
    public List<Product> products() {
        return products.getProducts();
    }

    @GetMapping("/cart")
    public CartResponse cart() {
        List<CartLine> items = cart.getItems();
        int count = items.stream().mapToInt(CartLine::quantity).sum();
        return new CartResponse(items, count, cart.getTotal());
    }

    @PostMapping("/cart/{productId}")
    public CartResponse add(@PathVariable Long productId) {
        try {
            cart.add(productId);
            return cart();
        } catch (NoSuchElementException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @DeleteMapping("/cart/{productId}")
    public CartResponse removeOne(@PathVariable Long productId) {
        cart.removeOne(productId);
        return cart();
    }

    @PostMapping("/checkout")
    public Map<String, Object> checkout() {
        BigDecimal total = cart.getTotal();
        int count = cart.getItems().stream().mapToInt(CartLine::quantity).sum();
        if (count == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Your cart is empty");
        }
        cart.clear();
        return Map.of(
                "message", "Order placed successfully!",
                "orderNumber", "DEMO-" + System.currentTimeMillis(),
                "itemsPurchased", count,
                "total", total
        );
    }

    public record CartResponse(List<CartLine> items, int itemCount, BigDecimal total) {}
}
