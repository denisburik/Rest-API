package br.com.school.product.api.product;

import br.com.school.product.domain.product.ProductEntity;

import java.math.BigDecimal;

public record ProductRequest(String skuCode,
                             String name,
                             BigDecimal stock,
                             BigDecimal price,
                             BigDecimal cost) {
    public ProductEntity toEntity() {
        return ProductEntity.create(skuCode, name, stock, price, cost);
    }

    public ProductEntity toEntity(String id) {
        return ProductEntity.with(id, skuCode, name, stock, price, cost);
    }
}
