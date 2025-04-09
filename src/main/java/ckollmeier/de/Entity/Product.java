package ckollmeier.de.Entity;

import ckollmeier.de.Entity.Interface.ProductInterface;
import ckollmeier.de.Enum.UnitEnum;
import io.soabase.recordbuilder.core.RecordBuilder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@RecordBuilder
public record Product(
        @NotBlank(message = "Id must not be empty") String id,
        @NotBlank(message = "Name must not be empty") String name,
        @Positive(message = "Content must be positive") BigDecimal content,
        @NotNull(message = "Unit must not be null") UnitEnum unit,
        @NotNull(message = "Description must not be null") String description
) implements ProductBuilder.With, ProductInterface {
    @Override
    public String productId() {
        return id;
    }
}
