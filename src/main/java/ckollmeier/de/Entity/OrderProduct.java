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
    private int quantity;
    /**
     * price sum.
     */
    private BigDecimal priceSubTotal;


    public void setQuantity(final int quantity) {
        this.quantity = quantity;
        this.priceSubTotal = stockArticle.price().multiply(new BigDecimal(quantity));
    }

    public StockArticle getStockArticle() {
        return stockArticle;
    }

    public int getQuantity() {
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
