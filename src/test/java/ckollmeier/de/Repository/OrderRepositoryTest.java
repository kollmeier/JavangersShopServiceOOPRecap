package ckollmeier.de.Repository;

import ckollmeier.de.Entity.Order;
import ckollmeier.de.Entity.OrderBuilder;
import ckollmeier.de.Entity.OrderProduct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
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
        Optional<Order> added = orderRepository.addOrder(testOrder1.withId(null));

        assertThat( added ).isPresent();
        assertThat(added.get().id()).matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}");
        assertEquals(testProducts, added.get().products());
    }

    @Test
    void addOrder_shouldThrowExceptionWhenOrderIsNull() {
        Exception e = assertThrows(NullPointerException.class, () -> orderRepository.addOrder(null));
    }

    @Test
    void addOrder_shouldThrowExceptionWhenDuplicateId() {
        Optional<Order> addedOptional = orderRepository.addOrder(testOrder1);
        assertThat(addedOptional).isPresent();
        Order added = addedOptional.get();
        Exception e = assertThrows(IllegalArgumentException.class, () -> orderRepository.addOrder(added));
        assertEquals("Order with id " + added.id() + " already exists", e.getMessage());
    }

    @Test
    void addOrder_shouldAddOrderWithUnchangedIdWhenOrderHasId() {
        Optional<Order> addedOptional = orderRepository.addOrder(testOrder1.withId("predefined-id"));
        assertThat(addedOptional).isPresent();
        Order added = addedOptional.get();

        assertEquals("predefined-id", added.id());
        assertEquals(testProducts, added.products());
    }

    @Test
    void removeOrder_shouldRemoveSuccessfully() {
        Optional<Order> addedOptional = orderRepository.addOrder(testOrder1);
        assertThat(addedOptional).isPresent();
        Order added = addedOptional.get();

        Optional<Order> removedOptional = orderRepository.removeOrder(added);
        assertThat(removedOptional).isPresent();
        Order removed = removedOptional.get();

        assertEquals(added.id(), removed.id());
        assertThat(orderRepository.find(added.id())).isEmpty();
    }

    @Test
    void removeOrder_shouldThrowWhenNull() {
        Exception e = assertThrows(NullPointerException.class, () -> orderRepository.removeOrder(null));
    }

    @Test
    void removeOrder_shouldReturnEmptyOptionalWhenOrderNotFound() {
        Order order = OrderBuilder.builder().id("non-existing-id").products(testProducts).build();
        assertThat(orderRepository.removeOrder(order)).isEmpty();
    }

    @Test
    void removeOrderWithId_shouldRemoveSuccessfully() {
        Optional<Order> addedOptional = orderRepository.addOrder(testOrder1);
        assertThat(addedOptional).isPresent();
        Order added = addedOptional.get();

        Optional<Order> removedOptional = orderRepository.removeOrderWithId(added.id());
        assertThat(removedOptional).isPresent();
        Order removed = removedOptional.get();

        assertEquals(added.id(), removed.id());
        assertThat(orderRepository.find(added.id())).isEmpty();
    }

    @Test
    void removeOrderWithId_shouldThrowWhenNullId() {
        Exception e = assertThrows(NullPointerException.class, () -> orderRepository.removeOrderWithId(null));
    }

    @Test
    void removeOrderWithId_shouldReturnEmptyOptionalWhenIdNotFound() {
        String randomId = UUID.randomUUID().toString();
        assertThat(orderRepository.removeOrderWithId(randomId)).isEmpty();
    }

    @Test
    void find_shouldReturnOrderById() {
        Optional<Order> addedOptional = orderRepository.addOrder(testOrder1);
        assertThat(addedOptional).isPresent();
        Order added = addedOptional.get();
        Optional<Order> foundOptional = orderRepository.find(added.id());
        assertThat(foundOptional).isPresent();
        Order found = foundOptional.get();

        assertEquals(added.id(), found.id());
        assertEquals(added.products(), found.products());
    }

    @Test
    void find_shouldReturnEmptyOptionalWhenNotFound() {
        Optional<Order> result = orderRepository.find(UUID.randomUUID().toString());
        assertThat(result).isEmpty();
    }

    @Test
    void find_shouldThrowWhenIdIsNull() {
        Exception e = assertThrows(NullPointerException.class, () -> orderRepository.find(null));
    }

    @Test
    void findAll_shouldReturnAllOrders() {
        orderRepository.addOrder(testOrder1);
        orderRepository.addOrder(testOrder2);
        Collection<Order> orders = orderRepository.findAll();
        assertEquals(2, orders.size());
        assertThat(orders).containsExactlyInAnyOrder(testOrder1, testOrder2);
    }

    @Test
    void findAll_shouldReturnEmptyListWhenNoOrders() {
        Collection<Order> orders = orderRepository.findAll();
        assertThat(orders).isEmpty();
    }

    @Test
    void countOrders_shouldReturnCorrectCount() {
        assertEquals(0, orderRepository.countOrders());
        orderRepository.addOrder(testOrder1);
        orderRepository.addOrder(testOrder2);
        assertEquals(2, orderRepository.countOrders());
    }
    @Test
    void countOrders_shouldReturnCorrectCountAfterRemove() {
        assertEquals(0, orderRepository.countOrders());
        Optional<Order> addedOptional = orderRepository.addOrder(testOrder1);
        assertThat(addedOptional).isPresent();
        Order added = addedOptional.get();

        orderRepository.addOrder(testOrder2);
        assertEquals(2, orderRepository.countOrders());
        orderRepository.removeOrder(added);
        assertEquals(1, orderRepository.countOrders());
    }
}