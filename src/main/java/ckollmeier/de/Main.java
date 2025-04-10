package ckollmeier.de;

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
import ckollmeier.de.ValidationHelpers.NotBlankString;
import ckollmeier.de.ValidationHelpers.NotNullString;
import ckollmeier.de.ValidationHelpers.NotNullUnitEnum;
import ckollmeier.de.ValidationHelpers.PositiveOrZeroBigDecimal;

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
        System.out.print("\u001B[2J");
        System.out.println("\u001B[1;32mMain Menu:");
        System.out.println("\u001B[1;32m1.\u001B[0m Manage Products");
        System.out.println("\u001B[1;32m2.\u001B[0m Manage Stock");
        System.out.println("\u001B[1;32m3.\u001B[0m Manage Orders");
        System.out.println("\u001B[1;32m0.\u001B[0m Exit");
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
        System.out.print("\u001B[2J");
        System.out.println("\u001B[1;32mProduct Management:");
        System.out.println("\u001B[1;32m1.\u001B[0m Add Product");
        System.out.println("\u001B[1;32m2.\u001B[0m Remove Product");
        System.out.println("\u001B[1;32m3.\u001B[0m List Products");
        System.out.println("\u001B[1;32m0.\u001B[0m Back to Main Menu");
        System.out.print("Enter your choice: ");
    }

    private static void addProduct() {
        String name = ValidatedInput.getValidatedInput("Enter product name:", NotBlankString.class);
        String description = ValidatedInput.getValidatedInput("Enter product description:", NotNullString.class);
        BigDecimal content = ValidatedInput.getValidatedInput("Enter product content:", PositiveOrZeroBigDecimal.class);
        UnitEnum unit = ValidatedInput.getValidatedInput("Enter product unit (KG, L, PCS):", NotNullUnitEnum.class);

        Product product = ProductBuilder.builder().name(name).description(description).content(content).unit(unit).build();
        try {
            SHOP_SERVICE.addProduct(product);
            System.out.println("Product added successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error adding product: " + e.getMessage());
        }
    }

    private static void removeProduct() {
        String productId = ValidatedInput.getValidatedInput("Enter product ID to remove:", NotBlankString.class);
        PRODUCT_REPOSITORY.find(productId).ifPresentOrElse(product -> {
            try {
                SHOP_SERVICE.removeProduct(product);
                System.out.println("Product removed successfully.");
            } catch (IllegalArgumentException e) {
                System.out.println("Error removing product: " + e.getMessage());
            }
        }, () -> System.out.println("Product not found."));
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
        System.out.print("\u001B[2J");
        System.out.println("Stock Management:");
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
        PRODUCT_REPOSITORY.find(productId).ifPresentOrElse(product -> {
            BigDecimal quantity = ValidatedInput.getValidatedInput("Enter quantity:", PositiveOrZeroBigDecimal.class);
            UnitEnum unit = ValidatedInput.getValidatedInput("Enter unit (KG, L, PCS):", NotNullUnitEnum.class);
            BigDecimal price = ValidatedInput.getValidatedInput("Enter price:", PositiveOrZeroBigDecimal.class);
            try {
                SHOP_SERVICE.addStock(product, quantity, unit, price);
                System.out.println("Stock added successfully.");
            } catch (IllegalArgumentException e) {
                System.out.println("Error adding stock: " + e.getMessage());
            }
        }, () -> System.out.println("Product not found."));
    }

    private static void increaseStock() {
        String productId = ValidatedInput.getValidatedInput("Enter product ID:", NotBlankString.class);
        PRODUCT_REPOSITORY.find(productId).ifPresentOrElse(product -> {
            BigDecimal quantity = ValidatedInput.getValidatedInput("Enter quantity to increase:", PositiveOrZeroBigDecimal.class);
            UnitEnum unit = ValidatedInput.getValidatedInput("Enter unit of quantity (KG, L, PCS):", NotNullUnitEnum.class);
            try {
                SHOP_SERVICE.increaseStock(product, quantity, unit);
                System.out.println("Stock increased successfully.");
            } catch (IllegalArgumentException e) {
                System.out.println("Error increasing stock: " + e.getMessage());
            }
        }, () -> System.out.println("Product not found."));
    }

    private static void decreaseStock() {
        String productId = ValidatedInput.getValidatedInput("Enter product ID:", NotBlankString.class);
        PRODUCT_REPOSITORY.find(productId).ifPresentOrElse(product -> {
            BigDecimal quantity = ValidatedInput.getValidatedInput("Enter quantity to decreaase:", PositiveOrZeroBigDecimal.class);
            UnitEnum unit = ValidatedInput.getValidatedInput("Enter unit of quantity (KG, L, PCS):", NotNullUnitEnum.class);
            try {
                SHOP_SERVICE.decreaseStock(product, quantity, unit);
                System.out.println("Stock decreased successfully.");
            } catch (IllegalArgumentException e) {
                System.out.println("Error decreasing stock: " + e.getMessage());
            }
        }, () -> System.out.println("Product not found."));
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
        System.out.print("\u001B[2J");
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
            String productId = ValidatedInput.getValidatedInput("Enter product ID (or type 'done' to finish):", NotBlankString.class);
            if (productId.equalsIgnoreCase("done")) {
                addingProducts = false;
            } else {
                STOCK_REPOSITORY.find(productId).ifPresentOrElse(stockArticle -> {
                    BigDecimal quantity = ValidatedInput.getValidatedInput("Enter quantity:", PositiveOrZeroBigDecimal.class);
                    OrderProduct orderProduct = new OrderProduct(stockArticle);
                    orderProduct.setQuantity(quantity);
                    orderProducts.add(orderProduct);
                }, () -> System.out.println("Product not found."));
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
        String orderId = ValidatedInput.getValidatedInput("Enter order ID to remove:", NotBlankString.class);
        ORDER_REPOSITORY.find(orderId).ifPresentOrElse(
            order -> {
                SHOP_SERVICE.removeOrder(order).ifPresentOrElse(
                        removedOrder -> System.out.println("Order removed successfully."),
                        () -> System.out.println("Error removing order")
                );
            }, () ->  System.out.println("Order not found.")
        );
    }
}