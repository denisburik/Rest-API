package br.com.school.product.api.product;

import br.com.school.product.domain.product.ProductEntity;

import java.math.BigDecimal;

public record ProductResponse(String id,
                              String skuCode,
                              String name,
                              BigDecimal stock,
                              BigDecimal price,
                              BigDecimal cost) {

    public static ProductResponse fromEntity(ProductEntity entity) {
        return new ProductResponse(entity.getId(),
                entity.getSkuCode(),
                entity.getName(),
                entity.getStock(),
                entity.getPrice(),
                entity.getCost());
    }
}
