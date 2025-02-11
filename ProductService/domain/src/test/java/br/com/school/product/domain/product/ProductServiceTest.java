package br.com.school.product.domain.product;

import br.com.school.product.domain.exception.NotFoundException;
import br.com.school.product.domain.kafka.product.EventType;
import br.com.school.product.domain.kafka.product.ProductProducer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService service;

    @Mock
    private ProductRepository repository;

    @Mock
    private ProductProducer producer;

    @Test
    void shouldCreateNewProduct() {
        final var expectedSku = "1";
        final var expectedName = "Product name test";
        final var expectedStock = BigDecimal.valueOf(10);
        final var expectedCost = BigDecimal.valueOf(20);
        final var expectedPrice = BigDecimal.valueOf(30);

        final var product = ProductEntity.create(expectedSku, expectedName, expectedStock, expectedPrice, expectedCost);

        final var expectedId = product.getId();
        final var expectedEventType = EventType.CREATE;

        Mockito.when(repository.save(any())).thenReturn(product);
        Mockito.doNothing().when(producer).sendProduct(any());

        service.createProduct(product);

        Mockito.verify(repository, Mockito.times(1))
                .save(argThat(arg -> Objects.equals(expectedSku, arg.getSkuCode())
                        && Objects.equals(expectedName, arg.getName())
                        && Objects.equals(expectedStock, arg.getStock())
                        && Objects.equals(expectedPrice, arg.getPrice())
                        && Objects.equals(expectedCost, arg.getCost())));
        Mockito.verify(producer, Mockito.times(1))
                .sendProduct(argThat(arg -> Objects.equals(expectedId, arg.id())
                && Objects.equals(expectedSku, arg.skuCode())
                && Objects.equals(expectedName, arg.name())
                && Objects.equals(expectedStock, arg.stock())
                && Objects.equals(expectedEventType, arg.eventType())));
    }

    @Test
    void shouldUpdateProduct() {
        final var expectedSku = "1";
        final var expectedName = "Product name test";
        final var expectedStock = BigDecimal.valueOf(10);
        final var expectedCost = BigDecimal.valueOf(20);
        final var expectedPrice = BigDecimal.valueOf(30);

        final var productFromDb = ProductEntity.create("13", "expected name", BigDecimal.valueOf(100),
                BigDecimal.valueOf(300), BigDecimal.valueOf(120));

        final var expectedId = productFromDb.getId();
        final var expectedEventType = EventType.UPDATE;

        final var product = ProductEntity.with(productFromDb.getId(),
                expectedSku, expectedName, expectedStock, expectedPrice, expectedCost);

        Mockito.when(repository.save(any())).thenReturn(product);
        Mockito.doNothing().when(producer).sendProduct(any());

        service.updateProduct(productFromDb, product);

        Mockito.verify(repository, Mockito.times(1))
                .save(argThat(arg -> Objects.equals(expectedSku, arg.getSkuCode())
                        && Objects.equals(expectedName, arg.getName())
                        && Objects.equals(expectedStock, arg.getStock())
                        && Objects.equals(expectedPrice, arg.getPrice())
                        && Objects.equals(expectedCost, arg.getCost())));
        Mockito.verify(producer, Mockito.times(1))
                .sendProduct(argThat(arg -> Objects.equals(expectedId, arg.id())
                        && Objects.equals(expectedSku, arg.skuCode())
                        && Objects.equals(expectedName, arg.name())
                        && Objects.equals(expectedStock, arg.stock())
                        && Objects.equals(expectedEventType, arg.eventType())));
    }

    @Test
    void shouldReturnProductGetById() {
        final var expectedSku = "1";
        final var expectedName = "Product name test";
        final var expectedStock = BigDecimal.valueOf(10);
        final var expectedCost = BigDecimal.valueOf(20);
        final var expectedPrice = BigDecimal.valueOf(30);

        final var product = ProductEntity.create(expectedSku, expectedName, expectedStock, expectedPrice, expectedCost);

        Mockito.when(repository.findById(any())).thenReturn(Optional.of(product));

        final var actualProduct = service.getProductById("12");

        Assertions.assertTrue(
                Objects.equals(expectedSku, actualProduct.getSkuCode())
                    && Objects.equals(expectedName, actualProduct.getName())
                    && Objects.equals(expectedStock, actualProduct.getStock())
                    && Objects.equals(expectedCost, actualProduct.getCost())
                    && Objects.equals(expectedPrice, actualProduct.getPrice())
        );

        Mockito.verify(repository, Mockito.times(1))
                .findById("12");
    }

    @Test
    void shouldNotReturnProductWhenProductNotFound() {
        final var exceptionMessage = "Product with id 12 not found";
        Mockito.when(repository.findById(any())).thenReturn(Optional.empty());

        final var exception = Assertions.assertThrows(NotFoundException.class, () -> service.getProductById("12"));

        Assertions.assertEquals(exceptionMessage, exception.getMessage());

        Mockito.verify(repository, Mockito.times(1))
                .findById("12");
    }

    @Test
    void shouldDeleteProduct() {
        final var expectedSku = "1";
        final var expectedName = "Product name test";
        final var expectedStock = BigDecimal.valueOf(10);
        final var expectedCost = BigDecimal.valueOf(20);
        final var expectedPrice = BigDecimal.valueOf(30);

        final var product = ProductEntity.create(expectedSku, expectedName, expectedStock, expectedPrice, expectedCost);

        final var expectedId = product.getId();
        final var expectedEventType = EventType.DELETE;

        Mockito.when(repository.findById(any())).thenReturn(Optional.of(product));
        Mockito.doNothing().when(repository).delete(any());
        Mockito.doNothing().when(producer).sendProduct(any());

        service.deleteProduct("12");

        Mockito.verify(repository, Mockito.times(1))
                .findById("12");
        Mockito.verify(repository, Mockito.times(1))
                .delete(any());

        Mockito.verify(producer, Mockito.times(1))
                .sendProduct(argThat(arg -> Objects.equals(expectedId, arg.id())
                        && Objects.equals(expectedSku, arg.skuCode())
                        && Objects.equals(expectedName, arg.name())
                        && Objects.equals(expectedStock, arg.stock())
                        && Objects.equals(expectedEventType, arg.eventType())));
    }

    @Test
    void shouldNotDeleteWhenProductNotFound() {
        final var exceptionMessage = "Product with id 12 not found";
        Mockito.when(repository.findById(any())).thenReturn(Optional.empty());

        final var exception = Assertions.assertThrows(NotFoundException.class, () -> service.deleteProduct("12"));

        Assertions.assertEquals(exceptionMessage, exception.getMessage());

        Mockito.verify(repository, Mockito.times(1))
                .findById("12");
        Mockito.verify(repository, Mockito.never()).delete(any());
    }

    @Test
    void shouldFindAll() {
        final var expectedSku = "1";
        final var expectedName = "Product name test";
        final var expectedStock = BigDecimal.valueOf(10);
        final var expectedCost = BigDecimal.valueOf(20);
        final var expectedPrice = BigDecimal.valueOf(30);

        final var product = ProductEntity.create(expectedSku, expectedName, expectedStock, expectedPrice, expectedCost);
        final var page = new PageImpl<>(List.of(product));

        Mockito.when(repository.findAll(any(Pageable.class))).thenReturn(page);

        final var pageReturn = service.findAllProducts(Pageable.ofSize(1));
        Assertions.assertNotNull(pageReturn);
        Assertions.assertEquals(product.getId(), pageReturn.get().toList().get(0).getId());

        Mockito.verify(repository, Mockito.times(1))
                .findAll(any(Pageable.class));
    }

}