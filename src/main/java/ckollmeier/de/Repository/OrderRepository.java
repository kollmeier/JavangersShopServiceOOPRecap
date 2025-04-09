package ckollmeier.de.Repository;

import ckollmeier.de.Entity.Order;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import ckollmeier.de.ValidationUtils;
import lombok.NonNull;

public final class OrderRepository {
    /**
     * Orders.
     */
    private final Map<String, Order> orders = new HashMap<>();

    private Order orderWithId(final @NonNull Order order) {
        if (order.id() != null) {
            return order;
        }
        String id = UUID.randomUUID().toString();
        return order.withId(id);
    }

    /**
     * @param order the order to add to list
     * @return the added order
     */
    public Optional<Order> addOrder(final @NonNull Order order) {
        Order orderWithId = ValidationUtils.validated(orderWithId(order));
        if (find(orderWithId.id()).isPresent()) {
            throw new IllegalArgumentException("Order with id " + orderWithId.id() + " already exists");
        }
        orders.put(orderWithId.id(), orderWithId);
        return Optional.of(orderWithId);
    }

    /**
     * @param order the order to remove from list
     * @return the removed order
     */
    public Optional<Order> removeOrder(final @NonNull Order order) {
        return removeOrderWithId(order.id());
    }

    /**
     * @param orderId id of the order to remove from list
     * @return the removed order
     */
    public Optional<Order> removeOrderWithId(final @NonNull String orderId) {
        return find(orderId).map(order -> {
            orders.remove(order.id());
            return order;
        });
    }

    /**
     * @param id id of order to find
     * @return found order or null
     */
    public Optional<Order> find(final @NonNull String id) {
        return Optional.ofNullable(orders.get(id));
    }

    public Collection<Order> findAll() {
        return orders.values();
    }

    public int countOrders() {
        return orders.size();
    }
}
