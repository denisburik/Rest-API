package br.com.school.product.domain.product;

import br.com.school.product.domain.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository repository;

    public ProductEntity createProduct(ProductEntity entity) {
        return repository.save(entity);
    }

    public ProductEntity updateProduct(ProductEntity productFromDb, ProductEntity product) {
        productFromDb.update(product.getSkuCode(),
                product.getName(),
                product.getStock(),
                product.getPrice(),
                product.getCost());
        return repository.save(productFromDb);
    }

    public ProductEntity getProductById(String id) {
        return repository.findById(id).orElseThrow(() -> NotFoundException.create("Product with id %s not found".formatted(id)));
    }

    public void deleteProduct(String id) {
        final var productFromDb = getProductById(id);
        repository.delete(productFromDb);
    }

    public Page<ProductEntity> findAllProducts(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
