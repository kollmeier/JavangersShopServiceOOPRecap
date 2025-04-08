package ckollmeier.de;

import ckollmeier.de.Entity.*;
import ckollmeier.de.Enum.UnitEnum;
import ckollmeier.de.Repository.OrderRepository;
import ckollmeier.de.Repository.ProductRepository;
import ckollmeier.de.Repository.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ShopServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private StockRepository stockRepository;

    @Mock
    private ProductRepository productRepository;

    private ShopService shopService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        shopService = new ShopService(orderRepository, stockRepository, productRepository);
    }

    @Test
    void addOrder_SufficientStock_OrderAdded() {
        // Arrange
        Product product = ProductBuilder.builder().id(UUID.randomUUID().toString()).name("Product 1").build();
        BigDecimal quantity = BigDecimal.TEN;
        UnitEnum unit = UnitEnum.PCS;
        BigDecimal price = BigDecimal.valueOf(5.0);
        StockArticle stockArticle = StockArticleBuilder.builder()
                .id(UUID.randomUUID().toString())
                .product(product)
                .quantity(quantity)
                .unit(unit)
                .price(price)
                .build();

        OrderProduct orderProduct = new OrderProduct(stockArticle);
        orderProduct.setQuantity(BigDecimal.ONE);
        List<OrderProduct> orderProducts = List.of(orderProduct);
        Order order = new Order("order-1", orderProducts);


        when(stockRepository.isSufficientInStock(orderProduct, BigDecimal.ONE, UnitEnum.PCS)).thenReturn(true);
        when(orderRepository.addOrder(any(Order.class))).thenReturn(order);

        // Act
        Order result = shopService.addOrder(order);

        // Assert
        assertNotNull(result);
        assertEquals(order.id(), result.id());
        verify(stockRepository, times(1)).decreaseQuantity(orderProduct, BigDecimal.ONE, UnitEnum.PCS);
        verify(orderRepository, times(1)).addOrder(any(Order.class));
    }

    @Test
    void addOrder_InsufficientStock_OrderNotAdded() {
        // Arrange
        // Arrange
        Product product = ProductBuilder.builder().id(UUID.randomUUID().toString()).name("Product 1").build();
        BigDecimal quantity = BigDecimal.ONE;
        UnitEnum unit = UnitEnum.PCS;
        BigDecimal price = BigDecimal.valueOf(5.0);
        StockArticle stockArticle = StockArticleBuilder.builder()
                .id(UUID.randomUUID().toString())
                .product(product)
                .quantity(quantity)
                .unit(unit)
                .price(price)
                .build();


        OrderProduct orderProduct = new OrderProduct(stockArticle);
        orderProduct.setQuantity(BigDecimal.TEN);

        List<OrderProduct> orderProducts = List.of(orderProduct);
        Order order = new Order("order-1", orderProducts);

        when(stockRepository.isSufficientInStock(orderProduct, BigDecimal.TEN, unit)).thenReturn(false);
        when(orderRepository.addOrder(any(Order.class))).thenReturn(order.withProducts(new ArrayList<>()));

        // Act
        Order result = shopService.addOrder(order);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.products().size());
        verify(stockRepository, never()).decreaseQuantity(any(), any(), any());
        verify(orderRepository, times(1)).addOrder(any(Order.class));
    }

    @Test
    void removeOrder_ValidOrder_OrderRemovedAndStockRestored() {
        // Arrange
        StockArticle stockArticle = mock(StockArticle.class);
        when(stockArticle.productId()).thenReturn("prod-1");
        when(stockArticle.price()).thenReturn(BigDecimal.TEN);
        when(stockArticle.unit()).thenReturn(UnitEnum.PCS);

        OrderProduct orderProduct = new OrderProduct(stockArticle);
        orderProduct.setQuantity(BigDecimal.ONE);

        List<OrderProduct> orderProducts = List.of(orderProduct);
        Order order = new Order("order-1", orderProducts);

        when(orderRepository.find(order.id())).thenReturn(order);
        when(orderRepository.removeOrder(order)).thenReturn(order);

        // Act
        Order result = shopService.removeOrder(order);

        // Assert
        assertNotNull(result);
        assertEquals(order.id(), result.id());
        verify(stockRepository, times(1)).increaseQuantity(orderProduct, BigDecimal.ONE, UnitEnum.PCS);
        verify(orderRepository, times(1)).removeOrder(order);
    }

    @Test
    void removeOrder_NullOrder_ThrowsIllegalArgumentException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> shopService.removeOrder(null));
    }

    @Test
    void removeOrder_NullOrderId_ThrowsIllegalArgumentException() {
        // Arrange
        Order order = new Order(null, new ArrayList<>());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> shopService.removeOrder(order));
    }

    @Test
    void removeOrder_OrderNotFound_ThrowsIllegalArgumentException() {
        // Arrange
        Order order = new Order("order-1", new ArrayList<>());
        when(orderRepository.find(order.id())).thenReturn(null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> shopService.removeOrder(order));
    }

    @Test
    void addProduct_ValidProduct_ProductAdded() {
        // Arrange
        Product product = ProductBuilder.builder().id(UUID.randomUUID().toString()).name("Test Product").build();
        when(productRepository.addProduct(product)).thenReturn(product);

        // Act
        Product result = shopService.addProduct(product);

        // Assert
        assertNotNull(result);
        assertEquals(product.id(), result.id());
        verify(productRepository, times(1)).addProduct(product);
    }

    @Test
    void removeProduct_ValidProduct_ProductRemoved() {
        // Arrange
        Product product = ProductBuilder.builder().id(UUID.randomUUID().toString()).name("Test Product").build();
        when(productRepository.removeProduct(product)).thenReturn(product);

        // Act
        Product result = shopService.removeProduct(product);

        // Assert
        assertNotNull(result);
        assertEquals(product.id(), result.id());
        verify(productRepository, times(1)).removeProduct(product);
    }

    @Test
    void getAllProducts_ProductsExist_ReturnsAllProducts() {
        // Arrange
        List<Product> products = List.of(
                ProductBuilder.builder().id(UUID.randomUUID().toString()).name("Product 1").build(),
                ProductBuilder.builder().id(UUID.randomUUID().toString()).name("Product 2").build()
        );
        when(productRepository.findAll()).thenReturn(products);

        // Act
        List<Product> result = shopService.getAllProducts();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void addStock_ValidInput_StockAdded() {
        // Arrange
        Product product = ProductBuilder.builder().id(UUID.randomUUID().toString()).name("Product 1").build();
        BigDecimal quantity = BigDecimal.TEN;
        UnitEnum unit = UnitEnum.PCS;
        BigDecimal price = BigDecimal.valueOf(5.0);
        StockArticle stockArticle = StockArticleBuilder.builder()
                .id(UUID.randomUUID().toString())
                .product(product)
                .quantity(BigDecimal.ONE)
                .unit(UnitEnum.PCS)
                .price(BigDecimal.TEN)
                .build();
        when(stockRepository.addProduct(product, quantity, unit, price)).thenReturn(stockArticle);

        // Act
        StockArticle result = shopService.addStock(product, quantity, unit, price);

        // Assert
        assertNotNull(result);
        assertEquals(product.id(), result.productId());
        verify(stockRepository, times(1)).addProduct(product, quantity, unit, price);
    }

    @Test
    void increaseStock_ValidInput_StockIncreased() {
        // Arrange
        Product product = ProductBuilder.builder().id(UUID.randomUUID().toString()).name("Product 1").build();
        BigDecimal quantity = BigDecimal.TEN;
        UnitEnum unit = UnitEnum.PCS;
        BigDecimal price = BigDecimal.valueOf(5.0);
        StockArticle stockArticle = StockArticleBuilder.builder()
                .id(UUID.randomUUID().toString())
                .product(product)
                .quantity(BigDecimal.ONE)
                .unit(UnitEnum.PCS)
                .price(BigDecimal.TEN)
                .build();
        when(stockRepository.increaseQuantity(product, quantity, unit)).thenReturn(stockArticle);

        // Act
        StockArticle result = shopService.increaseStock(product, quantity, unit);

        // Assert
        assertNotNull(result);
        assertEquals(product.id(), result.productId());
        verify(stockRepository, times(1)).increaseQuantity(product, quantity, unit);
    }

    @Test
    void decreaseStock_ValidInput_StockDecreased() {
        // Arrange
        Product product = ProductBuilder.builder().id(UUID.randomUUID().toString()).name("Product 1").build();
        BigDecimal quantity = BigDecimal.TEN;
        UnitEnum unit = UnitEnum.PCS;
        BigDecimal price = BigDecimal.valueOf(5.0);
        StockArticle stockArticle = StockArticleBuilder.builder()
                .id(UUID.randomUUID().toString())
                .product(product)
                .quantity(BigDecimal.ONE)
                .unit(UnitEnum.PCS)
                .price(BigDecimal.TEN)
                .build();
        when(stockRepository.decreaseQuantity(product, quantity, unit)).thenReturn(stockArticle);

        // Act
        StockArticle result = shopService.decreaseStock(product, quantity, unit);

        // Assert
        assertNotNull(result);
        assertEquals(product.id(), result.productId());
        verify(stockRepository, times(1)).decreaseQuantity(product, quantity, unit);
    }

    @Test
    void getAllStock_StockExists_ReturnsAllStock() {
        // Arrange
        Product product1 = ProductBuilder.builder().id(UUID.randomUUID().toString()).name("Product 1").build();
        Product product2 = ProductBuilder.builder().id(UUID.randomUUID().toString()).name("Product 2").build();
        List<Product> products = List.of(product1, product2);
        StockArticle stockArticle1 = StockArticleBuilder.builder()
                .id(UUID.randomUUID().toString())
                .product(product1)
                .quantity(BigDecimal.TEN)
                .unit(UnitEnum.PCS)
                .price(BigDecimal.ONE)
                .build();
        StockArticle stockArticle2 = StockArticleBuilder.builder()
                .id(UUID.randomUUID().toString())
                .product(product2)
                .quantity(BigDecimal.ONE)
                .unit(UnitEnum.KG)
                .price(BigDecimal.TEN)
                .build();

        when(productRepository.findAll()).thenReturn(products);
        when(stockRepository.findByProductId(product1.id())).thenReturn(stockArticle1);
        when(stockRepository.findByProductId(product2.id())).thenReturn(stockArticle2);

        // Act
        List<StockArticle> result = shopService.getAllStock();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(stockArticle1));
        assertTrue(result.contains(stockArticle2));
        verify(productRepository, times(1)).findAll();
        verify(stockRepository, times(1)).findByProductId(product1.id());
        verify(stockRepository, times(1)).findByProductId(product2.id());
    }
    @Test
    void getAllStock_NoStock_ReturnsEmptyList() {
        // Arrange
        Product product1 = ProductBuilder.builder().id(UUID.randomUUID().toString()).name("Product 1").build();
        Product product2 = ProductBuilder.builder().id(UUID.randomUUID().toString()).name("Product 2").build();
        List<Product> products = List.of(product1, product2);

        when(productRepository.findAll()).thenReturn(products);
        when(stockRepository.findByProductId(product1.id())).thenReturn(null);
        when(stockRepository.findByProductId(product2.id())).thenReturn(null);

        // Act
        List<StockArticle> result = shopService.getAllStock();

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(productRepository, times(1)).findAll();
        verify(stockRepository, times(1)).findByProductId(product1.id());
        verify(stockRepository, times(1)).findByProductId(product2.id());
    }
}