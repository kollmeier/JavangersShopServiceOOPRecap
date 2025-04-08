package ckollmeier.de.Repository;

import ckollmeier.de.Entity.Order;
import ckollmeier.de.Entity.OrderBuilder;
import ckollmeier.de.Entity.OrderProduct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderRepositoryTest {

    private OrderRepository orderRepository;
    private Order testOrder1;
    private Order testOrder2;
    private List<OrderProduct> testProducts;

    @BeforeEach
    void init() {
        orderRepository = new OrderRepository();
        OrderProduct product1 = mock(OrderProduct.class);
        when(product1.name()).thenReturn("Test Product");
        when(product1.id()).thenReturn("prod-1");
        when(product1.productId()).thenReturn("prod-1");

        OrderProduct product2 = mock(OrderProduct.class);
        when(product2.name()).thenReturn("Test Product 2");
        when(product2.id()).thenReturn("prod-2");
        when(product2.productId()).thenReturn("prod-2");

        testProducts = List.of(product1, product2);

        testOrder1 = OrderBuilder.builder().id("order-1").products(testProducts).build();
        testOrder2 = OrderBuilder.builder().id("order-2").products(testProducts).build();
    }

    @Test
    void addOrder_shouldGenerateIdAndReturnOrder() {
        Order added = orderRepository.addOrder(testOrder1);

        assertNotNull(added);
        assertNotNull(added.id());
        assertEquals(testProducts, added.products());
    }

    @Test
    void addOrder_shouldThrowExceptionWhenOrderIsNull() {
        Exception e = assertThrows(IllegalArgumentException.class, () -> orderRepository.addOrder(null));
        assertEquals("Order cannot be null", e.getMessage());
    }

    @Test
    void addOrder_shouldThrowExceptionWhenDuplicateId() {
        Order added = orderRepository.addOrder(testOrder1);
        Exception e = assertThrows(IllegalArgumentException.class, () -> orderRepository.addOrder(added));
        assertEquals("Order with id " + added.id() + " already exists", e.getMessage());
    }

    @Test
    void removeOrder_shouldRemoveSuccessfully() {
        Order added = orderRepository.addOrder(testOrder1);
        Order removed = orderRepository.removeOrder(added);

        assertEquals(added.id(), removed.id());
        assertNull(orderRepository.find(added.id()));
    }

    @Test
    void removeOrder_shouldThrowWhenNull() {
        Exception e = assertThrows(IllegalArgumentException.class, () -> orderRepository.removeOrder(null));
        assertEquals("Order cannot be null", e.getMessage());
    }

    @Test
    void removeOrderWithId_shouldThrowWhenNullId() {
        Exception e = assertThrows(IllegalArgumentException.class, () -> orderRepository.removeOrderWithId(null));
        assertEquals("Order id cannot be null", e.getMessage());
    }

    @Test
    void removeOrderWithId_shouldThrowWhenIdNotFound() {
        String randomId = UUID.randomUUID().toString();
        Exception e = assertThrows(IllegalArgumentException.class, () -> orderRepository.removeOrderWithId(randomId));
        assertEquals("Order with id " + randomId + " not found", e.getMessage());
    }

    @Test
    void find_shouldReturnOrderById() {
        Order added = orderRepository.addOrder(testOrder1);
        Order found = orderRepository.find(added.id());

        assertEquals(added.id(), found.id());
    }

    @Test
    void find_shouldReturnNullWhenNotFound() {
        Order result = orderRepository.find(UUID.randomUUID().toString());
        assertNull(result);
    }

    @Test
    void find_shouldThrowWhenIdIsNull() {
        Exception e = assertThrows(IllegalArgumentException.class, () -> orderRepository.find(null));
        assertEquals("Order id cannot be null", e.getMessage());
    }

    @Test
    void findAll_shouldReturnAllOrders() {
        orderRepository.addOrder(testOrder1);
        orderRepository.addOrder(testOrder2);
        Collection<Order> orders = orderRepository.findAll();
        assertEquals(2, orders.size());
    }

    @Test
    void countOrders_shouldReturnCorrectCount() {
        assertEquals(0, orderRepository.countOrders());
        orderRepository.addOrder(testOrder1);
        orderRepository.addOrder(testOrder2);
        assertEquals(2, orderRepository.countOrders());
    }
}