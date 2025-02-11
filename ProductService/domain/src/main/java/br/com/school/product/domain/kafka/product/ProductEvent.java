package br.com.school.product.domain.kafka.product;

import br.com.school.product.domain.product.ProductEntity;

import java.math.BigDecimal;

public record ProductEvent(String id,
                           String skuCode,
                           String name,
                           BigDecimal stock,
                           EventType eventType) {

    public static ProductEvent create(ProductEntity entity, EventType eventType) {
        return new ProductEvent(entity.getId(),
                entity.getSkuCode(),
                entity.getName(),
                entity.getStock(),
                eventType);
    }
}
