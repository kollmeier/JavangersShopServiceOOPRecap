package ckollmeier.de.Repository;

import ckollmeier.de.Entity.Order;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class OrderRepository {
    /**
     * Orders.
     */
    private final Map<String, Order> orders = new HashMap<>();

    private Order orderWithId(final Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
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
    public Order addOrder(final Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        Order orderWithId = orderWithId(order);
        if (find(orderWithId.id()) != null) {
            throw new IllegalArgumentException("Order with id " + orderWithId.id() + " already exists");
        }
        orders.put(orderWithId.id(), orderWithId);
        return orderWithId;
    }

    /**
     * @param order the order to remove from list
     * @return the removed order
     */
    public Order removeOrder(final Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        return removeOrderWithId(order.id());
    }

    /**
     * @param orderId id of the order to rremove from list
     * @return the removed order
     */
    public Order removeOrderWithId(final String orderId) {
        if (orderId == null) {
            throw new IllegalArgumentException("Order id cannot be null");
        }
        Order found = find(orderId);
        if (found == null) {
            throw new IllegalArgumentException("Order with id " + orderId + " not found");
        }
        orders.remove(found.id());
        return found;
    }

    /**
     * @param id id of order to find
     * @return found order or null
     */
    public Order find(final String id) {
        if (id == null) {
            throw new IllegalArgumentException("Order id cannot be null");
        }
        return orders.get(id);
    }

    public Collection<Order> findAll() {
        return orders.values();
    }

    public int countOrders() {
        return orders.size();
    }
}
