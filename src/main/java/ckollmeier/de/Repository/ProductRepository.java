package ckollmeier.de.Repository;

import ckollmeier.de.Entity.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class ProductRepository {
    /**
     * list of products.
     */
    private final List<Product> products = new ArrayList<>();

    private Product productWithId(final Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        if (product.id() != null) {
            return product;
        }
        String id = UUID.randomUUID().toString();
        return product.withId(id);
    }

    /**
     * @param product the product to add to list
     * @return the added product
     */
    public Product addProduct(final Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        return null;
    }

    /**
     * @param product the product to remove from list
     * @return the removed product
     */
    public Product removeProduct(final Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        return null;
    }

    /**
     * @param productId id of the product to rremove from list
     * @return the removed product
     */
    public Product removeProductWithId(final String productId) {
        if (productId == null) {
            throw new IllegalArgumentException("Product id cannot be null");
        }
        return null;
    }

    public Product find(final String id) {
        return null;
    }

    public List<Product> findAll() {
        return products;
    }

    /**
     * @param product product to increase the quantity
     * @param quantity quantity in amounts of unit
     */
    public void increaseQuantity(final Product product, final BigDecimal quantity) {

    }

    /**
     * @param product product to decrease the quantity
     * @param quantity quantity in amounts of unit
     */
    public void decreaseQuantity(final Product product, final BigDecimal quantity) {

    }

    /**
     * @param productId productId to increase the quantity
     * @param quantity quantity in amounts of unit
     */
    public void increaseQuantityById(final String productId, final BigDecimal quantity) {
        if (productId == null) {
            throw new IllegalArgumentException("Product id cannot be null");
        }
        Product product = find(productId);
        if (product == null) {
            throw new IllegalArgumentException("Product not found");
        }
        increaseQuantity(product, quantity);
    }

    /**
     * @param productId productId to decrease the quantity
     * @param quantity quantity in amounts of unit
     */
    public void decreaseQuantityById(final String productId, final BigDecimal quantity) {
        if (productId == null) {
            throw new IllegalArgumentException("Product id cannot be null");
        }
        Product product = find(productId);
        if (product == null) {
            throw new IllegalArgumentException("Product not found");
        }
        decreaseQuantity(product, quantity);
    }
}
