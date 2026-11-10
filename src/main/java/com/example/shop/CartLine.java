package com.example.shop;

import java.math.BigDecimal;

public record CartLine(Long productId, String name, BigDecimal price, int quantity, String image) {
    public BigDecimal lineTotal() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}
