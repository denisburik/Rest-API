package br.com.school.product.domain.product;

import br.com.school.product.domain.exception.NotificationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

class ProductEntityTest {

    @Test
    void sholdInstanceNewProduct() {
        final var expectedSku = "1";
        final var expectedName = "Product name test";
        final var expectedStock = BigDecimal.valueOf(10);
        final var expectedCost = BigDecimal.valueOf(20);
        final var expectedPrice = BigDecimal.valueOf(30);

        final var product = ProductEntity.create(expectedName, expectedSku, expectedStock, expectedPrice, expectedCost);
        Assertions.assertNotNull(product.getId());
        Assertions.assertEquals(expectedSku, product.getSkuCode());
        Assertions.assertEquals(expectedName, product.getName());
        Assertions.assertEquals(expectedStock, product.getStock());
        Assertions.assertEquals(expectedCost, product.getCost());
        Assertions.assertEquals(expectedPrice, product.getPrice());
    }

    private static Stream<Arguments> getProductArguments() {
        return Stream.of(
            Arguments.of("", "Product Test", BigDecimal.valueOf(1), BigDecimal.valueOf(20),
                    BigDecimal.valueOf(10), "Sku cannot be null or empty"),
            Arguments.of("1", "Prod   ", BigDecimal.valueOf(1), BigDecimal.valueOf(20),
                    BigDecimal.valueOf(10), "Name langth must be between 5 and 200 symbols"),
            Arguments.of("1", "Product Test", BigDecimal.valueOf(-1), BigDecimal.valueOf(20),
                    BigDecimal.valueOf(10), "Stock cannot be negative"),
            Arguments.of("1", "Product Test", BigDecimal.valueOf(1), BigDecimal.valueOf(10),
                    BigDecimal.valueOf(10), "Price must be greater then zero"),
            Arguments.of("1", "Product Test", BigDecimal.valueOf(1), BigDecimal.valueOf(20),
                    BigDecimal.valueOf(0), "Cost must be greater then zero")
        );
    }

    @ParameterizedTest
    @MethodSource("getProductArguments")
    void shouldNotInstanceNewProduct(String sku,
                                     String name,
                                     BigDecimal stock,
                                     BigDecimal price,
                                     BigDecimal cost,
                                     String message) {
        final var expectedError = Assertions.assertThrows(NotificationException.class,
            () -> ProductEntity.create(name, sku, stock, price, cost));

        Assertions.assertEquals(message, expectedError.getErrors().get(0).message());
    }

    @Test
    void sholdInstanceNewProductAndUpdate() {
        final var expectedSku = "1";
        final var expectedName = "Product name test";
        final var expectedStock = BigDecimal.valueOf(10);
        final var expectedCost = BigDecimal.valueOf(20);
        final var expectedPrice = BigDecimal.valueOf(30);

        final var product = ProductEntity.create( "Name Test test", "123", BigDecimal.valueOf(40),
                BigDecimal.valueOf(200), BigDecimal.valueOf(100));

        final var id = product.getId();

        product.update(expectedName, expectedSku, expectedStock, expectedPrice, expectedCost);

        Assertions.assertNotNull(product.getId());
        Assertions.assertEquals(id, product.getId());
        Assertions.assertEquals(expectedSku, product.getSkuCode());
        Assertions.assertEquals(expectedName, product.getName());
        Assertions.assertEquals(expectedStock, product.getStock());
        Assertions.assertEquals(expectedCost, product.getCost());
        Assertions.assertEquals(expectedPrice, product.getPrice());
    }

    @ParameterizedTest
    @MethodSource("getProductArguments")
    void shouldNotInstanceNewProductAndNotUpdate(String sku,
                                     String name,
                                     BigDecimal stock,
                                     BigDecimal price,
                                     BigDecimal cost,
                                     String message) {

        final var product = ProductEntity.create( "Name Test test", "123", BigDecimal.valueOf(40),
                BigDecimal.valueOf(200), BigDecimal.valueOf(100));

        final var expectedError = Assertions.assertThrows(NotificationException.class,
                () -> product.update(name, sku, stock, price, cost));

        Assertions.assertEquals(message, expectedError.getErrors().get(0).message());
    }

}