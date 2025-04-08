package ckollmeier.de.Entity;

import io.soabase.recordbuilder.core.RecordBuilder;

import java.util.List;

@RecordBuilder
public record Order(
        String id,
        List<Product> products
) implements OrderBuilder.With {
}
