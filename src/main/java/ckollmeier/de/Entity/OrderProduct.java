package ckollmeier.de.Entity;

import ckollmeier.de.Entity.Interface.ProductInterface;
import ckollmeier.de.Enum.UnitEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@RequiredArgsConstructor
public final class OrderProduct implements ProductInterface {
    private @NotBlank(message = "Id must not be empty") String id;

    private final @NotNull(message = "StockArticle must not be null") StockArticle stockArticle;

    @Setter
    private @PositiveOrZero(message = "Quantity must be positive") BigDecimal quantity;

    private @PositiveOrZero(message = "Price must be positive") BigDecimal priceSubTotal;

    public void setQuantity(final BigDecimal newQuantity, final UnitEnum unit) {
        BigDecimal factor = stockArticle.unit().conversionFactor(unit);
        BigDecimal convertedQuantity = newQuantity.multiply(factor);
        setQuantity(convertedQuantity);
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public String name() {
        return stockArticle.name();
    }

    @Override
    public String description() {
        return stockArticle.description();
    }

    @Override
    public UnitEnum unit() {
        return stockArticle.unit();
    }

    @Override
    public BigDecimal content() {
        return stockArticle.content();
    }

    @Override
    public String productId() {
        return stockArticle.productId();
    }
}
