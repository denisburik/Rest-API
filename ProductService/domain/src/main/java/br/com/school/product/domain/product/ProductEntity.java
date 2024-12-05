package br.com.school.product.domain.product;

import br.com.school.product.domain.exception.NotificationException;
import br.com.school.product.domain.validation.Error;
import br.com.school.product.domain.validation.NotificationValidation;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
public class ProductEntity {
    private String id;
    private String name;
    private String skuCode;

    private BigDecimal stock;
    private BigDecimal price;
    private BigDecimal cost;

    public ProductEntity(String id,
                         String name,
                         String skuCode,
                         BigDecimal stock,
                         BigDecimal price,
                         BigDecimal cost) {
        this.id = id;
        this.name = name;
        this.skuCode = skuCode;
        this.stock = stock;
        this.price = price;
        this.cost = cost;

        selfValidate();
    }

    public static ProductEntity create(final String name,
                                       final String skuCode,
                                       final BigDecimal stock,
                                       final BigDecimal price,
                                       final BigDecimal cost) {
        final var id = UUID.randomUUID().toString();
        return new ProductEntity(id, name, skuCode, stock, price, cost);
    }

    public void update(final String name,
                       final String skuCode,
                       final BigDecimal stock,
                       final BigDecimal price,
                       final BigDecimal cost) {
        this.id = id;
        this.name = name;
        this.skuCode = skuCode;
        this.stock = stock;
        this.price = price;
        this.cost = cost;
        selfValidate();
    }

    private void selfValidate() {
        final var notification = NotificationValidation.create();

        if (skuCode.isEmpty()) {
            notification.append(new Error("Sku cannot be null or empty"));
        }

        final var nameLength = name.trim().length();
        if (nameLength < 5 || nameLength > 200) {
            notification.append(new Error("Name langth must be between 5 and 200 symbols"));
        }

        if (stock.compareTo(BigDecimal.ZERO) < 0) {
            notification.append(new Error("Stock cannot be negative"));
        }

        if (cost.compareTo(BigDecimal.ZERO) <= 0) {
            notification.append(new Error("Cost must be greater then zero"));
        }

        if (price.compareTo(cost) < 0) {
            notification.append(new Error("Price must be greater then zero"));
        }

        if (notification.hasErrors()) {
            throw new NotificationException("Falied to instance new product entity", notification);
        }
    }
}
