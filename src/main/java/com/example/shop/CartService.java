package com.example.shop;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class CartService {
    private final Map<Long, Integer> quantities = new LinkedHashMap<>();
    private final ProductService products;

    public CartService(ProductService products) {
        this.products = products;
    }

    public synchronized List<CartLine> getItems() {
        List<CartLine> lines = new ArrayList<>();
        quantities.forEach((id, quantity) -> {
            Product p = products.getProduct(id);
            lines.add(new CartLine(p.id(), p.name(), p.price(), quantity, p.image()));
        });
        return lines;
    }

    public synchronized void add(Long productId) {
        products.getProduct(productId); // validates that the product exists
        quantities.merge(productId, 1, Integer::sum);
    }

    public synchronized void removeOne(Long productId) {
        Integer quantity = quantities.get(productId);
        if (quantity == null) return;
        if (quantity <= 1) quantities.remove(productId);
        else quantities.put(productId, quantity - 1);
    }

    public synchronized void clear() {
        quantities.clear();
    }

    public synchronized BigDecimal getTotal() {
        return getItems().stream()
                .map(CartLine::lineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
