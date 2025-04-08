package ckollmeier.de.Entity;

import ckollmeier.de.Enum.UnitEnum;
import io.soabase.recordbuilder.core.RecordBuilder;

import java.math.BigDecimal;

@RecordBuilder
public record Product(
        String id,
        String name,
        BigDecimal content,
        UnitEnum unit,
        String description
) implements ProductBuilder.With {
}
