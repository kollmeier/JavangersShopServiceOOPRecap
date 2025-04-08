package ckollmeier.de.Repository;

import ckollmeier.de.Entity.Product;

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
        Product productWithId = productWithId(product);
        if (find(productWithId.id()) != null) {
            throw new IllegalArgumentException("Product with id " + productWithId.id() + " already exists");
        }
        products.add(productWithId);
        return productWithId;
    }

    /**
     * @param product the product to remove from list
     * @return the removed product
     */
    public Product removeProduct(final Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        if (product.id() == null) {
            throw new IllegalArgumentException("Product id cannot be null");
        }
        return removeProductWithId(product.id());
    }

    /**
     * @param productId id of the product to rremove from list
     * @return the removed product
     */
    public Product removeProductWithId(final String productId) {
        if (productId == null) {
            throw new IllegalArgumentException("Product id cannot be null");
        }
        Product found = find(productId);
        if (found == null) {
            throw new IllegalArgumentException("Product with id " + productId + " not found");
        }
        products.remove(found);
        return found;
    }

    /**
     * @param id id of product to find
     * @return found product or null
     */
    public Product find(final String id) {
        if (id == null) {
            throw new IllegalArgumentException("Product id cannot be null");
        }
        for (Product product : products) {
            if (product.id().equals(id)) {
                return product;
            }
        }
        return null;
    }

    public List<Product> findAll() {
        return products;
    }

    public int countProducts() {
        return products.size();
    }
}