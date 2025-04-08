package ckollmeier.de.Repository;

import ckollmeier.de.Entity.Product;
import ckollmeier.de.Entity.StockArticle;
import ckollmeier.de.Enum.UnitEnum;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public final class StockRepository {
    /**
     * Articles in stock.
     */
    private final Map<String, StockArticle> stockArticles = new HashMap<>();
    /**
     * Articles in stock indexed by product id.
     */
    private final Map<String, StockArticle> stockArticlesByProductId = new HashMap<>();

    private StockArticle updateStockArticle(final StockArticle stockArticle) {
        stockArticles.replace(stockArticle.id(), stockArticle);
        stockArticlesByProductId.put(stockArticle.product().id(), stockArticle);
        return stockArticle;
    }

    /**
     * @param product  product to increase the quantity
     * @param quantity quantity in amounts of unit
     * @param unit     unit of the added amount
     * @return product with increased quantity
     */
    public StockArticle increaseQuantity(final Product product, final BigDecimal quantity, final UnitEnum unit) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        if (quantity == null) {
            throw new IllegalArgumentException("Quantity cannot be null");
        }
        if (unit == null) {
            throw new IllegalArgumentException("Unit cannot be null");
        }
        if (quantity.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        StockArticle stockArticle = stockArticlesByProductId.get(product.id());
        if (stockArticle == null) {
            throw new IllegalArgumentException("Product " + product.id() + " does not exist");
        }
        return updateStockArticle(
                stockArticle.withQuantity(
                        stockArticle.quantity()
                                .add(
                                        quantity.multiply(stockArticle.unit().conversionFactor(unit))
                                )
                )
        );
    }

    /**
     * @param product  product to decrease the quantity
     * @param quantity quantity in amounts of unit
     * @param unit     unit of the added amount
     * @return product with decreased quantity
     */
    public StockArticle decreaseQuantity(final Product product, final BigDecimal quantity, final UnitEnum unit) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        if (quantity == null) {
            throw new IllegalArgumentException("Quantity cannot be null");
        }
        if (unit == null) {
            throw new IllegalArgumentException("Unit cannot be null");
        }
        StockArticle stockArticle = stockArticlesByProductId.get(product.id());
        if (stockArticle == null) {
            throw new IllegalArgumentException("Product " + product.id() + " does not exist");
        }
        BigDecimal decreasedQuantity = quantity.multiply(stockArticle.unit().conversionFactor(unit));
        if (decreasedQuantity.compareTo(stockArticle.quantity()) < 0) {
            throw new IllegalArgumentException("Not enough stock quantity");
        }
        return updateStockArticle(
                stockArticle.withQuantity(
                        stockArticle.quantity()
                                .subtract(decreasedQuantity)
                )
        );
    }

    public StockArticle find(final String id) {
        return stockArticles.get(id);
    }

    public StockArticle findByProductId(final String productId) {
        return stockArticlesByProductId.get(productId);
    }
}
