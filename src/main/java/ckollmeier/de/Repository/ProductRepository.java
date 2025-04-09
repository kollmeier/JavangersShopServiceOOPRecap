package ckollmeier.de.Repository;

import ckollmeier.de.Entity.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import lombok.NonNull;

public final class ProductRepository {
    /**
     * list of products.
     */
    private final List<Product> products = new ArrayList<>();

    private Product productWithId(final @NonNull Product product) {
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
    public Product addProduct(final @NonNull Product product) {
        Product productWithId = productWithId(product);
        if (find(productWithId.id()).isPresent()) {
            throw new IllegalArgumentException("Product with id " + productWithId.id() + " already exists");
        }
        products.add(productWithId);
        return productWithId;
    }

    /**
     * @param product the product to remove from list
     * @return an optional of the removed product
     */
    public Optional<Product> removeProduct(final @NonNull Product product) {
        return removeProductWithId(product.id());
    }

    /**
     * @param productId id of the product to remove from list
     * @return an optional of the removed product
     */
    public Optional<Product> removeProductWithId(final @NonNull String productId) {
        return find(productId).map(product -> {
            products.remove(product);
            return product;
        });
    }

    /**
     * @param id id of product to find
     * @return an optional of the found product
     */
    public Optional<Product> find(final @NonNull String id) {
        return products.stream().filter(product -> product.id().equals(id)).findFirst();
    }

    public List<Product> findAll() {
        return products;
    }

    public int countProducts() {
        return products.size();
    }
}