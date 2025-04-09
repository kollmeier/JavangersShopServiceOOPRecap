package ckollmeier.de.Repository;

import ckollmeier.de.Entity.Interface.ProductInterface;
import ckollmeier.de.Entity.Product;
import ckollmeier.de.Entity.ProductBuilder;
import ckollmeier.de.Entity.StockArticle;
import ckollmeier.de.Entity.StockArticleBuilder;
import ckollmeier.de.Enum.UnitEnum;

import ckollmeier.de.ValidationUtils;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.NonNull;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public final class StockRepository {
    private final Map<String, StockArticle> stockArticles = new HashMap<>();
    private final Map<String, StockArticle> stockArticlesByProductId = new HashMap<>();

    private StockArticle stockArticleWithId(final @NonNull StockArticle stockArticle) {
        String id = UUID.randomUUID().toString();
        return ValidationUtils.validated(stockArticle.withId(id));
    }

    private StockArticle updateStockArticle(final @NonNull StockArticle stockArticle) {
        ValidationUtils.validate(stockArticle);
        stockArticles.replace(stockArticle.id(), stockArticle);
        stockArticlesByProductId.put(stockArticle.product().id(), stockArticle);
        return stockArticle;
    }

    /**
     * @param product product to check against
     * @return true if in stock
     */
    public boolean isInStock(final @NonNull ProductInterface product) {
        return findByProductId(product.id()).isPresent();
    }

    /**
     * @param product product to check against
     * @param quantity needed quantity
     * @param unit unit of the quantity
     * @return true if enough in stock
     */
    public boolean isSufficientInStock(final @NonNull ProductInterface product, final @NonNull @PositiveOrZero BigDecimal quantity, final @NonNull UnitEnum unit) {
        ValidationUtils.validate(quantity);
        Optional<StockArticle> optionalStockArticle = findByProductId(product.productId());
        if (optionalStockArticle.isEmpty()) {
            return false;
        }
        StockArticle stockArticle = optionalStockArticle.get();
        BigDecimal convertedQuantity = quantity.multiply(stockArticle.unit().conversionFactor(unit));

        return stockArticle.quantity().compareTo(convertedQuantity) >= 0;
    }

    /**
     * @param product  product to increase the quantity
     * @param quantity quantity in amounts of unit
     * @param unit     unit of the added amount
     * @return product with increased quantity
     */
    public StockArticle increaseQuantity(final @NonNull ProductInterface product, final @NonNull BigDecimal quantity, final @NonNull UnitEnum unit) {
        ValidationUtils.validate(quantity);
        StockArticle stockArticle = findByProductId(product.id()).orElseGet(
                () -> addProduct(
                        stockArticleWithId(
                                StockArticleBuilder.builder()
                                        .product(ProductBuilder.builder()
                                                .id(product.productId())
                                                .name(product.name())
                                                .description(product.description())
                                                .content(product.content())
                                                .unit(product.unit())
                                                .build())
                                        .quantity(BigDecimal.ZERO)
                                        .unit(unit)
                                        .price(BigDecimal.ZERO) // Default price, can be updated later
                                        .build()
                        )
                )
        );
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
    public StockArticle decreaseQuantity(final @NonNull ProductInterface product, final @NonNull BigDecimal quantity, final @NonNull UnitEnum unit) {
        if (quantity.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        StockArticle stockArticle = findByProductId(product.id())
                .orElseThrow(() -> new IllegalArgumentException("Product " + product.id() + " does not exist"));
        BigDecimal decreasedQuantity = quantity.multiply(stockArticle.unit().conversionFactor(unit));
        if (stockArticle.quantity().compareTo(decreasedQuantity) <= 0) {
            throw new IllegalArgumentException("Not enough stock quantity");
        }
        return updateStockArticle(
                stockArticle.withQuantity(
                        stockArticle.quantity()
                                .subtract(decreasedQuantity)
                )
        );
    }

    public Optional<StockArticle> find(final @NonNull String id) {
        return Optional.ofNullable(stockArticles.get(id));
    }

    public Optional<StockArticle> findByProductId(final @NonNull String productId) {
        return Optional.ofNullable(stockArticlesByProductId.get(productId));
    }


    /**
     * Adds a new product to the stock repository.
     *
     * @param stockArticle the stock article to add
     * @return the added stock article
     * @throws IllegalArgumentException if stockArticle is null or already exists
     */
    public StockArticle addProduct(final @NonNull StockArticle stockArticle) {
        if (stockArticles.containsKey(stockArticle.id())) {
            throw new IllegalArgumentException("StockArticle with id " + stockArticle.id() + " already exists");
        }
        if (stockArticlesByProductId.containsKey(stockArticle.product().id())) {
            throw new IllegalArgumentException("StockArticle for product with id " + stockArticle.product().id() + " already exists");
        }

        stockArticles.put(stockArticle.id(), stockArticle);
        stockArticlesByProductId.put(stockArticle.product().id(), stockArticle);

        return stockArticle;
    }

    /**
     * Adds a new product to the stock repository using a ProductInterface instance.
     *
     * @param product  the product to add
     * @param quantity the initial stock quantity
     * @param unit     the unit of the quantity
     * @param price    the price of the product
     * @return the added stock article
     * @throws IllegalArgumentException if the product is null, quantity is less than zero, or product already exists
     */
    public StockArticle addProduct(final @NonNull ProductInterface product, final @NonNull @PositiveOrZero BigDecimal quantity, final @NonNull UnitEnum unit, final @NonNull BigDecimal price) {
        if (quantity.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Quantity must be non-negative");
        }
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price must be non-negative");
        }

        if (stockArticlesByProductId.containsKey(product.id())) {
            throw new IllegalArgumentException("StockArticle for product with id " + product.id() + " already exists");
        }
        Product productFromInterface = ProductBuilder.builder()
                .id(product.productId())
                .name(product.name())
                .description(product.description())
                .content(product.content())
                .unit(product.unit())
                .build();
        StockArticle stockArticle = stockArticleWithId(StockArticleBuilder.builder()
                .product(productFromInterface)
                .quantity(quantity)
                .unit(unit)
                .price(price)
                .build());

        return addProduct(stockArticle);
    }
}