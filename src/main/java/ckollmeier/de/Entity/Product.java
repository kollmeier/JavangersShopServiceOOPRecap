package ckollmeier.de.Entity;

import ckollmeier.de.Enum.UnitEnum;
import io.soabase.recordbuilder.core.RecordBuilder;

import java.math.BigDecimal;

@RecordBuilder
public record Product(
        String id,
        String name,
        BigDecimal quantity,
        UnitEnum unit,
        BigDecimal content,
        UnitEnum contentUnit,
        BigDecimal price,
        String description
) implements ProductBuilder.With {
}
