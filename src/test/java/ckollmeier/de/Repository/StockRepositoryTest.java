package ckollmeier.de.Repository;

import ckollmeier.de.Entity.Interface.ProductInterface;
import ckollmeier.de.Entity.StockArticle;
import ckollmeier.de.Enum.UnitEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

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

        stockRepository.addProduct(product, new BigDecimal("10.0"), UnitEnum.KG, new BigDecimal("19.99"));
    }

    @Test
    void isInStock_shouldReturnTrue_whenProductExists() {
        assertTrue(stockRepository.isInStock(product));
    }

    @Test
    void isSufficientInStock_shouldReturnTrue_whenEnoughQuantityAvailable() {
        assertTrue(stockRepository.isSufficientInStock(product, new BigDecimal("5.0"), UnitEnum.KG));
    }

    @Test
    void isSufficientInStock_shouldReturnFalse_whenProductNotInStock() {
        ProductInterface otherProduct = mock(ProductInterface.class);
        when(otherProduct.id()).thenReturn("other-prod");
        assertFalse(stockRepository.isSufficientInStock(otherProduct, new BigDecimal("1.0"), UnitEnum.KG));
    }

    @Test
    void increaseQuantity_shouldAddToExistingQuantity() {
        StockArticle updated = stockRepository.increaseQuantity(product, new BigDecimal("5.0"), UnitEnum.KG);
        assertThat(new BigDecimal("15.0").setScale(updated.quantity().scale(), RoundingMode.HALF_UP)).isEqualTo(updated.quantity());
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
    void find_shouldReturnNull_whenIdNotStored() {
        StockArticle found = stockRepository.find("non-existent-id");
        assertNull(found);
    }

    @Test
    void findByProductId_shouldReturnStockArticle_whenProductExists() {
        StockArticle found = stockRepository.findByProductId("prod-1");
        assertNotNull(found);
    }
}
