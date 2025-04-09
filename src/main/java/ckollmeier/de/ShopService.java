package ckollmeier.de;

import ckollmeier.de.Entity.Interface.ProductInterface;
import ckollmeier.de.Entity.Order;
import ckollmeier.de.Entity.OrderProduct;
import ckollmeier.de.Entity.Product;
import ckollmeier.de.Entity.StockArticle;
import ckollmeier.de.Enum.UnitEnum;
import ckollmeier.de.Repository.OrderRepository;
import ckollmeier.de.Repository.ProductRepository;
import ckollmeier.de.Repository.StockRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.index.qual.NonNegative;

import javax.annotation.Nonnegative;

@RequiredArgsConstructor
public class ShopService {
    private final OrderRepository orderRepository;
    private final StockRepository stockRepository;
    private final ProductRepository productRepository;

    /**
     * @param order orders to place
     * @return the placed order
     */
    public Optional<Order> addOrder(final @NonNull Order order) {
        List<OrderProduct> productList = new ArrayList<>();
        for (OrderProduct product : order.products()) {
            if (stockRepository.isSufficientInStock(product, product.getQuantity(), product.unit())) {
                productList.add(product);
                stockRepository.decreaseQuantity(product, product.getQuantity(), product.unit());
            } else {
                System.out.println("Not enough stock for product: " + product.name());
            }
        }
        return orderRepository.addOrder(order.withProducts(productList));
    }

    /**
     * Removes an order and restores stock quantities.
     *
     * @param order the order to remove
     * @return the removed order
     */
    public Optional<Order> removeOrder(final @NonNull Order order) {
        if (order.id() == null) {
            throw new IllegalArgumentException("Order id cannot be null");
        }
        orderRepository.find(order.id()).orElseThrow(() -> new IllegalArgumentException("Order with id " + order.id() + " not found"));
        for (OrderProduct product : order.products()) {
            stockRepository.increaseQuantity(product, product.getQuantity(), product.unit());
        }

        return orderRepository.removeOrder(order);
    }

    /**
     * Adds a new product to the repository.
     *
     * @param product the product to be added
     * @return the added product
     */
    public Product addProduct(final @NonNull Product product) {
        return productRepository.addProduct(product);
    }

    /**
     * Removes a product from the repository.
     *
     * @param product the product to be removed
     * @return the removed product
     */
    public Optional<Product> removeProduct(final @NonNull Product product) {
        return productRepository.removeProduct(product);
    }

    /**
     * Retrieves all products from the repository.
     *
     * @return a list of all products
     */
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Adds stock to the repository for a given product.
     *
     * @param product  the product to add stock for
     * @param quantity the quantity of stock to add
     * @param unit     the unit of measurement for the stock
     * @param price    the price of the stock
     * @return the added stock article
     */
    public StockArticle addStock(final @NonNull ProductInterface product, final @NonNull @NonNegative BigDecimal quantity, final @NonNull UnitEnum unit, final @NonNull @NonNegative BigDecimal price) {
        return stockRepository.addProduct(product, quantity, unit, price);
    }

    /**
     * Increases the stock quantity for a given product.
     *
     * @param product  the product to increase stock for
     * @param quantity the quantity to add to the stock
     * @param unit     the unit of measurement for the stock
     * @return the updated stock article
     */
    public StockArticle increaseStock(final @NonNull ProductInterface product, final @NonNull @NonNegative BigDecimal quantity, final @NonNull UnitEnum unit) {
        return stockRepository.increaseQuantity(product, quantity, unit);
    }

    /**
     * Decreases the stock quantity for a given product.
     *
     * @param product  the product to decrease stock for
     * @param quantity the quantity to subtract from the stock
     * @param unit     the unit of measurement for the stock
     * @return the updated stock article
     */
    public StockArticle decreaseStock(final @NonNull ProductInterface product, final @NonNull @Nonnegative BigDecimal quantity, final @NonNull UnitEnum unit) {
        return stockRepository.decreaseQuantity(product, quantity, unit);
    }

    /**
     * Retrieves all stock articles from the repository.
     *
     * @return a list of all stock articles
     */
    public List<StockArticle> getAllStock() {
        List<StockArticle> allStock = new ArrayList<>();
        for (Product product : productRepository.findAll()) {
            Optional<StockArticle> stockArticle = stockRepository.findByProductId(product.id());
            stockArticle.ifPresent(allStock::add);
        }
        return allStock;
    }
}