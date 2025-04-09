package ckollmeier.de.Entity;

import io.soabase.recordbuilder.core.RecordBuilder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@RecordBuilder
public record Order(
        @NotBlank(message = "Id must not be empty") String id,
        @NotNull(message = "Products must not be null") List<OrderProduct> products
) implements OrderBuilder.With {
}
