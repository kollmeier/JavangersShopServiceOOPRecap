package ckollmeier.de.Repository;

import ckollmeier.de.Entity.Product;
import ckollmeier.de.Entity.ProductBuilder;
import ckollmeier.de.Enum.UnitEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class StockRepositoryTest {
//    @ParameterizedTest
//    @CsvSource({
//            "100, g, 10, g, 110",
//            "100, g, 20, mg, 100.020",
//            "100, g, 30, kg, 30100",
//            "10, mg, 5, g, 5010",
//            "10, mg, 1, t, 1000000010",
//            "1, t, 10, mg, 1.000000010",
//            "1, t, 10, g, 1.000010",
//            "1, l, 250, ml, 1.250",
//            "500, ml, 25, cl, 750",
//            "500, ml, 25, dl, 3000",
//            "500, ml, 1, m^3, 1000500",
//            "2, l, 500, ml, 2.500"
//    })
//    void increaseQuantityShouldIncreaseTheQuantityByAddingUpAndNoticingTheConversionFactor(
//            String existing,
//            String existingUnit,
//            String addUp,
//            String addUpUnit,
//            String result) {
//
//        Product product = ProductBuilder.builder()
//                .name("Coca-Cola")
//                .unit(UnitEnum.getUnitForShort(existingUnit))
//                .build();
//
//        Product addedProduct = productRepository.addProduct(product);
//
//        addedProduct = productRepository.increaseQuantity(addedProduct, new BigDecimal(addUp), UnitEnum.getUnitForShort(addUpUnit));
//
//        assertThat(addedProduct.quantity()).isEqualTo(new BigDecimal(result));
//    }
//
//    @ParameterizedTest
//    @CsvSource({
//            "g, l",
//            "g, ml",
//            "g, m^3",
//            "g, p",
//            "mg, l",
//            "mg, ml",
//            "mg, ml",
//            "kg, m^3",
//            "kg, l",
//            "kg, m^3",
//            "kg, p",
//            "ml, p",
//    })
//    void increaseQuantityShouldThrowIllegalArgumentExceptionWhenUnitsNotConvertible(
//            String existingUnit,
//            String addUpUnit
//    ) {
//        ProductRepository productRepository = new ProductRepository();
//
//        Product product = ProductBuilder.builder()
//                .name("Coca-Cola")
//                .quantity(new BigDecimal("1.0"))
//                .unit(UnitEnum.getUnitForShort(existingUnit))
//                .build();
//
//        final Product addedProduct = productRepository.addProduct(product);
//
//        assertThrows(IllegalArgumentException.class, () -> {
//            productRepository.increaseQuantity(addedProduct, new BigDecimal("1.0"), UnitEnum.getUnitForShort(addUpUnit));
//        });
//    }
//
//    @Test
//    void increaseQuantityShouldThrowIllegalArgumentExceptionWhenProductIsNull() {
//        ProductRepository productRepository = new ProductRepository();
//        assertThrows(IllegalArgumentException.class, () -> {
//            productRepository.increaseQuantity(null, new BigDecimal("1.0"), UnitEnum.getUnitForShort("m^3"));
//        });
//    }
//
//    @Test
//    void increaseQuantityShouldThrowIllegalArgumentExceptionWhenProductIdIsNull() {
//        ProductRepository productRepository = new ProductRepository();
//
//        Product product = ProductBuilder.builder()
//                .name("Coca-Cola")
//                .quantity(new BigDecimal("1.0"))
//                .unit(UnitEnum.MG)
//                .build();
//
//        assertThrows(IllegalArgumentException.class, () -> {
//            productRepository.increaseQuantity(product, new BigDecimal("1.0"), UnitEnum.getUnitForShort("m^3"));
//        });
//    }
//
//    @Test
//    void increaseQuantityShouldThrowIllegalArgumentExceptionWhenQuantityIsNull() {
//        ProductRepository productRepository = new ProductRepository();
//
//        Product product = ProductBuilder.builder()
//                .name("Coca-Cola")
//                .quantity(new BigDecimal("1.0"))
//                .unit(UnitEnum.MG)
//                .build();
//        Product addedProduct = productRepository.addProduct(product);
//
//        assertThrows(IllegalArgumentException.class, () -> {
//            productRepository.increaseQuantity(addedProduct, null, UnitEnum.getUnitForShort("m^3"));
//        });
//    }
//
//    @Test
//    void increaseQuantityShouldThrowIllegalArgumentExceptionUnitIsNull() {
//        ProductRepository productRepository = new ProductRepository();
//
//        Product product = ProductBuilder.builder()
//                .name("Coca-Cola")
//                .quantity(new BigDecimal("1.0"))
//                .unit(UnitEnum.MG)
//                .build();
//        Product addedProduct = productRepository.addProduct(product);
//
//        assertThrows(IllegalArgumentException.class, () -> {
//            productRepository.increaseQuantity(addedProduct, new BigDecimal(1), null);
//        });
//    }
//
//    @ParameterizedTest
//    @CsvSource({
//            "100, g, 10, g, 90",
//            "100, g, 20, mg, 99.980",
//            "30, kg, 100, g, 29.900",
//            "10, mg, 5, g, -4990", // negative values possible
//            "10, mg, 1, t, -999999990", // negative values possible
//            "1, t, 10, mg, 0.999999990",
//            "1, t, 10, g, 0.999990",
//            "1, l, 250, ml, 0.750",
//            "500, ml, 25, cl, 250",
//            "500, ml, 25, dl, -2000",
//            "500, ml, 1, m^3, -999500",
//            "2, l, 500, ml, 1.500"
//    })
//    void decreaseQuantityShouldDecreaseTheQuantityByAddingUpAndNoticingTheConversionFactor(
//            String existing,
//            String existingUnit,
//            String reduce,
//            String reduceUnit,
//            String result) {
//        ProductRepository productRepository = new ProductRepository();
//
//        Product product = ProductBuilder.builder()
//                .name("Coca-Cola")
//                .quantity(new BigDecimal(existing))
//                .unit(UnitEnum.getUnitForShort(existingUnit))
//                .build();
//
//        Product addedProduct = productRepository.addProduct(product);
//
//        addedProduct = productRepository.decreaseQuantity(addedProduct, new BigDecimal(reduce), UnitEnum.getUnitForShort(reduceUnit));
//
//        assertThat(addedProduct.quantity()).isEqualTo(new BigDecimal(result));
//    }
//
//    @ParameterizedTest
//    @CsvSource({
//            "g, l",
//            "g, ml",
//            "g, m^3",
//            "g, p",
//            "mg, l",
//            "mg, ml",
//            "mg, ml",
//            "kg, m^3",
//            "kg, l",
//            "kg, m^3",
//            "kg, p",
//            "ml, p",
//    })
//    void decreaseQuantityShouldThrowIllegalArgumentExceptionWhenUnitsNotConvertible(
//            String existingUnit,
//            String addUpUnit
//    ) {
//        ProductRepository productRepository = new ProductRepository();
//
//        Product product = ProductBuilder.builder()
//                .name("Coca-Cola")
//                .quantity(new BigDecimal("1.0"))
//                .unit(UnitEnum.getUnitForShort(existingUnit))
//                .build();
//
//        final Product addedProduct = productRepository.addProduct(product);
//
//        assertThrows(IllegalArgumentException.class, () -> {
//            productRepository.decreaseQuantity(addedProduct, new BigDecimal("1.0"), UnitEnum.getUnitForShort(addUpUnit));
//        });
//    }
//
//    @Test
//    void decreaseQuantityShouldThrowIllegalArgumentExceptionWhenProductIsNull() {
//        ProductRepository productRepository = new ProductRepository();
//        assertThrows(IllegalArgumentException.class, () -> {
//            productRepository.decreaseQuantity(null, new BigDecimal("1.0"), UnitEnum.getUnitForShort("m^3"));
//        });
//    }
//
//    @Test
//    void decreaseQuantityShouldThrowIllegalArgumentExceptionWhenProductIdIsNull() {
//        ProductRepository productRepository = new ProductRepository();
//
//        Product product = ProductBuilder.builder()
//                .name("Coca-Cola")
//                .quantity(new BigDecimal("1.0"))
//                .unit(UnitEnum.MG)
//                .build();
//
//        assertThrows(IllegalArgumentException.class, () -> {
//            productRepository.decreaseQuantity(product, new BigDecimal("1.0"), UnitEnum.getUnitForShort("m^3"));
//        });
//    }
//
//    @Test
//    void decreaseQuantityShouldThrowIllegalArgumentExceptionWhenQuantityIsNull() {
//        ProductRepository productRepository = new ProductRepository();
//
//        Product product = ProductBuilder.builder()
//                .name("Coca-Cola")
//                .quantity(new BigDecimal("1.0"))
//                .unit(UnitEnum.MG)
//                .build();
//        Product addedProduct = productRepository.addProduct(product);
//
//        assertThrows(IllegalArgumentException.class, () -> {
//            productRepository.decreaseQuantity(addedProduct, null, UnitEnum.getUnitForShort("m^3"));
//        });
//    }
//
//    @Test
//    void decreaseQuantityShouldThrowIllegalArgumentExceptionUnitIsNull() {
//        ProductRepository productRepository = new ProductRepository();
//
//        Product product = ProductBuilder.builder()
//                .name("Coca-Cola")
//                .quantity(new BigDecimal("1.0"))
//                .unit(UnitEnum.MG)
//                .build();
//        Product addedProduct = productRepository.addProduct(product);
//
//        assertThrows(IllegalArgumentException.class, () -> {
//            productRepository.decreaseQuantity(addedProduct, new BigDecimal(1), null);
//        });
//    }
}
