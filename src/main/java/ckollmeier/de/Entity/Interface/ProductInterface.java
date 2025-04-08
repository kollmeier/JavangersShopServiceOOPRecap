package ckollmeier.de.Entity.Interface;

import ckollmeier.de.Enum.UnitEnum;

import java.math.BigDecimal;

public interface ProductInterface {
    String id();
    String name();
    String description();
    UnitEnum unit();
    BigDecimal content();
    String productId();
}
