package ckollmeier.de.Entity;

import ckollmeier.de.Entity.Interface.ProductInterface;
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
