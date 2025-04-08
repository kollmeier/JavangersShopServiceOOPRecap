package ckollmeier.de;

import ckollmeier.de.Entity.Order;
import ckollmeier.de.Entity.OrderProduct;
import ckollmeier.de.Repository.OrderRepository;
import ckollmeier.de.Repository.ProductRepository;
import ckollmeier.de.Repository.StockRepository;

import java.util.ArrayList;
import java.util.List;

public class ShopService {
    private final OrderRepository orderRepository;
    private final StockRepository stockRepository;
    private final ProductRepository productRepository;

    public ShopService(final OrderRepository orderRepository, final StockRepository stockRepository, final ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.stockRepository = stockRepository;
        this.productRepository = productRepository;
    }

    /**
     * @param order orders to place
     * @return the placed order
     */
    public Order addOrder(final Order order) {
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


}