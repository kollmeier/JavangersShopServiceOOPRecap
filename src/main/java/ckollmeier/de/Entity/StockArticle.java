package ckollmeier.de.Entity;

import ckollmeier.de.Entity.Interface.ProductInterface;
import ckollmeier.de.Enum.UnitEnum;
import io.soabase.recordbuilder.core.RecordBuilder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

@RecordBuilder
public record StockArticle(
    @NotBlank(message = "Id must not be empty") String id,
    @NotNull(message = "Product must not be null") Product product,
    @PositiveOrZero(message = "Quantity must be positive or zero") BigDecimal quantity,
    @NotNull(message = "Unit must not be null") UnitEnum unit,
    @PositiveOrZero(message = "Price must be positive or zero") BigDecimal price
) implements StockArticleBuilder.With, ProductInterface {
    @Override
    public String name() {
        return product.name();
    }

    @Override
    public String description() {
        return product.description();
    }

    @Override
    public BigDecimal content() {
        return product.content();
    }

    @Override
    public String productId() {
        return product.productId();
    }
}
