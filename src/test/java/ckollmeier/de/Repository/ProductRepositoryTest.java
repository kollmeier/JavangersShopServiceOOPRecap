package ckollmeier.de.Repository;

import ckollmeier.de.Entity.Product;
import ckollmeier.de.Entity.ProductBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

class ProductRepositoryTest {

    @Test
    void addProductShouldThrowIllegalArgumentExceptionWhenProductIsNull() {
        ProductRepository productRepository = new ProductRepository();

        assertThrows(IllegalArgumentException.class, () -> {
            productRepository.addProduct(null);
        });
    }

    @Test
    void addProductShouldAddProductWithUuidAsIdWhenProductHasNoUuid() {
        ProductRepository productRepository = new ProductRepository();
        Product product = ProductBuilder.builder()
                .name("Coca-Cola").build();

        Product addedProduct = productRepository.addProduct(product);

        assertNotNull(product, addedProduct.id());
        assertThat(addedProduct.id()).matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}");
    }

    @Test
    void addProductShouldAddProductWithUnchangedUuidWhenProductHasUuid() {
        ProductRepository productRepository = new ProductRepository();
        Product product = ProductBuilder.builder()
                .name("Coca-Cola")
                .id("12214309-08e7-4875-beb1-33da650e04f7")
                .build();

        Product addedProduct = productRepository.addProduct(product);

        assertNotNull(product, addedProduct.id());
        assertThat(addedProduct.id()).isEqualTo("12214309-08e7-4875-beb1-33da650e04f7");
    }

    @Test
    void addProductShouldThrowInvalidArgumentExceptionWhenProductWithIdAlreadyExists() {
        ProductRepository productRepository = new ProductRepository();
        Product product = ProductBuilder.builder()
                .name("Coca-Cola")
                .build();

        Product addedProduct = productRepository.addProduct(product);

        assertThrows(IllegalArgumentException.class, () -> {
            productRepository.addProduct(addedProduct);
        });
    }

    @Test
    void addProductShouldIncreaseProductCountWhenProductWasAdded() {
        ProductRepository productRepository = new ProductRepository();
        Product product = ProductBuilder.builder()
                .name("Coca-Cola")
                .build();

        Product addedProduct = productRepository.addProduct(product);

        Product product2 = ProductBuilder.builder()
                .name("Fanta")
                .build();

        Product addedProduct2 = productRepository.addProduct(product2);

        assertThat(productRepository.countProducts()).isEqualTo(2);
    }

    @Test
    void removeProductShouldThrowIllegalArgumentExceptionWhenProductIsNull() {
        ProductRepository productRepository = new ProductRepository();
        assertThrows(IllegalArgumentException.class, () -> {
            productRepository.removeProduct(null);
        });
    }

    @Test
    void removeProductShouldThrowIllegalArgumentExceptionWhenProductIdIsNull() {
        ProductRepository productRepository = new ProductRepository();

        Product product = ProductBuilder.builder()
                .name("Cola").build();

        assertThrows(IllegalArgumentException.class, () -> {
            productRepository.removeProduct(product);
        });
    }

    @Test
    void removeProductShouldThrowIllegalArgumentExceptionWhenProductIsNotFound() {
        ProductRepository productRepository = new ProductRepository();

        Product product = ProductBuilder.builder()
                .name("Cola").build();

        productRepository.addProduct(product);

        Product product2 = ProductBuilder.builder()
                .name("Fanta").build();

        assertThrows(IllegalArgumentException.class, () -> {
            productRepository.removeProduct(product2);
        });
    }

    @Test
    void removeProductShouldDecreaseProductCountWhenProductWasRemoved() {
        ProductRepository productRepository = new ProductRepository();
        Product product = ProductBuilder.builder()
                .name("Coca-Cola")
                .build();

        Product addedProduct = productRepository.addProduct(product);

        Product product2 = ProductBuilder.builder()
                .name("Fanta")
                .build();

        Product addedProduct2 = productRepository.addProduct(product2);

        productRepository.removeProduct(addedProduct);

        assertThat(productRepository.countProducts()).isEqualTo(1);
    }

    @Test
    void removeProductWithIdShouldThrowIllegalArgumentExceptionWhenProductIsNull() {
        ProductRepository productRepository = new ProductRepository();
        assertThrows(IllegalArgumentException.class, () -> {
            productRepository.removeProductWithId(null);
        });
    }

    @Test
    void removeProductWithIdShouldThrowIllegalArgumentExceptionWhenProductIsNotFound() {
        ProductRepository productRepository = new ProductRepository();

        Product product = ProductBuilder.builder()
                .name("Cola").build();

        productRepository.addProduct(product);

        Product product2 = ProductBuilder.builder()
                .name("Fanta").build();

        assertThrows(IllegalArgumentException.class, () -> {
            productRepository.removeProductWithId(product2.id());
        });
    }

    @Test
    void removeProductWithIdShouldDecreaseProductCountWhenProductWasRemoved() {
        ProductRepository productRepository = new ProductRepository();
        Product product = ProductBuilder.builder()
                .name("Coca-Cola")
                .build();

        Product addedProduct = productRepository.addProduct(product);

        Product product2 = ProductBuilder.builder()
                .name("Fanta")
                .build();

        Product addedProduct2 = productRepository.addProduct(product2);

        productRepository.removeProductWithId(addedProduct.id());

        assertThat(productRepository.countProducts()).isEqualTo(1);
    }

    @Test
    void findShouldThrowIllegalArgumentExceptionWhenIdIsNull() {
        ProductRepository productRepository = new ProductRepository();
        assertThrows(IllegalArgumentException.class, () -> {
            productRepository.find(null);
        });
    }

    @Test
    void findShouldReturnNullWhenIdIsNotInRepository() {
        ProductRepository productRepository = new ProductRepository();

        Product product = ProductBuilder.builder()
                .name("Coca-Cola")
                .build();

        Product addedProduct = productRepository.addProduct(product);

        assertThat(productRepository.find("xxxxxxx")).isNull();
    }

    @Test
    void findShouldReturnProductWhenIdIsInRepository() {
        ProductRepository productRepository = new ProductRepository();

        Product product = ProductBuilder.builder()
                .name("Coca-Cola")
                .build();
        Product addedProduct = productRepository.addProduct(product);


        Product product2 = ProductBuilder.builder()
                .name("Fanta")
                .build();
        Product addedProduct2 = productRepository.addProduct(product);

        assertThat(productRepository.find(addedProduct.id())).isEqualTo(addedProduct);
    }

    @Test
    void findAll() {
        ProductRepository productRepository = new ProductRepository();

        Product product = ProductBuilder.builder()
                .name("Coca-Cola")
                .build();
        Product addedProduct = productRepository.addProduct(product);


        Product product2 = ProductBuilder.builder()
                .name("Fanta")
                .build();
        Product addedProduct2 = productRepository.addProduct(product);

        assertThat(productRepository.findAll()).hasSize(2);
    }
}