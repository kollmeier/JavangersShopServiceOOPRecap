package ckollmeier.de.Enum;

import java.math.BigDecimal;
import java.math.RoundingMode;

public enum UnitEnum {
    /**
     * Miligram.
     */
    MG("Milligram", "mg", new BigDecimal("0.001"), "g", 0),
    /**
     * Gram.
     */
    G("Gram", "g", new BigDecimal(1), "g", 2),
    /**
     * Kilogram.
     */
    KG("Kilogram", "kg", new BigDecimal("1000"), "g", 3),
    /**
     * Ton.
     */
    T("Ton", "t", new BigDecimal("1000000"), "g", 2),

    /**
     * Milliliter.
     */
    ML("Milliliter", "ml", new BigDecimal("0.001"), "l", 0),
    /**
     * Centiliter.
     */
    CL("Centiliter", "cl", new BigDecimal("0.01"), "l", 1),
    /**
     * Deziliter.
     */
    DL("Deziliter", "dl", new BigDecimal("0.1"), "l", 2),
    /**
     * Liter.
     */
    L("Liter", "l", new BigDecimal("1"), "l", 3),
    /**
     * Cubic metre.
     */
    M3("Cubic metre", "m^3", new BigDecimal("1000"), "l", 2),

    /**
     * Pieces.
     */
    PCS("Pieces", "p", new BigDecimal("1"), "p", 0);

    /**
     * name of the unit.
     */
    private String unitName;
    /**
     * short name of the unit.
     */
    private String shortName;
    /**
     * factor for conversion.
     */
    private BigDecimal factor;
    /**
     * base unit for conversions.
     */
    private String base;
    /**
     * precision of this unit.
     */
    private int precision;

    UnitEnum(
            final String unitName,
            final String shortName,
            final BigDecimal factor,
            final String base,
            final int precision) {
        this.unitName = unitName;
        this.shortName = shortName;
        this.factor = factor;
        this.base = base;
    }

    public String getUnitName() {
        return unitName;
    }

    public String getShortName() {
        return shortName;
    }

    public BigDecimal getFactor() {
        return factor;
    }

    private UnitEnum getBaseUnit() {
        return switch (base) {
            case "g" -> G;
            case "kg" -> KG;
            case "t" -> T;
            case "ml" -> ML;
            case "cl" -> CL;
            case "dl" -> DL;
            case "l" -> L;
            case "m" -> M3;
            case "p" -> PCS;
            default -> throw new IllegalStateException("Unexpected value: " + base);
        };
    }

    /**
     * @param toUnit unit to convert to
     * @return conversion factor to convert to toUnit
     */
    public BigDecimal conversionFactor(final UnitEnum toUnit) {
        if (toUnit == null) {
            throw new IllegalArgumentException("null not allowed");
        }
        if (!base.equals(toUnit.base)) {
            throw new IllegalArgumentException("units not convertible");
        }
        // into base unit
        BigDecimal inBase = getBaseUnit().factor.divide(factor, toUnit.precision, RoundingMode.HALF_UP);
        return inBase.multiply(toUnit.getFactor());
    }

    public int getPrecision() {
        return precision;
    }
}
