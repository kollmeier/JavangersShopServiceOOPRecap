package ckollmeier.de.Repository;

import ckollmeier.de.Entity.Interface.ProductInterface;
import ckollmeier.de.Entity.Product;
import ckollmeier.de.Entity.ProductBuilder;
import ckollmeier.de.Entity.StockArticle;
import ckollmeier.de.Entity.StockArticleBuilder;
import ckollmeier.de.Enum.UnitEnum;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class StockRepository {
    /**
     * Articles in stock.
     */
    private final Map<String, StockArticle> stockArticles = new HashMap<>();
    /**
     * Articles in stock indexed by product id.
     */
    private final Map<String, StockArticle> stockArticlesByProductId = new HashMap<>();

    private StockArticle stockArticleWithId(final StockArticle stockArticle) {
        if (stockArticle == null) {
            throw new IllegalArgumentException("StockArticle cannot be null");
        }
        if (stockArticle.id() != null) {
            return stockArticle;
        }
        String id = UUID.randomUUID().toString();
        return stockArticle.withId(id);
    }

    private StockArticle updateStockArticle(final StockArticle stockArticle) {
        stockArticles.replace(stockArticle.id(), stockArticle);
        stockArticlesByProductId.put(stockArticle.product().id(), stockArticle);
        return stockArticle;
    }

    /**
     * @param product product to check against
     * @return true if in stock
     */
    public boolean isInStock(final ProductInterface product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        return stockArticlesByProductId.containsKey(product.id());
    }

    /**
     * @param product product to check against
     * @param quantity needed quantity
     * @param unit unit of the quantity
     * @return true if enough in stock
     */
    public boolean isSufficientInStock(final ProductInterface product, final BigDecimal quantity, final UnitEnum unit) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        if (quantity == null) {
            throw new IllegalArgumentException("Quantity cannot be null");
        }
        if (unit == null) {
            throw new IllegalArgumentException("Unit cannot be null");
        }
        if (!stockArticlesByProductId.containsKey(product.productId())) {
            return false;
        }
        StockArticle stockArticle = stockArticlesByProductId.get(product.productId());
        BigDecimal convertedQuantity = quantity.multiply(stockArticle.unit().conversionFactor(unit));

        return stockArticle.quantity().compareTo(convertedQuantity) >= 0;
    }

    /**
     * @param product  product to increase the quantity
     * @param quantity quantity in amounts of unit
     * @param unit     unit of the added amount
     * @return product with increased quantity
     */
    public StockArticle increaseQuantity(final ProductInterface product, final BigDecimal quantity, final UnitEnum unit) {
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
            stockArticle = stockArticleWithId(
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
            );
            addProduct(stockArticle);
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
    public StockArticle decreaseQuantity(final ProductInterface product, final BigDecimal quantity, final UnitEnum unit) {
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

    public StockArticle find(final String id) {
        return stockArticles.get(id);
    }

    public StockArticle findByProductId(final String productId) {
        return stockArticlesByProductId.get(productId);
    }


    /**
     * Adds a new product to the stock repository.
     *
     * @param stockArticle the stock article to add
     * @return the added stock article
     * @throws IllegalArgumentException if stockArticle is null or already exists
     */
    public StockArticle addProduct(final StockArticle stockArticle) {
        if (stockArticle == null) {
            throw new IllegalArgumentException("StockArticle cannot be null");
        }
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
    public StockArticle addProduct(final ProductInterface product, final BigDecimal quantity, final UnitEnum unit, final BigDecimal price) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        if (quantity == null || quantity.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Quantity must be non-negative");
        }
        if (unit == null) {
            throw new IllegalArgumentException("Unit cannot be null");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
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