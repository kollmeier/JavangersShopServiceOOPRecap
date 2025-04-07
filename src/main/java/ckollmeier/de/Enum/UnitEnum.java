package ckollmeier.de.Enum;

import java.math.BigDecimal;
import java.math.RoundingMode;

public enum UnitEnum {
    /**
     * Miligram.
     */
    MG("Milligram", "mg", new BigDecimal("0.001"), "g"),
    /**
     * Gram.
     */
    G("Gram", "g", new BigDecimal(1), "g"),
    /**
     * Kilogram.
     */
    KG("Kilogram", "kg", new BigDecimal("1000"), "g"),
    /**
     * Ton.
     */
    T("Ton", "t", new BigDecimal("1000000"), "g"),

    /**
     * Milliliter.
     */
    ML("Milliliter", "ml", new BigDecimal("0.001"), "l"),
    /**
     * Centiliter.
     */
    CL("Centiliter", "cl", new BigDecimal("0.01"), "l"),
    /**
     * Deziliter.
     */
    DL("Deziliter", "dl", new BigDecimal("0.1"), "l"),
    /**
     * Liter.
     */
    L("Liter", "l", new BigDecimal("1"), "l"),
    /**
     * Cubic metre.
     */
    M3("Cubic metre", "m^3", new BigDecimal("1000"), "l"),

    /**
     * Pieces.
     */
    PCS("Pieces", "p", new BigDecimal("1"), "p");

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
     * scale of this unit.
     */
    private int scale;

    UnitEnum(
            final String unitName,
            final String shortName,
            final BigDecimal factor,
            final String base
    ) {
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

    /**
     * @param unitString string to match
     * @return unitEnum for the string
     */
    public static UnitEnum getUnitForShort(final String unitString) {
        return switch (unitString) {
            case "mg" -> MG;
            case "g" -> G;
            case "kg" -> KG;
            case "t" -> T;
            case "ml" -> ML;
            case "cl" -> CL;
            case "dl" -> DL;
            case "l" -> L;
            case "m^3" -> M3;
            case "p" -> PCS;
            default -> throw new IllegalStateException("Unexpected value: " + unitString);
        };
    }

    private UnitEnum getBaseUnit() {
        return getUnitForShort(base);
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
        BigDecimal intoBase = getBaseUnit().factor.divide(factor);
        BigDecimal intoUnit = intoBase.multiply(toUnit.getFactor());
        return intoUnit.setScale(Math.max(toUnit.scale, intoUnit.scale()), RoundingMode.HALF_UP);
    }

    public int getScale() {
        return scale;
    }
}
