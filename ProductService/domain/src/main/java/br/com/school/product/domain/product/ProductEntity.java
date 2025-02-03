package br.com.school.product.domain.product;

import br.com.school.product.domain.exception.NotificationException;
import br.com.school.product.domain.validation.Error;
import br.com.school.product.domain.validation.NotificationValidation;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Entity
@Table(name = "product")
@NoArgsConstructor
public class ProductEntity {

    @Id
    private String id;
    private String skuCode;
    private String name;

    private BigDecimal stock;
    private BigDecimal price;
    private BigDecimal cost;

    public ProductEntity(String id,
                         String skuCode,
                         String name,
                         BigDecimal stock,
                         BigDecimal price,
                         BigDecimal cost) {
        this.id = id;
        this.skuCode = skuCode;
        this.name = name;
        this.stock = stock;
        this.price = price;
        this.cost = cost;

        selfValidate();
    }

    public static ProductEntity create(final String skuCode,
                                       final String name,
                                       final BigDecimal stock,
                                       final BigDecimal price,
                                       final BigDecimal cost) {
        final var id = UUID.randomUUID().toString();
        return new ProductEntity(id, skuCode, name, stock, price, cost);
    }

    public void update(final String skuCode,
                       final String name,
                       final BigDecimal stock,
                       final BigDecimal price,
                       final BigDecimal cost) {
        this.id = id;
        this.skuCode = skuCode;
        this.name = name;
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

        if (price.compareTo(cost) <= 0) {
            notification.append(new Error("Price must be greater then zero"));
        }

        if (notification.hasErrors()) {
            throw new NotificationException("Falied to instance new product entity", notification);
        }
    }
}
