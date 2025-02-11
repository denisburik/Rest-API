package br.com.school.product.domain.product;

import br.com.school.product.domain.exception.NotFoundException;
import br.com.school.product.domain.kafka.product.EventType;
import br.com.school.product.domain.kafka.product.ProductEvent;
import br.com.school.product.domain.kafka.product.ProductProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository repository;
    private final ProductProducer producer;

    public ProductEntity createProduct(ProductEntity entity) {
        final var savedProduct = repository.save(entity);
        producer.sendProduct(ProductEvent.create(savedProduct, EventType.CREATE));

        return savedProduct;
    }

    public ProductEntity updateProduct(ProductEntity productFromDb, ProductEntity product) {
        productFromDb.update(product.getSkuCode(),
                product.getName(),
                product.getStock(),
                product.getPrice(),
                product.getCost());
        final var savedProduct = repository.save(productFromDb);
        producer.sendProduct(ProductEvent.create(savedProduct, EventType.UPDATE));

        return savedProduct;
    }

    public ProductEntity getProductById(String id) {
        return repository.findById(id).orElseThrow(() -> NotFoundException.create("Product with id %s not found".formatted(id)));
    }

    public void deleteProduct(String id) {
        final var productFromDb = getProductById(id);
        repository.delete(productFromDb);
        producer.sendProduct(ProductEvent.create(productFromDb, EventType.DELETE));
    }

    public Page<ProductEntity> findAllProducts(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
