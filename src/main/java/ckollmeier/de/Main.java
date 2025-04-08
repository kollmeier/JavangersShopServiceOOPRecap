package ckollmeier.de;

import ckollmeier.de.Entity.Interface.ProductInterface;
import ckollmeier.de.Entity.Order;
import ckollmeier.de.Entity.OrderBuilder;
import ckollmeier.de.Entity.OrderProduct;
import ckollmeier.de.Entity.Product;
import ckollmeier.de.Entity.ProductBuilder;
import ckollmeier.de.Entity.StockArticle;
import ckollmeier.de.Enum.UnitEnum;
import ckollmeier.de.Repository.OrderRepository;
import ckollmeier.de.Repository.ProductRepository;
import ckollmeier.de.Repository.StockRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final OrderRepository ORDER_REPOSITORY = new OrderRepository();
    private static final StockRepository STOCK_REPOSITORY = new StockRepository();
    private static final ProductRepository PRODUCT_REPOSITORY = new ProductRepository();
    private static final ShopService SHOP_SERVICE = new ShopService(ORDER_REPOSITORY, STOCK_REPOSITORY, PRODUCT_REPOSITORY);

    public static void main(final String[] args) {
        boolean running = true;
        while (running) {
            printMainMenu();
            String choice = SCANNER.nextLine();
            switch (choice) {
                case "1":
                    manageProducts();
                    break;
                case "2":
                    manageStock();
                    break;
                case "3":
                    manageOrders();
                    break;
                case "0":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        System.out.println("Exiting the application.");
    }

    private static void printMainMenu() {
        System.out.println("\nMain Menu:");
        System.out.println("1. Manage Products");
        System.out.println("2. Manage Stock");
        System.out.println("3. Manage Orders");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void manageProducts() {
        boolean managingProducts = true;
        while (managingProducts) {
            printProductMenu();
            String choice = SCANNER.nextLine();
            switch (choice) {
                case "1":
                    addProduct();
                    break;
                case "2":
                    removeProduct();
                    break;
                case "3":
                    listProducts();
                    break;
                case "0":
                    managingProducts = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void printProductMenu() {
        System.out.println("\nProduct Management:");
        System.out.println("1. Add Product");
        System.out.println("2. Remove Product");
        System.out.println("3. List Products");
        System.out.println("0. Back to Main Menu");
        System.out.print("Enter your choice: ");
    }

    private static void addProduct() {
        System.out.print("Enter product name: ");
        String name = SCANNER.nextLine();
        System.out.print("Enter product description: ");
        String description = SCANNER.nextLine();
        System.out.print("Enter product content: ");
        BigDecimal content = new BigDecimal(SCANNER.nextLine());
        System.out.print("Enter product unit (KG, L, PCS): ");
        UnitEnum unit = UnitEnum.valueOf(SCANNER.nextLine());

        Product product = ProductBuilder.builder().name(name).description(description).content(content).unit(unit).build();
        try {
            SHOP_SERVICE.addProduct(product);
            System.out.println("Product added successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error adding product: " + e.getMessage());
        }
    }

    private static void removeProduct() {
        System.out.print("Enter product ID to remove: ");
        String productId = SCANNER.nextLine();
        Product product = PRODUCT_REPOSITORY.find(productId);
        if (product != null) {
            try {
                SHOP_SERVICE.removeProduct(product);
                System.out.println("Product removed successfully.");
            } catch (IllegalArgumentException e) {
                System.out.println("Error removing product: " + e.getMessage());
            }
        } else {
            System.out.println("Product not found.");
        }
    }

    private static void listProducts() {
        List<Product> products = SHOP_SERVICE.getAllProducts();
        if (products.isEmpty()) {
            System.out.println("No products found.");
        } else {
            System.out.println("\nList of Products:");
            for (Product product : products) {
                System.out.println("ID: " + product.id() + ", Name: " + product.name() + ", Description: " + product.description() + ", Content: " + product.content() + ", Unit: " + product.unit());
            }
        }
    }

    private static void manageStock() {
        boolean managingStock = true;
        while (managingStock) {
            printStockMenu();
            String choice = SCANNER.nextLine();
            switch (choice) {
                case "1":
                    addStock();
                    break;
                case "2":
                    increaseStock();
                    break;
                case "3":
                    decreaseStock();
                    break;
                case "4":
                    listStock();
                    break;
                case "0":
                    managingStock = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void printStockMenu() {
        System.out.println("\nStock Management:");
        System.out.println("1. Add Stock");
        System.out.println("2. Increase Stock");
        System.out.println("3. Decrease Stock");
        System.out.println("4. List Stock");
        System.out.println("0. Back to Main Menu");
        System.out.print("Enter your choice: ");
    }

    private static void addStock() {
        System.out.print("Enter product ID: ");
        String productId = SCANNER.nextLine();
        ProductInterface product = PRODUCT_REPOSITORY.find(productId);
        if (product != null) {
            System.out.print("Enter quantity: ");
            BigDecimal quantity = new BigDecimal(SCANNER.nextLine());
            System.out.print("Enter unit (KG, L, PCS): ");
            UnitEnum unit = UnitEnum.valueOf(SCANNER.nextLine());
            System.out.print("Enter price: ");
            BigDecimal price = new BigDecimal(SCANNER.nextLine());
            try {
                SHOP_SERVICE.addStock(product, quantity, unit, price);
                System.out.println("Stock added successfully.");
            } catch (IllegalArgumentException e) {
                System.out.println("Error adding stock: " + e.getMessage());
            }
        } else {
            System.out.println("Product not found.");
        }
    }

    private static void increaseStock() {
        System.out.print("Enter product ID: ");
        String productId = SCANNER.nextLine();
        ProductInterface product = PRODUCT_REPOSITORY.find(productId);
        if (product != null) {
            System.out.print("Enter quantity to increase: ");
            BigDecimal quantity = new BigDecimal(SCANNER.nextLine());
            System.out.print("Enter unit (KG, L, PCS): ");
            UnitEnum unit = UnitEnum.valueOf(SCANNER.nextLine());
            try {
                SHOP_SERVICE.increaseStock(product, quantity, unit);
                System.out.println("Stock increased successfully.");
            } catch (IllegalArgumentException e) {
                System.out.println("Error increasing stock: " + e.getMessage());
            }
        } else {
            System.out.println("Product not found.");
        }
    }

    private static void decreaseStock() {
        System.out.print("Enter product ID: ");
        String productId = SCANNER.nextLine();
        ProductInterface product = PRODUCT_REPOSITORY.find(productId);
        if (product != null) {
            System.out.print("Enter quantity to decrease: ");
            BigDecimal quantity = new BigDecimal(SCANNER.nextLine());
            System.out.print("Enter unit (KG, L, PCS): ");
            UnitEnum unit = UnitEnum.valueOf(SCANNER.nextLine());
            try {
                SHOP_SERVICE.decreaseStock(product, quantity, unit);
                System.out.println("Stock decreased successfully.");
            } catch (IllegalArgumentException e) {
                System.out.println("Error decreasing stock: " + e.getMessage());
            }
        } else {
            System.out.println("Product not found.");
        }
    }

    private static void listStock() {
        List<StockArticle> stock = SHOP_SERVICE.getAllStock();
        if (stock.isEmpty()) {
            System.out.println("No stock found.");
        } else {
            System.out.println("\nList of Stock:");
            for (StockArticle stockArticle : stock) {
                System.out.println("Product ID: " + stockArticle.productId() + ", Quantity: " + stockArticle.quantity() + ", Unit: " + stockArticle.unit() + ", Price: " + stockArticle.price());
            }
        }
    }

    private static void manageOrders() {
        boolean managingOrders = true;
        while (managingOrders) {
            printOrderMenu();
            String choice = SCANNER.nextLine();
            switch (choice) {
                case "1":
                    addOrder();
                    break;
                case "2":
                    removeOrder();
                    break;
                case "0":
                    managingOrders = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void printOrderMenu() {
        System.out.println("\nOrder Management:");
        System.out.println("1. Add Order");
        System.out.println("2. Remove Order");
        System.out.println("0. Back to Main Menu");
        System.out.print("Enter your choice: ");
    }

    private static void addOrder() {
        List<OrderProduct> orderProducts = new ArrayList<>();
        boolean addingProducts = true;
        while (addingProducts) {
            System.out.print("Enter product ID (or type 'done' to finish): ");
            String productId = SCANNER.nextLine();
            if (productId.equalsIgnoreCase("done")) {
                addingProducts = false;
            } else {
                ProductInterface product = STOCK_REPOSITORY.find(productId);
                if (product != null) {
                    System.out.print("Enter quantity: ");
                    BigDecimal quantity = new BigDecimal(SCANNER.nextLine());
                    OrderProduct orderProduct = new OrderProduct(
                            STOCK_REPOSITORY.findByProductId(product.id())
                    );
                    orderProduct.setQuantity(quantity);
                    orderProducts.add(orderProduct);
                } else {
                    System.out.println("Product not found.");
                }
            }
        }
        Order order = OrderBuilder.builder().products(orderProducts).build();
        try {
            SHOP_SERVICE.addOrder(order);
            System.out.println("Order added successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error adding order: " + e.getMessage());
        }
    }

    private static void removeOrder() {
        System.out.print("Enter order ID to remove: ");
        String orderId = SCANNER.nextLine();
        Order order = ORDER_REPOSITORY.find(orderId);
        if (order != null) {
            try {
                SHOP_SERVICE.removeOrder(order);
                System.out.println("Order removed successfully.");
            } catch (IllegalArgumentException e) {
                System.out.println("Error removing order: " + e.getMessage());
            }
        } else {
            System.out.println("Order not found.");
        }
    }
}