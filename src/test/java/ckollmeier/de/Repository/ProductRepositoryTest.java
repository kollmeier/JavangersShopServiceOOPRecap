package ckollmeier.de.Repository;

import ckollmeier.de.Entity.Product;
import ckollmeier.de.Entity.ProductBuilder;
import ckollmeier.de.Enum.UnitEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

class ProductRepositoryTest {

    @Test
    void addProductShouldThrowIllegalArgumentExceptionWhenProductIsNull() {
        ProductRepository productRepository = new ProductRepository();

        assertThrows(IllegalArgumentException.class, () -> {
            productRepository.addProduct(null);
        });
    }

    @Test
    void addProductShouldAddProductWithUuidAsIdWhenProductHasNoUuid() {
        ProductRepository productRepository = new ProductRepository();
        Product product = ProductBuilder.builder()
                .name("Coca-Cola").build();

        Product addedProduct = productRepository.addProduct(product);

        assertNotNull(product, addedProduct.id());
        assertThat(addedProduct.id()).matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}");
    }

    @Test
    void addProductShouldAddProductWithUnchangedUuidWhenProductHasUuid() {
        ProductRepository productRepository = new ProductRepository();
        Product product = ProductBuilder.builder()
                .name("Coca-Cola")
                .id("12214309-08e7-4875-beb1-33da650e04f7")
                .build();

        Product addedProduct = productRepository.addProduct(product);

        assertNotNull(product, addedProduct.id());
        assertThat(addedProduct.id()).isEqualTo("12214309-08e7-4875-beb1-33da650e04f7");
    }

    @Test
    void addProductShouldThrowInvalidArgumentExceptionWhenProductWithIdAlreadyExists() {
        ProductRepository productRepository = new ProductRepository();
        Product product = ProductBuilder.builder()
                .name("Coca-Cola")
                .build();

        Product addedProduct = productRepository.addProduct(product);

        assertThrows(IllegalArgumentException.class, () -> {
            productRepository.addProduct(addedProduct);
        });
    }

    @Test
    void addProductShouldIncreaseProductCountWhenProductWasAdded() {
        ProductRepository productRepository = new ProductRepository();
        Product product = ProductBuilder.builder()
                .name("Coca-Cola")
                .build();

        Product addedProduct = productRepository.addProduct(product);

        Product product2 = ProductBuilder.builder()
                .name("Fanta")
                .build();

        Product addedProduct2 = productRepository.addProduct(product2);

        assertThat(productRepository.countProducts()).isEqualTo(2);
    }

    @Test
    void removeProductShouldThrowIllegalArgumentExceptionWhenProductIsNull() {
        ProductRepository productRepository = new ProductRepository();
        assertThrows(IllegalArgumentException.class, () -> {
            productRepository.removeProduct(null);
        });
    }

    @Test
    void removeProductShouldThrowIllegalArgumentExceptionWhenProductIdIsNull() {
        ProductRepository productRepository = new ProductRepository();

        Product product = ProductBuilder.builder()
                .name("Cola").build();

        assertThrows(IllegalArgumentException.class, () -> {
            productRepository.removeProduct(product);
        });
    }

    @Test
    void removeProductShouldThrowIllegalArgumentExceptionWhenProductIsNotFound() {
        ProductRepository productRepository = new ProductRepository();

        Product product = ProductBuilder.builder()
                .name("Cola").build();

        productRepository.addProduct(product);

        Product product2 = ProductBuilder.builder()
                .name("Fanta").build();

        assertThrows(IllegalArgumentException.class, () -> {
            productRepository.removeProduct(product2);
        });
    }

    @Test
    void removeProductShouldDecreaseProductCountWhenProductWasRemoved() {
        ProductRepository productRepository = new ProductRepository();
        Product product = ProductBuilder.builder()
                .name("Coca-Cola")
                .build();

        Product addedProduct = productRepository.addProduct(product);

        Product product2 = ProductBuilder.builder()
                .name("Fanta")
                .build();

        Product addedProduct2 = productRepository.addProduct(product2);

        productRepository.removeProduct(addedProduct);

        assertThat(productRepository.countProducts()).isEqualTo(1);
    }

    @Test
    void removeProductWithIdShouldThrowIllegalArgumentExceptionWhenProductIsNull() {
        ProductRepository productRepository = new ProductRepository();
        assertThrows(IllegalArgumentException.class, () -> {
            productRepository.removeProductWithId(null);
        });
    }

    @Test
    void removeProductWithIdShouldThrowIllegalArgumentExceptionWhenProductIsNotFound() {
        ProductRepository productRepository = new ProductRepository();

        Product product = ProductBuilder.builder()
                .name("Cola").build();

        productRepository.addProduct(product);

        Product product2 = ProductBuilder.builder()
                .name("Fanta").build();

        assertThrows(IllegalArgumentException.class, () -> {
            productRepository.removeProductWithId(product2.id());
        });
    }

    @Test
    void removeProductWithIdShouldDecreaseProductCountWhenProductWasRemoved() {
        ProductRepository productRepository = new ProductRepository();
        Product product = ProductBuilder.builder()
                .name("Coca-Cola")
                .build();

        Product addedProduct = productRepository.addProduct(product);

        Product product2 = ProductBuilder.builder()
                .name("Fanta")
                .build();

        Product addedProduct2 = productRepository.addProduct(product2);

        productRepository.removeProductWithId(addedProduct.id());

        assertThat(productRepository.countProducts()).isEqualTo(1);
    }

    @Test
    void findShouldThrowIllegalArgumentExceptionWhenIdIsNull() {
        ProductRepository productRepository = new ProductRepository();
        assertThrows(IllegalArgumentException.class, () -> {
            productRepository.find(null);
        });
    }

    @Test
    void findShouldReturnNullWhenIdIsNotInRepository() {
        ProductRepository productRepository = new ProductRepository();

        Product product = ProductBuilder.builder()
                .name("Coca-Cola")
                .build();

        Product addedProduct = productRepository.addProduct(product);

        assertThat(productRepository.find("xxxxxxx")).isNull();
    }

    @Test
    void findShouldReturnProductWhenIdIsInRepository() {
        ProductRepository productRepository = new ProductRepository();

        Product product = ProductBuilder.builder()
                .name("Coca-Cola")
                .build();
        Product addedProduct = productRepository.addProduct(product);


        Product product2 = ProductBuilder.builder()
                .name("Fanta")
                .build();
        Product addedProduct2 = productRepository.addProduct(product);

        assertThat(productRepository.find(addedProduct.id())).isEqualTo(addedProduct);
    }

    @Test
    void findAll() {
        ProductRepository productRepository = new ProductRepository();

        Product product = ProductBuilder.builder()
                .name("Coca-Cola")
                .build();
        Product addedProduct = productRepository.addProduct(product);


        Product product2 = ProductBuilder.builder()
                .name("Fanta")
                .build();
        Product addedProduct2 = productRepository.addProduct(product);

        assertThat(productRepository.findAll()).hasSize(2);
    }

    @ParameterizedTest
    @CsvSource({
            "100, g, 10, g, 110",
            "100, g, 20, mg, 100.020",
            "100, g, 30, kg, 30100",
            "10, mg, 5, g, 5010",
            "10, mg, 1, t, 1000000010",
            "1, t, 10, mg, 1.000000010",
            "1, t, 10, g, 1.000010",
            "1, l, 250, ml, 1.250",
            "500, ml, 25, cl, 750",
            "500, ml, 25, dl, 3000",
            "500, ml, 1, m^3, 1000500",
            "2, l, 500, ml, 2.500"
    })
    void increaseQuantityShouldIncreaseTheQuantityByAddingUpAndNoticingTheConversionFactor(
            String existing,
            String existingUnit,
            String addUp,
            String addUpUnit,
            String result) {
        ProductRepository productRepository = new ProductRepository();


        Product product = ProductBuilder.builder()
                .name("Coca-Cola")
                .quantity(new BigDecimal(existing))
                .unit(UnitEnum.getUnitForShort(existingUnit))
                .build();

        Product addedProduct = productRepository.addProduct(product);

        addedProduct = productRepository.increaseQuantity(addedProduct, new BigDecimal(addUp), UnitEnum.getUnitForShort(addUpUnit));

        assertThat(addedProduct.quantity()).isEqualTo(new BigDecimal(result));
    }

    @ParameterizedTest
    @CsvSource({
            "g, l",
            "g, ml",
            "g, m^3",
            "g, p",
            "mg, l",
            "mg, ml",
            "mg, ml",
            "kg, m^3",
            "kg, l",
            "kg, m^3",
            "kg, p",
            "ml, p",
    })
    void increaseQuantityShouldThrowIllegalArgumentExceptionWhenUnitsNotConvertible(
            String existingUnit,
            String addUpUnit
    ) {
        ProductRepository productRepository = new ProductRepository();

        Product product = ProductBuilder.builder()
                .name("Coca-Cola")
                .quantity(new BigDecimal("1.0"))
                .unit(UnitEnum.getUnitForShort(existingUnit))
                .build();

        final Product addedProduct = productRepository.addProduct(product);

        assertThrows(IllegalArgumentException.class, () -> {
            productRepository.increaseQuantity(addedProduct, new BigDecimal("1.0"), UnitEnum.getUnitForShort(addUpUnit));
        });
    }

    @Test
    void increaseQuantityShouldThrowIllegalArgumentExceptionWhenProductIsNull() {
        ProductRepository productRepository = new ProductRepository();
        assertThrows(IllegalArgumentException.class, () -> {
            productRepository.increaseQuantity(null, new BigDecimal("1.0"), UnitEnum.getUnitForShort("m^3"));
        });
    }

    @Test
    void increaseQuantityShouldThrowIllegalArgumentExceptionWhenProductIdIsNull() {
        ProductRepository productRepository = new ProductRepository();

        Product product = ProductBuilder.builder()
                .name("Coca-Cola")
                .quantity(new BigDecimal("1.0"))
                .unit(UnitEnum.MG)
                .build();

        assertThrows(IllegalArgumentException.class, () -> {
            productRepository.increaseQuantity(product, new BigDecimal("1.0"), UnitEnum.getUnitForShort("m^3"));
        });
    }

    @Test
    void increaseQuantityShouldThrowIllegalArgumentExceptionWhenQuantityIsNull() {
        ProductRepository productRepository = new ProductRepository();

        Product product = ProductBuilder.builder()
                .name("Coca-Cola")
                .quantity(new BigDecimal("1.0"))
                .unit(UnitEnum.MG)
                .build();
        Product addedProduct = productRepository.addProduct(product);

        assertThrows(IllegalArgumentException.class, () -> {
            productRepository.increaseQuantity(addedProduct, null, UnitEnum.getUnitForShort("m^3"));
        });
    }

    @Test
    void increaseQuantityShouldThrowIllegalArgumentExceptionUnitIsNull() {
        ProductRepository productRepository = new ProductRepository();

        Product product = ProductBuilder.builder()
                .name("Coca-Cola")
                .quantity(new BigDecimal("1.0"))
                .unit(UnitEnum.MG)
                .build();
        Product addedProduct = productRepository.addProduct(product);

        assertThrows(IllegalArgumentException.class, () -> {
            productRepository.increaseQuantity(addedProduct, new BigDecimal(1), null);
        });
    }

    @ParameterizedTest
    @CsvSource({
            "100, g, 10, g, 90",
            "100, g, 20, mg, 99.980",
            "30, kg, 100, g, 29.900",
            "10, mg, 5, g, -4990", // negative values possible
            "10, mg, 1, t, -999999990", // negative values possible
            "1, t, 10, mg, 0.999999990",
            "1, t, 10, g, 0.999990",
            "1, l, 250, ml, 0.750",
            "500, ml, 25, cl, 250",
            "500, ml, 25, dl, -2000",
            "500, ml, 1, m^3, -999500",
            "2, l, 500, ml, 1.500"
    })
    void decreaseQuantityShouldDecreaseTheQuantityByAddingUpAndNoticingTheConversionFactor(
            String existing,
            String existingUnit,
            String reduce,
            String reduceUnit,
            String result) {
        ProductRepository productRepository = new ProductRepository();

        Product product = ProductBuilder.builder()
                .name("Coca-Cola")
                .quantity(new BigDecimal(existing))
                .unit(UnitEnum.getUnitForShort(existingUnit))
                .build();

        Product addedProduct = productRepository.addProduct(product);

        addedProduct = productRepository.decreaseQuantity(addedProduct, new BigDecimal(reduce), UnitEnum.getUnitForShort(reduceUnit));

        assertThat(addedProduct.quantity()).isEqualTo(new BigDecimal(result));
    }

    @ParameterizedTest
    @CsvSource({
            "g, l",
            "g, ml",
            "g, m^3",
            "g, p",
            "mg, l",
            "mg, ml",
            "mg, ml",
            "kg, m^3",
            "kg, l",
            "kg, m^3",
            "kg, p",
            "ml, p",
    })
    void decreaseQuantityShouldThrowIllegalArgumentExceptionWhenUnitsNotConvertible(
            String existingUnit,
            String addUpUnit
    ) {
        ProductRepository productRepository = new ProductRepository();

        Product product = ProductBuilder.builder()
                .name("Coca-Cola")
                .quantity(new BigDecimal("1.0"))
                .unit(UnitEnum.getUnitForShort(existingUnit))
                .build();

        final Product addedProduct = productRepository.addProduct(product);

        assertThrows(IllegalArgumentException.class, () -> {
            productRepository.decreaseQuantity(addedProduct, new BigDecimal("1.0"), UnitEnum.getUnitForShort(addUpUnit));
        });
    }

    @Test
    void decreaseQuantityShouldThrowIllegalArgumentExceptionWhenProductIsNull() {
        ProductRepository productRepository = new ProductRepository();
        assertThrows(IllegalArgumentException.class, () -> {
            productRepository.decreaseQuantity(null, new BigDecimal("1.0"), UnitEnum.getUnitForShort("m^3"));
        });
    }

    @Test
    void decreaseQuantityShouldThrowIllegalArgumentExceptionWhenProductIdIsNull() {
        ProductRepository productRepository = new ProductRepository();

        Product product = ProductBuilder.builder()
                .name("Coca-Cola")
                .quantity(new BigDecimal("1.0"))
                .unit(UnitEnum.MG)
                .build();

        assertThrows(IllegalArgumentException.class, () -> {
            productRepository.decreaseQuantity(product, new BigDecimal("1.0"), UnitEnum.getUnitForShort("m^3"));
        });
    }

    @Test
    void decreaseQuantityShouldThrowIllegalArgumentExceptionWhenQuantityIsNull() {
        ProductRepository productRepository = new ProductRepository();

        Product product = ProductBuilder.builder()
                .name("Coca-Cola")
                .quantity(new BigDecimal("1.0"))
                .unit(UnitEnum.MG)
                .build();
        Product addedProduct = productRepository.addProduct(product);

        assertThrows(IllegalArgumentException.class, () -> {
            productRepository.decreaseQuantity(addedProduct, null, UnitEnum.getUnitForShort("m^3"));
        });
    }

    @Test
    void decreaseQuantityShouldThrowIllegalArgumentExceptionUnitIsNull() {
        ProductRepository productRepository = new ProductRepository();

        Product product = ProductBuilder.builder()
                .name("Coca-Cola")
                .quantity(new BigDecimal("1.0"))
                .unit(UnitEnum.MG)
                .build();
        Product addedProduct = productRepository.addProduct(product);

        assertThrows(IllegalArgumentException.class, () -> {
            productRepository.decreaseQuantity(addedProduct, new BigDecimal(1), null);
        });
    }

    @ParameterizedTest
    @CsvSource({
            "100, g, 10, g, 110",
            "100, g, 20, mg, 100.020",
            "100, g, 30, kg, 30100",
            "10, mg, 5, g, 5010",
            "10, mg, 1, t, 1000000010",
            "1, t, 10, mg, 1.000000010",
            "1, t, 10, g, 1.000010",
            "1, l, 250, ml, 1.250",
            "500, ml, 25, cl, 750",
            "500, ml, 25, dl, 3000",
            "500, ml, 1, m^3, 1000500",
            "2, l, 500, ml, 2.500"
    })
    void increaseQuantityByIdShouldIncreaseTheQuantityByAddingUpAndNoticingTheConversionFactor(
            String existing,
            String existingUnit,
            String addUp,
            String addUpUnit,
            String result) {
        ProductRepository productRepository = new ProductRepository();


        Product product = ProductBuilder.builder()
                .name("Coca-Cola")
                .quantity(new BigDecimal(existing))
                .unit(UnitEnum.getUnitForShort(existingUnit))
                .build();

        Product addedProduct = productRepository.addProduct(product);

        addedProduct = productRepository.increaseQuantityById(addedProduct.id(), new BigDecimal(addUp), UnitEnum.getUnitForShort(addUpUnit));

        assertThat(addedProduct.quantity()).isEqualTo(new BigDecimal(result));
    }

    @ParameterizedTest
    @CsvSource({
            "g, l",
            "g, ml",
            "g, m^3",
            "g, p",
            "mg, l",
            "mg, ml",
            "mg, ml",
            "kg, m^3",
            "kg, l",
            "kg, m^3",
            "kg, p",
            "ml, p",
    })
    void increaseQuantityByIdShouldThrowIllegalArgumentExceptionWhenUnitsNotConvertible(
            String existingUnit,
            String addUpUnit
    ) {
        ProductRepository productRepository = new ProductRepository();

        Product product = ProductBuilder.builder()
                .name("Coca-Cola")
                .quantity(new BigDecimal("1.0"))
                .unit(UnitEnum.getUnitForShort(existingUnit))
                .build();

        final Product addedProduct = productRepository.addProduct(product);

        assertThrows(IllegalArgumentException.class, () -> {
            productRepository.increaseQuantityById(addedProduct.id(), new BigDecimal("1.0"), UnitEnum.getUnitForShort(addUpUnit));
        });
    }

    @Test
    void increaseQuantityByIdShouldThrowIllegalArgumentExceptionWhenProductIdIsNull() {
        ProductRepository productRepository = new ProductRepository();
        assertThrows(IllegalArgumentException.class, () -> {
            productRepository.increaseQuantityById(null, new BigDecimal("1.0"), UnitEnum.getUnitForShort("m^3"));
        });
    }

    @Test
    void increaseQuantityByIdShouldThrowIllegalArgumentExceptionWhenQuantityIsNull() {
        ProductRepository productRepository = new ProductRepository();

        Product product = ProductBuilder.builder()
                .name("Coca-Cola")
                .quantity(new BigDecimal("1.0"))
                .unit(UnitEnum.MG)
                .build();
        Product addedProduct = productRepository.addProduct(product);

        assertThrows(IllegalArgumentException.class, () -> {
            productRepository.increaseQuantityById(addedProduct.id(), null, UnitEnum.getUnitForShort("m^3"));
        });
    }

    @Test
    void increaseQuantityByIdShouldThrowIllegalArgumentExceptionUnitIsNull() {
        ProductRepository productRepository = new ProductRepository();

        Product product = ProductBuilder.builder()
                .name("Coca-Cola")
                .quantity(new BigDecimal("1.0"))
                .unit(UnitEnum.MG)
                .build();
        Product addedProduct = productRepository.addProduct(product);

        assertThrows(IllegalArgumentException.class, () -> {
            productRepository.increaseQuantityById(addedProduct.id(), new BigDecimal(1), null);
        });
    }

    @ParameterizedTest
    @CsvSource({
            "100, g, 10, g, 90",
            "100, g, 20, mg, 99.980",
            "30, kg, 100, g, 29.900",
            "10, mg, 5, g, -4990", // negative values possible
            "10, mg, 1, t, -999999990", // negative values possible
            "1, t, 10, mg, 0.999999990",
            "1, t, 10, g, 0.999990",
            "1, l, 250, ml, 0.750",
            "500, ml, 25, cl, 250",
            "500, ml, 25, dl, -2000",
            "500, ml, 1, m^3, -999500",
            "2, l, 500, ml, 1.500"
    })
    void decreaseQuantityByIdShouldDecreaseTheQuantityByAddingUpAndNoticingTheConversionFactor(
            String existing,
            String existingUnit,
            String reduce,
            String reduceUnit,
            String result) {
        ProductRepository productRepository = new ProductRepository();

        Product product = ProductBuilder.builder()
                .name("Coca-Cola")
                .quantity(new BigDecimal(existing))
                .unit(UnitEnum.getUnitForShort(existingUnit))
                .build();

        Product addedProduct = productRepository.addProduct(product);

        addedProduct = productRepository.decreaseQuantityById(addedProduct.id(), new BigDecimal(reduce), UnitEnum.getUnitForShort(reduceUnit));

        assertThat(addedProduct.quantity()).isEqualTo(new BigDecimal(result));
    }

    @ParameterizedTest
    @CsvSource({
            "g, l",
            "g, ml",
            "g, m^3",
            "g, p",
            "mg, l",
            "mg, ml",
            "mg, ml",
            "kg, m^3",
            "kg, l",
            "kg, m^3",
            "kg, p",
            "ml, p",
    })
    void decreaseQuantityByIdShouldThrowIllegalArgumentExceptionWhenUnitsNotConvertible(
            String existingUnit,
            String addUpUnit
    ) {
        ProductRepository productRepository = new ProductRepository();

        Product product = ProductBuilder.builder()
                .name("Coca-Cola")
                .quantity(new BigDecimal("1.0"))
                .unit(UnitEnum.getUnitForShort(existingUnit))
                .build();

        final Product addedProduct = productRepository.addProduct(product);

        assertThrows(IllegalArgumentException.class, () -> {
            productRepository.decreaseQuantityById(addedProduct.id(), new BigDecimal("1.0"), UnitEnum.getUnitForShort(addUpUnit));
        });
    }

    @Test
    void decreaseQuantityByIdShouldThrowIllegalArgumentExceptionWhenProductIdIsNull() {
        ProductRepository productRepository = new ProductRepository();
        assertThrows(IllegalArgumentException.class, () -> {
            productRepository.decreaseQuantityById(null, new BigDecimal("1.0"), UnitEnum.getUnitForShort("m^3"));
        });
    }

    @Test
    void decreaseQuantityByIdShouldThrowIllegalArgumentExceptionWhenQuantityIsNull() {
        ProductRepository productRepository = new ProductRepository();

        Product product = ProductBuilder.builder()
                .name("Coca-Cola")
                .quantity(new BigDecimal("1.0"))
                .unit(UnitEnum.MG)
                .build();
        Product addedProduct = productRepository.addProduct(product);

        assertThrows(IllegalArgumentException.class, () -> {
            productRepository.decreaseQuantityById(addedProduct.id(), null, UnitEnum.getUnitForShort("m^3"));
        });
    }

    @Test
    void decreaseQuantityByIdShouldThrowIllegalArgumentExceptionUnitIsNull() {
        ProductRepository productRepository = new ProductRepository();

        Product product = ProductBuilder.builder()
                .name("Coca-Cola")
                .quantity(new BigDecimal("1.0"))
                .unit(UnitEnum.MG)
                .build();
        Product addedProduct = productRepository.addProduct(product);

        assertThrows(IllegalArgumentException.class, () -> {
            productRepository.decreaseQuantityById(addedProduct.id(), new BigDecimal(1), null);
        });
    }
}