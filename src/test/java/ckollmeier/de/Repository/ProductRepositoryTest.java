package ckollmeier.de.Repository;

import ckollmeier.de.Entity.Product;
import ckollmeier.de.Entity.ProductBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class ProductRepositoryTest {

    private ProductRepository productRepository;
    private Product testProduct1;
    private Product testProduct2;

    @BeforeEach
    void setUp() {
        productRepository = new ProductRepository();
        testProduct1 = ProductBuilder.builder().name("Coca-Cola").build();
        testProduct2 = ProductBuilder.builder().name("Fanta").build();
    }

    @Test
    void addProduct_shouldThrowNullPointerException_whenProductIsNull() {
        assertThrows(NullPointerException.class, () -> productRepository.addProduct(null));
    }

    @Test
    void addProduct_shouldAddProductWithUuidAsId_whenProductHasNoUuid() {
        Product addedProduct = productRepository.addProduct(testProduct1);
        assertNotNull(addedProduct.id());
        assertThat(addedProduct.id()).matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}");
    }

    @Test
    void addProduct_shouldAddProductWithUnchangedUuid_whenProductHasUuid() {
        Product productWithId = testProduct1.withId("12214309-08e7-4875-beb1-33da650e04f7");
        Product addedProduct = productRepository.addProduct(productWithId);
        assertThat(addedProduct.id()).isEqualTo("12214309-08e7-4875-beb1-33da650e04f7");
    }

    @Test
    void addProduct_shouldThrowInvalidArgumentException_whenProductWithIdAlreadyExists() {
        Product addedProduct = productRepository.addProduct(testProduct1);
        assertThrows(IllegalArgumentException.class, () -> productRepository.addProduct(addedProduct));
    }

    @Test
    void addProduct_shouldIncreaseProductCount_whenProductWasAdded() {
        productRepository.addProduct(testProduct1);
        productRepository.addProduct(testProduct2);
        assertThat(productRepository.countProducts()).isEqualTo(2);
    }

    @Test
    void removeProduct_shouldThrowNullPointerException_whenProductIsNull() {
        assertThrows(NullPointerException.class, () -> productRepository.removeProduct(null));
    }

    @Test
    void removeProduct_shouldThrowNullPointerException_whenProductIdIsNull() {
        Product product = ProductBuilder.builder().name("Cola").build();
        assertThrows(NullPointerException.class, () -> productRepository.removeProduct(product));
    }

    @Test
    void removeProduct_shouldReturnEmptyOptional_whenProductIsNotFound() {
        productRepository.addProduct(testProduct1.withId("prod-1"));
        assertThat(productRepository.removeProduct(testProduct2.withId("prod-2"))).isEmpty();
    }

    @Test
    void removeProduct_shouldDecreaseProductCount_whenProductWasRemoved() {
        Product addedProduct1 = productRepository.addProduct(testProduct1);
        productRepository.addProduct(testProduct2);
        productRepository.removeProduct(addedProduct1);
        assertThat(productRepository.countProducts()).isEqualTo(1);
    }

    @Test
    void removeProductWithId_shouldThrowNullPointerException_whenProductIsNull() {
        assertThrows(NullPointerException.class, () -> productRepository.removeProductWithId(null));
    }

    @Test
    void removeProductWithId_shouldReturnEmptyOptional_whenProductIsNotFound() {
        productRepository.addProduct(testProduct1.withId("prod-1"));
        assertThat(productRepository.removeProductWithId("prod-2")).isEmpty();
    }

    @Test
    void removeProductWithId_shouldDecreaseProductCount_whenProductWasRemoved() {
        Product addedProduct1 = productRepository.addProduct(testProduct1);
        productRepository.addProduct(testProduct2);
        productRepository.removeProductWithId(addedProduct1.id());
        assertThat(productRepository.countProducts()).isEqualTo(1);
    }

    @Test
    void find_shouldThrowNullPointerException_whenIdIsNull() {
        assertThrows(NullPointerException.class, () -> productRepository.find(null));
    }

    @Test
    void find_shouldReturnNull_whenIdIsNotInRepository() {
        productRepository.addProduct(testProduct1);
        assertThat(productRepository.find("xxxxxxx")).isEmpty();
    }

    @Test
    void find_shouldReturnProduct_whenIdIsInRepository() {
        Product addedProduct1 = productRepository.addProduct(testProduct1);
        productRepository.addProduct(testProduct2);
        assertThat(productRepository.find(addedProduct1.id())).isPresent().contains(addedProduct1);
    }

    @Test
    void findAll_shouldReturnAllProducts() {
        Product addedProduct1 = productRepository.addProduct(testProduct1);
        Product addedProduct2 = productRepository.addProduct(testProduct2);
        List<Product> allProducts = productRepository.findAll();
        assertThat(allProducts).hasSize(2);
        assertThat(allProducts).containsExactlyInAnyOrder(addedProduct1, addedProduct2);
    }

    @Test
    void findAll_shouldReturnEmptyList_whenNoProducts() {
        List<Product> allProducts = productRepository.findAll();
        assertThat(allProducts).isEmpty();
    }
}