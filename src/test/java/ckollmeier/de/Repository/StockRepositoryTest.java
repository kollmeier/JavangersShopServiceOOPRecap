package ckollmeier.de.Repository;

import ckollmeier.de.Entity.Interface.ProductInterface;
import ckollmeier.de.Entity.Product;
import ckollmeier.de.Entity.StockArticle;
import ckollmeier.de.Enum.UnitEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StockRepositoryTest {

    private StockRepository stockRepository;
    private ProductInterface product;

    @BeforeEach
    void setUp() {
        stockRepository = new StockRepository();
        product = mock(ProductInterface.class);
        when(product.id()).thenReturn("prod-1");
        when(product.productId()).thenReturn("prod-1");
        when(product.name()).thenReturn("Test Product");
        when(product.description()).thenReturn("Test Description");
        when(product.content()).thenReturn(BigDecimal.TEN.setScale(2, RoundingMode.HALF_UP));
        when(product.unit()).thenReturn(UnitEnum.KG);

        stockRepository.addProduct(product, new BigDecimal("10.0"), UnitEnum.KG, new BigDecimal("19.99"));
    }

    @Test
    void isInStock_shouldReturnTrue_whenProductExists() {
        assertTrue(stockRepository.isInStock(product));
    }

    @Test
    void isInStock_shouldReturnFalse_whenProductNotExists() {
        ProductInterface otherProduct = mock(ProductInterface.class);
        when(otherProduct.id()).thenReturn("non-existing-product");

        assertFalse(stockRepository.isInStock(otherProduct));
    }

    @Test
    void isSufficientInStock_shouldReturnTrue_whenEnoughQuantityAvailable() {
        assertTrue(stockRepository.isSufficientInStock(product, new BigDecimal("5.0"), UnitEnum.KG));
    }

    @Test
    void isSufficientInStock_shouldReturnFalse_whenNotEnoughQuantityAvailable() {
        assertFalse(stockRepository.isSufficientInStock(product, new BigDecimal("15.0"), UnitEnum.KG));
    }

    @Test
    void isSufficientInStock_shouldReturnFalse_whenProductNotInStock() {
        ProductInterface otherProduct = mock(ProductInterface.class);
        when(otherProduct.id()).thenReturn("other-prod");
        when(otherProduct.productId()).thenReturn("other-prod");
        assertFalse(stockRepository.isSufficientInStock(otherProduct, new BigDecimal("1.0"), UnitEnum.KG));
    }

    @Test
    void increaseQuantity_shouldAddToExistingQuantity() {
        StockArticle updated = stockRepository.increaseQuantity(product, new BigDecimal("5.0"), UnitEnum.KG);
        assertThat(new BigDecimal("15.0").setScale(updated.quantity().scale(), RoundingMode.HALF_UP)).isEqualTo(updated.quantity());
    }

    @Test
    void increaseQuantity_shouldAddNonExistingProduct_whenProductNotInStock() {
        ProductInterface otherProduct = mock(ProductInterface.class);
        when(otherProduct.id()).thenReturn("other-prod");
        when(otherProduct.productId()).thenReturn("other-prod");
        when(otherProduct.name()).thenReturn("Test Product");
        when(otherProduct.description()).thenReturn("Test Description");
        when(otherProduct.content()).thenReturn(BigDecimal.TEN.setScale(2, RoundingMode.HALF_UP));
        when(otherProduct.unit()).thenReturn(UnitEnum.KG);
        StockArticle updated = stockRepository.increaseQuantity(otherProduct, new BigDecimal("5.0"), UnitEnum.KG);
        assertThat(stockRepository.isInStock(otherProduct)).isTrue();
        assertThat(new BigDecimal("5.0").setScale(updated.quantity().scale(), RoundingMode.HALF_UP)).isEqualTo(updated.quantity());
    }

    @Test
    void decreaseQuantity_shouldSubtractFromExistingQuantity() {
        StockArticle updated = stockRepository.decreaseQuantity(product, new BigDecimal("3.0"), UnitEnum.KG);
        assertEquals(new BigDecimal("7.0").setScale(updated.quantity().scale(), RoundingMode.HALF_UP), updated.quantity());
    }

    @Test
    void decreaseQuantity_shouldThrowException_whenQuantityTooHigh() {
        assertThrows(IllegalArgumentException.class,
                () -> stockRepository.decreaseQuantity(product, new BigDecimal("20.0"), UnitEnum.KG));
    }

    @Test
    void decreaseQuantity_shouldThrowException_whenQuantityIsNegative() {
        assertThrows(IllegalArgumentException.class,
                () -> stockRepository.decreaseQuantity(product, new BigDecimal("-2.0"), UnitEnum.KG));
    }

    @Test
    void find_shouldReturnNull_whenIdNotStored() {
        Optional<StockArticle> found = stockRepository.find("non-existent-id");
        assertTrue(found.isEmpty());
    }

    @Test
    void findByProductId_shouldReturnStockArticle_whenProductExists() {
        Optional<StockArticle> found = stockRepository.findByProductId("prod-1");
        assertTrue(found.isPresent());
    }

    @Test
    void findByProductId_shouldReturnNull_whenProductNotExists() {
        Optional<StockArticle> found = stockRepository.findByProductId("non-existent-product");
        assertTrue(found.isEmpty());
    }

    @Test
    void addProduct_shouldAddStockArticleSuccessfully() {
        StockArticle stockArticle = mock(StockArticle.class);
        when(stockArticle.id()).thenReturn("new-id");
        when(stockArticle.product()).thenReturn(mock(Product.class));

        StockArticle added = stockRepository.addProduct(stockArticle);

        assertNotNull(added);
        assertEquals("new-id", added.id());
    }

    @Test
    void addProduct_shouldThrowException_whenStockArticleAlreadyExists() {
        StockArticle stockArticle = mock(StockArticle.class);
        when(stockArticle.id()).thenReturn("existing-id");
        when(stockArticle.product()).thenReturn(mock(Product.class));

        // Erstes Hinzufügen
        stockRepository.addProduct(stockArticle);

        // Zweites Hinzufügen sollte fehlschlagen
        assertThrows(IllegalArgumentException.class, () -> stockRepository.addProduct(stockArticle));
    }

    @Test
    void addProduct_shouldThrowException_whenQuantityIsNegative() {
        assertThrows(IllegalArgumentException.class,
                () -> stockRepository.addProduct(product, new BigDecimal("-5.0"), UnitEnum.KG, new BigDecimal("19.99")));
    }

    @Test
    void addProduct_shouldAddProductSuccessfully_whenValid() {
        ProductInterface newProduct = mock(ProductInterface.class);
        when(newProduct.id()).thenReturn("prod-2");
        when(newProduct.productId()).thenReturn("prod-2");
        when(newProduct.name()).thenReturn("Test Product");
        when(newProduct.description()).thenReturn("Test Description");
        when(newProduct.content()).thenReturn(BigDecimal.TEN.setScale(2, RoundingMode.HALF_UP));
        when(newProduct.unit()).thenReturn(UnitEnum.KG);

        StockArticle added = stockRepository.addProduct(newProduct, new BigDecimal("5.0"), UnitEnum.L, new BigDecimal("10.99"));

        assertNotNull(added);
        assertEquals("prod-2", added.productId());
        assertThat(new BigDecimal("5.0")).isEqualTo(added.quantity());
    }

    @Test
    void addProduct_shouldThrowException_whenProductIsNull() {
        assertThrows(NullPointerException.class, () -> stockRepository.addProduct(null, new BigDecimal("5.0"), UnitEnum.L, new BigDecimal("10.99")));
    }

    @Test
    void addProduct_shouldThrowException_whenQuantityIsNull() {
        assertThrows(NullPointerException.class, () -> stockRepository.addProduct(product, null, UnitEnum.L, new BigDecimal("10.99")));
    }

    @Test
    void addProduct_shouldThrowException_whenUnitIsNull() {
        assertThrows(NullPointerException.class, () -> stockRepository.addProduct(product, new BigDecimal("5.0"), null, new BigDecimal("10.99")));
    }

    @Test
    void addProduct_shouldThrowException_whenPriceIsNull() {
        assertThrows(NullPointerException.class, () -> stockRepository.addProduct(product, new BigDecimal("5.0"), UnitEnum.L, null));
    }

    @Test
    void increaseQuantity_shouldThrowException_whenProductIsNull() {
        assertThrows(NullPointerException.class, () -> stockRepository.increaseQuantity(null, new BigDecimal("5.0"), UnitEnum.L));
    }

    @Test
    void increaseQuantity_shouldThrowException_whenQuantityIsNull() {
        assertThrows(NullPointerException.class, () -> stockRepository.increaseQuantity(product, null, UnitEnum.L));
    }

    @Test
    void increaseQuantity_shouldThrowException_whenUnitIsNull() {
        assertThrows(NullPointerException.class, () -> stockRepository.increaseQuantity(product, new BigDecimal("5.0"), null));
    }

    @Test
    void decreaseQuantity_shouldThrowException_whenProductIsNull() {
        assertThrows(NullPointerException.class, () -> stockRepository.decreaseQuantity(null, new BigDecimal("5.0"), UnitEnum.L));
    }

    @Test
    void decreaseQuantity_shouldThrowException_whenQuantityIsNull() {
        assertThrows(NullPointerException.class, () -> stockRepository.decreaseQuantity(product, null, UnitEnum.L));
    }

    @Test
    void decreaseQuantity_shouldThrowException_whenUnitIsNull() {
        assertThrows(NullPointerException.class, () -> stockRepository.decreaseQuantity(product, new BigDecimal("5.0"), null));
    }

    @Test
    void isSufficientInStock_shouldThrowException_whenProductIsNull() {
        assertThrows(NullPointerException.class, () -> stockRepository.isSufficientInStock(null, new BigDecimal("5.0"), UnitEnum.L));
    }

    @Test
    void isSufficientInStock_shouldThrowException_whenQuantityIsNull() {
        assertThrows(NullPointerException.class, () -> stockRepository.isSufficientInStock(product, null, UnitEnum.L));
    }

    @Test
    void isSufficientInStock_shouldThrowException_whenUnitIsNull() {
        assertThrows(NullPointerException.class, () -> stockRepository.isSufficientInStock(product, new BigDecimal("5.0"), null));
    }

    @Test
    void isInStock_shouldThrowException_whenProductIsNull() {
        assertThrows(NullPointerException.class, () -> stockRepository.isInStock(null));
    }
}