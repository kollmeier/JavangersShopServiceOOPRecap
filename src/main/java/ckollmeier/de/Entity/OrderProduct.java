package ckollmeier.de.Entity;

import ckollmeier.de.Entity.Interface.ProductInterface;
import ckollmeier.de.Enum.UnitEnum;

import java.math.BigDecimal;

public final class OrderProduct implements ProductInterface {
    /**
     * unique id.
     */
    private String id;
    /**
     * article to order.
     */
    private StockArticle stockArticle;
    /**
     * quantity to order.
     */
    private BigDecimal quantity;
    /**
     * price sum.
     */
    private BigDecimal priceSubTotal;


    public void setQuantity(final BigDecimal quantity) {
        this.quantity = quantity;
        this.priceSubTotal = stockArticle.price().multiply(quantity);
    }

    public void setQuantity(final BigDecimal newQuantity, final UnitEnum unit) {
        BigDecimal factor = stockArticle.unit().conversionFactor(unit);
        BigDecimal convertedQuantity = newQuantity.multiply(factor);
        setQuantity(convertedQuantity);
    }

    public StockArticle getStockArticle() {
        return stockArticle;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public BigDecimal getPriceSubTotal() {
        return priceSubTotal;
    }

    public OrderProduct(final StockArticle stockArticle) {
        this.stockArticle = stockArticle;
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
