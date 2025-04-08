package ckollmeier.de.Entity;

import ckollmeier.de.Enum.UnitEnum;
import io.soabase.recordbuilder.core.RecordBuilder;

import java.math.BigDecimal;

@RecordBuilder
public record StockArticle(
    String id,
    Product product,
    BigDecimal quantity,
    UnitEnum unit,
    BigDecimal price
) implements StockArticleBuilder.With {
}
